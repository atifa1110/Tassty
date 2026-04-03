package com.example.tassty.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetFilterOptionsUseCase
import com.example.core.domain.usecase.GetMenuYouSearchUseCase
import com.example.core.domain.usecase.GetRestaurantYouSearchUseCase
import com.example.core.domain.usecase.GetSearchRestaurantUseCase
import com.example.core.domain.utils.RestaurantSearchFilter
import com.example.core.ui.utils.mapToResource
import com.example.core.ui.utils.toListState
import com.example.core.ui.utils.FilterManager
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.model.mapToActiveFilters
import com.example.tassty.screen.category.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.collections.map

/**
 * UI Architecture Pattern:
 * - **MVI (Model-View-Intent)**: The UI sends "Events" (Intents) and observes a single "State".
 * - **_internalState**: Holds temporary UI logic state (e.g., query text, visibility of sheets).
 * - **uiState**: The final, immutable state exposed to the UI, combined from all data streams.
 */

/**
 * ViewModel for the Search screen that orchestrates the entire search and discovery experience.
 * * This class handles:
 * - **Filter Management**: Delegates filter logic to [filterManager] to set selected filters into active states seamlessly.
 * - **Discovery Content**: Populates the initial page by fetching categories, featured restaurants, and menus through dedicated UseCases.
 * - **Dynamic Searching**: Powering the search page by handling real-time, debounced restaurant queries and complex filtering.
 * - **State Synchronization**: Merging multiple data streams into a single, reactive [uiState] for the UI to consume.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val filterManager: FilterManager,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getSearchRestaurantUseCase: GetSearchRestaurantUseCase,
    private val getRestaurantUseCase: GetRestaurantYouSearchUseCase,
    private val getMenuSearchUseCase: GetMenuYouSearchUseCase,
    private val getFilterOptionsUseCase: GetFilterOptionsUseCase
) : ViewModel(), FilterManager by filterManager {

    private val _internalState = MutableStateFlow(SearchInternalState())

    /**
     * Loads and prepares the master filter options from the server.
     * Transforms raw filter data (sort, price, ratings, etc.) into UI-ready models.
     */
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

    /**
     * Combines multiple data sources (Categories, Featured Restaurants, and Menus).
     * Provides the default "Discovery" content shown before the user starts searching.
     */
    private val contentFlow = combine(
        getAllCategoriesUseCase(),
        getRestaurantUseCase(),
        getMenuSearchUseCase()
    ) { categories, restaurants, menus ->
        SearchContent(
            categories = categories.toListState { it.toUiModel() },
            restaurants = restaurants.toListState { it.toUiModel() },
            menus = menus.toListState { it.toUiModel() }
        )
    }

    /**
     * Automatically manages search results based on user input and selected filters.
     * * Logic Flow:
     * 1. Monitoring: Watches for changes in the search text and filter options.
     * 2. Instant Feedback: Immediately triggers a 'loading' state when the user types.
     * 3. Efficiency: Uses an 800ms debounce to wait for the user to finish typing before calling the API.
     * 4. Cancellation: Automatically cancels old search requests if a new one is started.
     * 5. Cleanup: Returns an empty result instantly if the search box is cleared.
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchResultFlow = _internalState
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(Resource(data = emptyList()))
            } else {
                filterData.flatMapLatest { filters ->
                    flow {
                        emit(Resource(isLoading = true))
                        delay(800L)

                        val finalFilter = RestaurantSearchFilter(
                            keyword = query,
                            sorting = filters.appliedSort,
                            priceRange = filters.appliedPrice,
                            minRating = filters.appliedRating,
                            mode = filters.appliedMode,
                            cuisineId = filters.appliedCuisine
                        )

                        getSearchRestaurantUseCase(finalFilter).collect { response ->
                            emit(response.mapToResource { data ->
                                data.map { it.toUiModel() }
                            })
                        }
                    }
                }
            }
        }

    /**
     * Merges all data streams into a single source of truth for the UI.
     * Consolidates search results, filter selections, and discovery content.
     */
    val uiState: StateFlow<SearchUiState> = combine(
        _internalState,
        filterFlow,
        filterData,
        contentFlow,
        searchResultFlow
    ){ internal, filterMaster, filterData, content, search ->
        val data = filterMaster.data?: return@combine SearchUiState()

        val sortedList = data.sortList.map { it.copy(isSelected = it.key == filterData.selectedSort) }
        val priceList = data.priceRanges.map { it.copy(isSelected = it.key == filterData.selectedPrice) }
        val ratingList = data.ratingsOptions.map { it.copy(isSelected = it.key == filterData.selectedRating) }
        val modeList = data.modesOptions.map { it.copy(isSelected = it.key == filterData.selectedMode) }
        val cuisineList = data.cuisineOptions.map { it.copy(isSelected = it.key == filterData.selectedCuisine) }

        SearchUiState(
            isSearching = internal.query.isNotEmpty(),
            isSortSheetVisible = internal.isSortSheetVisible,
            isFilterSheetVisible = internal.isFilterSheetVisible,
            queryResult = search,
            categories = content.categories,
            restaurants = content.restaurants,
            menus = content.menus,
            sortList = sortedList,
            priceRanges = priceList,
            ratingsOptions = ratingList,
            modesOptions = modeList,
            cuisineOptions = cuisineList,
            query = internal.query,
            activeFilters = mapToActiveFilters(filterData,data)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun onEvent(event: SearchEvent) {
        when(event){
            is SearchEvent.ChangeQuery -> onQueryChange(event.query)
            is SearchEvent.Refresh -> {}
            is SearchEvent.Retry -> {}
            is SearchEvent.ClearError -> {}
            is SearchEvent.ShowFilterSheet -> _internalState.update { it.copy(isFilterSheetVisible = true) }
            is SearchEvent.UpdateDraftFilter -> onFilterSelected(event.category,event.value)
            is SearchEvent.ApplyFilters -> {
                _internalState.update {
                    applyFilters()
                    it.copy(isFilterSheetVisible = false)
                }
            }

            is SearchEvent.ResetFilter -> {
                _internalState.update {
                    resetFilters()
                    it.copy(isFilterSheetVisible = false)
                }
            }
            // Sort
            is SearchEvent.ShowSortSheet -> _internalState.update { it.copy(isSortSheetVisible = true) }
            is SearchEvent.ApplySort -> {
                _internalState.update {
                    applySort()
                    it.copy(isSortSheetVisible = false)
                }
            }

            is SearchEvent.ResetSort -> {
                _internalState.update {
                    resetSort()
                    it.copy(isSortSheetVisible = false)
                }
            }

        }
    }

    /**
     * Updates the search query and toggles the search screen visibility.
     * Determines whether to show discovery content or search results.
     */
    private fun onQueryChange(query: String) {
        val isSearching = query.isNotBlank()
        _internalState.update {
            it.copy(
                query = query,
                isSearching = isSearching
            )
        }
    }
}

