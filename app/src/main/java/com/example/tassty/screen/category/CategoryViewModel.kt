package com.example.tassty.screen.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetFilterOptionsUseCase
import com.example.core.domain.usecase.GetSearchRestaurantsByCategoryUseCase
import com.example.core.domain.utils.RestaurantSearchFilter
import com.example.core.ui.utils.mapToResource
import com.example.core.ui.utils.FilterManager
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.model.mapToActiveFilters
import com.example.tassty.navigation.CategoryDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val filterManager: FilterManager,
    private val getFilterOptionsUseCase: GetFilterOptionsUseCase,
    private val getSearchRestaurantsByCategoryUseCase: GetSearchRestaurantsByCategoryUseCase
) : ViewModel(), FilterManager by filterManager {

    val id = CategoryDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(CategoryInternalState())

    private val filterFlow = getFilterOptionsUseCase().map {
        it.mapToResource { data ->
            FilterState(
                sortList = data.sortOptions.map { it.toUiModel(FilterCategory.SORT) },
                priceRanges = data.priceRangeOptions.map { it.toUiModel(FilterCategory.PRICE) },
                ratingsOptions = data.ratingOptions.map { it.toUiModel(FilterCategory.RATING) },
                modesOptions = data.modeOptions.map { it.toUiModel(FilterCategory.MODE) },
                cuisineOptions = data.cuisineOptions.map { it.toUiModel(FilterCategory.CUISINE) }
            )
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val restaurantFlow = combine(
        _internalState.map { it.query }.distinctUntilChanged().debounce { if (it.isNotEmpty()) 800L else 0L },
        filterData
    ) { query, filters ->
        RestaurantSearchFilter(
            keyword = query.ifEmpty { "" },
            sorting = filters.applied[FilterCategory.SORT],
            priceRange = filters.applied[FilterCategory.PRICE],
            minRating = filters.applied[FilterCategory.RATING],
            mode = filters.applied[FilterCategory.MODE],
            cuisineId = filters.applied[FilterCategory.CUISINE]
        )
    }.flatMapLatest { finalFilter ->
        getSearchRestaurantsByCategoryUseCase(id, finalFilter)
            .map { response ->
                response.mapToResource { data ->
                    data.map { it.toUiModel() }
                }
            }
    }

    val uiState: StateFlow<CategoryUiState> = combine(
        _internalState,
        filterFlow,
        filterData,
        restaurantFlow
    ) { internal, filterMaster, filterData, restaurant ->

        val data = filterMaster.data ?: return@combine CategoryUiState(
            query = internal.query,
            restaurants = restaurant,
            activeFilters = emptyList()
        )

        val sortedList = data.sortList.map { it.copy(isSelected = it.key == filterData.selected[FilterCategory.SORT]) }
        val priceList = data.priceRanges.map { it.copy(isSelected = it.key == filterData.selected[FilterCategory.PRICE]) }
        val ratingList = data.ratingsOptions.map { it.copy(isSelected = it.key == filterData.selected[FilterCategory.RATING]) }
        val modeList = data.modesOptions.map { it.copy(isSelected = it.key == filterData.selected[FilterCategory.MODE]) }
        val cuisineList = data.cuisineOptions.map { it.copy(isSelected = it.key == filterData.selected[FilterCategory.CUISINE]) }

        CategoryUiState(
            query = internal.query,
            sortList = sortedList,
            priceRanges = priceList,
            ratingsOptions = ratingList,
            modesOptions = modeList,
            cuisineOptions = cuisineList,
            activeFilters = mapToActiveFilters(filterData, data),
            restaurants = restaurant,
            isFilterSheetVisible = internal.isFilterSheetVisible,
            isSortSheetVisible = internal.isSortSheetVisible
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState()
    )

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.onQueryChange -> _internalState.update { it.copy(query = event.query) }
            is CategoryEvent.ShowFilterSheet -> _internalState.update { it.copy(isFilterSheetVisible = true) }
            is CategoryEvent.ShowSortSheet -> _internalState.update { it.copy(isSortSheetVisible = true) }
            is CategoryEvent.UpdateDraftFilter -> onFilterSelected(event.category, event.value)
            is CategoryEvent.ApplyFilters -> {
                applyFilters()
                _internalState.update { it.copy(isFilterSheetVisible = false) }
            }
            is CategoryEvent.ApplySort -> {
                applySort()
                _internalState.update { it.copy(isSortSheetVisible = false) }
            }
            is CategoryEvent.ResetFilter -> {
                resetFilters()
                _internalState.update { it.copy(isFilterSheetVisible = false) }
            }
            is CategoryEvent.ResetSort -> resetSort()
        }
    }
}