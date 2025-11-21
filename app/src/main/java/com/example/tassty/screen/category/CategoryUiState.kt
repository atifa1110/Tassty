package com.example.tassty.screen.category

import com.example.core.data.model.Resource
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.buildSummaryChips
import com.example.tassty.model.FilterKey

import com.example.tassty.model.FilterState
import com.example.tassty.model.SummaryFilterChip
import com.example.tassty.screen.search.SearchEvent

data class CategoryUiState(
    val restaurants: List<RestaurantUiModel> = emptyList(),
    val totalCount: Int = 0,

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

    val queryResult: Resource<List<RestaurantMenuUiModel>> = Resource(emptyList())
){
    val activeSummaryChips: List<SummaryFilterChip>
        get() = buildSummaryChips(
            sort = sortBy ,
            activeFilters = activeFilters
        )
}

sealed interface CategoryEvent {
    data class onQueryChange(val query: String) : CategoryEvent
    object ShowFilterSheet : CategoryEvent

    data class UpdateDraftFilter(
        val filterKey: FilterKey,
        val value: String
    ) : CategoryEvent

    object ResetFilter: CategoryEvent
    object ApplyFilters : CategoryEvent
}