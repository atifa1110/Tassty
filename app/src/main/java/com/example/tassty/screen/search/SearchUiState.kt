package com.example.tassty.screen.search

import androidx.compose.runtime.Immutable
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.util.historyOptions
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.util.popularOptions
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class SearchUiState(
    val history: Resource<List<ChipFilterOption>> = Resource(data = historyOptions),
    val popular: Resource<List<ChipFilterOption>> = Resource(data = popularOptions),
    val categories: Resource<ImmutableList<CategoryUiModel>> = Resource(),
    val restaurants: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val menus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val queryResult: Resource<List<RestaurantMenuUiModel>> = Resource(),

    val isSearching: Boolean = false,
    val query: String = "",

    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,

    val sortList : List<FilterOptionUi<FilterCategory>> = emptyList(),
    val priceRanges: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val ratingsOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val modesOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val cuisineOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val activeFilters: List<FilterOptionUi<FilterCategory>> = emptyList(),
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
    val categories: Resource<ImmutableList<CategoryUiModel>> = Resource(),
    val restaurants: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val menus: Resource<ImmutableList<MenuUiModel>> = Resource(),
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
