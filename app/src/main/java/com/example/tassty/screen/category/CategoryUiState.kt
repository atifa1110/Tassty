package com.example.tassty.screen.category

import androidx.compose.runtime.Immutable
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.tassty.defaultFilter

@Immutable
data class CategoryUiState(
    val query: String = "",
    val sortList : List<FilterOptionUi> = emptyList(),
    val priceRanges: List<FilterOptionUi> = emptyList(),
    val ratingsOptions: List<FilterOptionUi> = emptyList(),
    val modesOptions: List<FilterOptionUi> = emptyList(),
    val cuisineOptions: List<FilterOptionUi> = emptyList(),
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,
    val activeFilters: List<FilterOptionUi> = emptyList(),
    val restaurants: Resource<List<RestaurantMenuUiModel>> = Resource(),
)

data class CategoryInternalState(
    val query: String = "",
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,
)

data class FilterState(
    val sortList : List<FilterOptionUi> = emptyList(),
    val priceRanges: List<FilterOptionUi> = emptyList(),
    val ratingsOptions: List<FilterOptionUi> = emptyList(),
    val modesOptions: List<FilterOptionUi> = emptyList(),
    val cuisineOptions: List<FilterOptionUi> = emptyList(),
    val activeFilters: List<FilterOptionUi> = defaultFilter,
)

sealed interface CategoryEvent {
    data class onQueryChange(val query: String) : CategoryEvent
    object ShowFilterSheet : CategoryEvent
    object ShowSortSheet : CategoryEvent

    data class UpdateDraftFilter(
        val category: FilterCategory,
        val value: String
    ) : CategoryEvent

    object ResetFilter: CategoryEvent
    object ResetSort: CategoryEvent
    object ApplyFilters : CategoryEvent
    object ApplySort : CategoryEvent
}