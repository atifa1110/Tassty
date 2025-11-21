package com.example.tassty.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetFilterOptionsUseCase
import com.example.core.domain.usecase.GetSearchFilterUseCase
import com.example.core.domain.usecase.GetSearchMenusUseCase
import com.example.core.domain.usecase.GetSearchRestaurantUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.historyOptions
import com.example.tassty.model.FilterKey
import com.example.tassty.model.FilterState
import com.example.tassty.model.toUi
import com.example.tassty.popularOptions
import com.example.tassty.screen.home.toListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface EventHandler<E> {
    fun onEvent(event: E)
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchFilterUseCase: GetSearchFilterUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getSearchMenusUseCase: GetSearchMenusUseCase,
    private val getSearchRestaurantUseCase: GetSearchRestaurantUseCase,
    private val getFilterOptionsUseCase: GetFilterOptionsUseCase
) : ViewModel(), EventHandler<SearchEvent>{

    private val _uiState = MutableStateFlow(SearchUiState(
        history = Resource(data = historyOptions),
        popular = Resource(data = popularOptions)
    ))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private val SEARCH_DELAY = 800L

    override fun onEvent(event: SearchEvent) {
        when(event){
            is SearchEvent.ChangeQuery -> onQueryChange(event.query)
            is SearchEvent.Refresh -> {}
            is SearchEvent.Retry -> {}
            is SearchEvent.ClearError -> {}
            is SearchEvent.ShowFilterSheet -> {
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

            is SearchEvent.UpdateDraftFilter -> {
                onFilterSelected(event.filterKey,event.value)
            }

            is SearchEvent.ApplyFilters -> {
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
                    search()
                }
            }

            is SearchEvent.ResetFilter->{
                _uiState.update {
                    it.copy(activeFilters = FilterState(), isFilterSheetVisible = false)
                }
            }
            // Sort
            is SearchEvent.ShowSortSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isSortSheetVisible = true,
                        sortList = currentState.sortList.map { option ->
                            option.copy(isSelected = option.key == currentState.sortBy?.key)
                        }
                    )
                }
            }

            is SearchEvent.UpdateDraftSort -> {
                _uiState.update { currentState ->
                   currentState.copy(
                       sortList = currentState.sortList.map { option ->
                           option.copy(isSelected = option.key == event.sortKey)
                       }
                   )
                }
            }

            is SearchEvent.ApplySort -> {
                _uiState.update { currentState ->
                    val currentActiveFilters = currentState.activeFilters.copy()
                    currentState.copy(
                        activeFilters = currentActiveFilters,
                        sortBy = currentState.sortList.firstOrNull{it.isSelected},
                        isSortSheetVisible = false
                    )
                }.also {
                    search()
                }
            }

            is SearchEvent.ResetSort -> {
                _uiState.update {
                    it.copy(sortBy = null, isSortSheetVisible = false)
                }
            }

        }
    }

    init {
        loadInitialData()
        loadFilterData()
    }

    private fun loadFilterData(){
        viewModelScope.launch {
            getFilterOptionsUseCase.invoke().collect { result->
                when(result){
                    is TasstyResponse.Error -> _uiState.update { it.copy(errorMessage = result.meta.message) }
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
    /**
     * Load initial cached/home content (only once)
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            combine(
                getAllCategoriesUseCase(),
                getSearchRestaurantUseCase(),
                getSearchMenusUseCase()
            ) { categories, restaurant,menus, ->
                Triple(categories, restaurant,menus)
            }.collect { (categories, restaurants, menus) ->
                _uiState.update {
                    it.copy(
                        categories = categories.toListState { it.toUiModel() },
                        restaurants = restaurants.toListState { it.toUiModel() },
                        menus = menus.toListState { it.toUiModel() }
                    )
                }
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

    /**
     * Called every time search text changes (from user input).
     * Handles debounce and decides which state to show.
     */
    private fun onQueryChange(query: String) {
        val isSearching = query.isNotBlank()
        _uiState.update {
            it.copy(
                query = query,
                isSearching = isSearching,
                queryResult = if (isSearching) Resource(isLoading = true) else Resource(emptyList())
            )
        }
        searchDebounced()
    }

    private fun searchDebounced() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            search()
        }
    }

    private fun search(){
        viewModelScope.launch {
            val currentUiState = uiState.value
            val query = currentUiState.query
            val filter = currentUiState.activeFilters
            val sort = currentUiState.sortBy

            if (query.isBlank()) {
                _uiState.update { it.copy(queryResult = Resource(emptyList())) }
                return@launch
            }

            getSearchFilterUseCase.invoke().collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _uiState.update { it.copy(queryResult = Resource(isLoading = false),
                            errorMessage = result.meta.message)
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
                            ?.filter { it.restaurant.restaurant.name.contains(query, ignoreCase = true) }

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
}