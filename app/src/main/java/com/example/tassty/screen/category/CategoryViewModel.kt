package com.example.tassty.screen.category

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetFilterOptionsUseCase
import com.example.core.domain.usecase.GetSearchRestaurantsByCategoryUseCase
import com.example.core.domain.utils.RestaurantSearchFilter
import com.example.core.domain.utils.mapToResource
import com.example.core.ui.FilterManager
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
        it.mapToResource { data->
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
            sorting = filters.appliedSort,
            priceRange = filters.appliedPrice,
            minRating = filters.appliedRating,
            mode = filters.appliedMode,
            cuisineId = filters.appliedCuisine
        )
    }.flatMapLatest { finalFilter ->
        getSearchRestaurantsByCategoryUseCase(id, finalFilter)
            .map { response ->
                response.mapToResource { data ->
                    data.map { it.toUiModel() }
                }
            }
    }

    val uiState : StateFlow<CategoryUiState> = combine(
        _internalState,
        filterFlow,
        filterData,
        restaurantFlow
    ) { internal, filterMaster,filterData, restaurant ->

        val data = filterMaster.data ?: return@combine CategoryUiState()

        val sortedList = data.sortList.map { it.copy(isSelected = it.key == filterData.selectedSort) }
        val priceList = data.priceRanges.map { it.copy(isSelected = it.key == filterData.selectedPrice) }
        val ratingList = data.ratingsOptions.map { it.copy(isSelected = it.key == filterData.selectedRating) }
        val modeList = data.modesOptions.map { it.copy(isSelected = it.key == filterData.selectedMode) }
        val cuisineList = data.cuisineOptions.map { it.copy(isSelected = it.key == filterData.selectedCuisine) }

        // 3. Return State ke UI
        CategoryUiState(
            query = internal.query,
            sortList = sortedList,
            priceRanges = priceList,
            ratingsOptions = ratingList,
            modesOptions = modeList,
            cuisineOptions = cuisineList,
            activeFilters = mapToActiveFilters(filterData,data),
            restaurants = restaurant,
            isFilterSheetVisible = internal.isFilterSheetVisible,
            isSortSheetVisible = internal.isSortSheetVisible
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState()
    )

    /**
     * Called every time search text changes (from user input).
     */
    private fun onQueryChange(query: String) {
        _internalState.update {
            it.copy(query = query)
        }
    }

    fun onEvent(event: CategoryEvent) {
        when(event){
            is CategoryEvent.onQueryChange -> onQueryChange(event.query)
            is CategoryEvent.ShowFilterSheet -> _internalState.update { it.copy(isFilterSheetVisible = true) }
            is CategoryEvent.ShowSortSheet -> _internalState.update { it.copy(isSortSheetVisible = true) }
            is CategoryEvent.UpdateDraftFilter -> onFilterSelected(event.category,event.value)
            is CategoryEvent.ApplyFilters -> {
                _internalState.update { currentState ->
                    applyFilters()
                    currentState.copy(
                        isFilterSheetVisible = false,
                    )
                }
            }
            is CategoryEvent.ApplySort-> {
                _internalState.update { currentState->
                    applySort()
                    currentState.copy(
                        isSortSheetVisible = false,
                    )
                }
            }
            is CategoryEvent.ResetFilter -> {
                _internalState.update {
                    resetFilters()
                    it.copy(isFilterSheetVisible = false)
                }
            }
            is CategoryEvent.ResetSort -> {
                resetSort()
            }
        }
    }

}

