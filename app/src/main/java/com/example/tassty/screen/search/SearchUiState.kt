package com.example.tassty.screen.search

import androidx.compose.runtime.Immutable
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.historyOptions
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.popularOptions

@Immutable
data class SearchUiState(
    val history: Resource<List<ChipFilterOption>> = Resource(data = historyOptions),
    val popular: Resource<List<ChipFilterOption>> = Resource(data = popularOptions),
    val categories: Resource<List<CategoryUiModel>> = Resource(),
    val restaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val menus: Resource<List<MenuUiModel>> = Resource(),
    val queryResult: Resource<List<RestaurantMenuUiModel>> = Resource(),

    val isSearching: Boolean = false,
    val query: String = "",

    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,

    val sortList : List<FilterOptionUi> = emptyList(),
    val priceRanges: List<FilterOptionUi> = emptyList(),
    val ratingsOptions: List<FilterOptionUi> = emptyList(),
    val modesOptions: List<FilterOptionUi> = emptyList(),
    val cuisineOptions: List<FilterOptionUi> = emptyList(),
    val activeFilters: List<FilterOptionUi> = emptyList(),
)

data class SearchInternalState(
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,
    val isSearching: Boolean = false,
    val query: String = "",
)

data class SearchContent(
    val history: Resource<List<ChipFilterOption>> = Resource(data = historyOptions),
    val popular: Resource<List<ChipFilterOption>> = Resource(data = popularOptions),
    val categories: Resource<List<CategoryUiModel>> = Resource(),
    val restaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val menus: Resource<List<MenuUiModel>> = Resource(),
)
sealed interface SearchEvent {
    data class ChangeQuery(val query: String) : SearchEvent
    object Refresh : SearchEvent
    object Retry : SearchEvent
    object ClearError : SearchEvent

    object ShowSortSheet : SearchEvent
    object ResetFilter: SearchEvent
    object ApplySort : SearchEvent

    object ShowFilterSheet : SearchEvent

    data class UpdateDraftFilter(
        val category: FilterCategory,
        val value: String
    ) : SearchEvent

    object ResetSort: SearchEvent
    object ApplyFilters : SearchEvent
}
