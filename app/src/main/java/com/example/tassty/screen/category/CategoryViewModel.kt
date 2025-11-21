package com.example.tassty.screen.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetFilterOptionsUseCase
import com.example.core.domain.usecase.GetSearchFilterUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.model.FilterKey
import com.example.tassty.model.FilterState
import com.example.tassty.model.toUi
import com.example.tassty.screen.search.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFilterOptionsUseCase: GetFilterOptionsUseCase,
    private val getSearchFilterUseCase: GetSearchFilterUseCase
) : ViewModel() , EventHandler<CategoryEvent>{

    val category: String = savedStateHandle.get<String>("categoryName") ?: ""

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private val SEARCH_DELAY = 800L

    init {
        executeSearch()
        loadFilterData()
    }

    private fun loadFilterData(){
        viewModelScope.launch {
            getFilterOptionsUseCase.invoke().collect { result->
                when(result){
                    is TasstyResponse.Error -> {}
                    is TasstyResponse.Loading -> {}
                    is TasstyResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                sortList = result.data?.sortOptions?.map { it.toUi() } ?:emptyList(),
                                rupiahPriceRanges = result.data?.priceRangeOptions?.map { it.toUi() } ?:emptyList(),
                                restoRatingsOptions = result.data?.ratingOptions?.map { it.toUi() } ?:emptyList(),
                                discountOptions = result.data?.discountOptions?.map { it.toUi() } ?:emptyList(),
                                modesOptions = result.data?.modeOptions?.map { it.toUi() } ?:emptyList(),
                                cuisineOption = result.data?.cuisineOptions?.map { it.toUi() } ?:emptyList(),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(
                query = query
            )
        }
        searchDebounced()
    }

    private fun searchDebounced() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            executeSearch()
        }
    }

    private fun executeSearch(){
        viewModelScope.launch {
            getSearchFilterUseCase.invoke().collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _uiState.update { it.copy(queryResult = Resource(isLoading = false))
                        }
                    }
                    is TasstyResponse.Loading -> {
                        _uiState.update {
                            it.copy(queryResult = Resource(isLoading = true))
                        }
                    }
                    is TasstyResponse.Success -> {
                        val data = result.data?.map { data-> data.toUiModel() }
                        val filtered = data
                            ?.filter { it.restaurant.restaurant.name.contains(uiState.value.query, ignoreCase = true) }

                        _uiState.update {
                            it.copy(
                                queryResult = Resource(
                                    isLoading = false, data = filtered)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onEvent(event: CategoryEvent) {
        when(event){
            is CategoryEvent.onQueryChange -> onQueryChange(event.query)
            is CategoryEvent.ShowFilterSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isFilterSheetVisible = true,
                        rupiahPriceRanges = currentState.rupiahPriceRanges.map { option ->
                            option.copy(isSelected = option.key == currentState.activeFilters.priceRange?.key)
                        },
                        restoRatingsOptions = currentState.restoRatingsOptions.map { option ->
                            option.copy(isSelected = option.key == currentState.activeFilters.priceRange?.key)
                        },
                        discountOptions = currentState.discountOptions.map { option ->
                            option.copy(isSelected = option.key == currentState.activeFilters.discounts?.key)
                        },
                        modesOptions = currentState.modesOptions.map { option ->
                            option.copy(isSelected = option.key == currentState.activeFilters.mode?.key)
                        },
                        cuisineOption = currentState.cuisineOption.map { option ->
                            option.copy(isSelected = option.key == currentState.activeFilters.cuisine?.key)
                        }
                    )
                }
            }

            is CategoryEvent.UpdateDraftFilter -> onFilterSelected(event.filterKey,event.value)
            is CategoryEvent.ApplyFilters -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        activeFilters = FilterState(
                            priceRange = currentState.rupiahPriceRanges.firstOrNull{it.isSelected},
                            restoRating = currentState.restoRatingsOptions.firstOrNull{it.isSelected},
                            discounts = currentState.discountOptions.firstOrNull{it.isSelected},
                            mode = currentState.modesOptions.firstOrNull{it.isSelected},
                            cuisine = currentState.cuisineOption.firstOrNull{it.isSelected}
                        ),
                        isFilterSheetVisible = false
                    )
                }.also {
                    executeSearch()
                }
            }
            is CategoryEvent.ResetFilter -> _uiState.update {
                it.copy(activeFilters = FilterState(), isFilterSheetVisible = false)
            }
        }
    }

    fun onFilterSelected(filterKey: FilterKey, optionKey: String) {
        _uiState.update { current ->
            when (filterKey) {
                FilterKey.PriceRange -> current.copy(
                    rupiahPriceRanges = current.rupiahPriceRanges.map {
                        it.copy(isSelected = it.key == optionKey) // radio
                    }
                )
                FilterKey.RestoRating -> current.copy(
                    restoRatingsOptions = current.restoRatingsOptions.map {
                        it.copy(isSelected = it.key == optionKey) // radio
                    }
                )
                FilterKey.Discount -> current.copy(
                    discountOptions = current.discountOptions.map {
                        it.copy(isSelected = it.key == optionKey) // chip
                    }
                )
                FilterKey.Mode -> current.copy(
                    modesOptions = current.modesOptions.map {
                        it.copy(isSelected = it.key == optionKey) // radio
                    }
                )
                FilterKey.Cuisine -> current.copy(
                    cuisineOption = current.cuisineOption.map {
                        it.copy(isSelected = it.key == optionKey) // chip
                    }
                )
            }
        }
    }

}

