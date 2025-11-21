package com.example.tassty.screen.search

import com.example.core.data.model.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.buildSummaryChips
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.FilterKey
import com.example.tassty.model.FilterState
import com.example.tassty.model.SummaryFilterChip

data class SearchUiState(
    val history: Resource<List<ChipFilterOption>> = Resource(),
    val popular: Resource<List<ChipFilterOption>> = Resource(),
    val categories: Resource<List<CategoryUiModel>> = Resource(),
    val restaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val menus: Resource<List<MenuUiModel>> = Resource(),
    val queryResult: Resource<List<RestaurantMenuUiModel>> = Resource(emptyList()),
    val errorMessage: String? = null,
    val isSearching: Boolean = false,
    val query: String = "",

    val activeFilters: FilterState = FilterState(),
    val sortBy: FilterOptionUi? = null,

    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,

    val sortList : List<FilterOptionUi> = emptyList(),
    val rupiahPriceRanges: List<FilterOptionUi> = emptyList(),
    val restoRatingsOptions: List<FilterOptionUi> = emptyList(),
    val discountOptions: List<FilterOptionUi> = emptyList(),
    val modesOptions: List<FilterOptionUi> = emptyList(),
    val cuisineOption: List<FilterOptionUi> = emptyList(),
){
    val activeSummaryChips: List<SummaryFilterChip>
        get() = buildSummaryChips(
            sort = sortBy ,
            activeFilters = activeFilters
        )
}


sealed interface SearchEvent {
    data class ChangeQuery(val query: String) : SearchEvent
    object Refresh : SearchEvent
    object Retry : SearchEvent
    object ClearError : SearchEvent

    object ShowSortSheet : SearchEvent
    data class UpdateDraftSort(val sortKey: String): SearchEvent
    object ResetFilter: SearchEvent
    object ApplySort : SearchEvent

    object ShowFilterSheet : SearchEvent

    data class UpdateDraftFilter(
        val filterKey: FilterKey,
        val value: String
    ) : SearchEvent

    object ResetSort: SearchEvent
    object ApplyFilters : SearchEvent
}
