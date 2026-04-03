package com.example.tassty.screen.category

import androidx.compose.runtime.Immutable
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.RestaurantMenuUiModel

@Immutable
data class CategoryUiState(
    val query: String = "",
    val sortList: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val priceRanges: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val ratingsOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val modesOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val cuisineOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val activeFilters: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,
    val restaurants: Resource<List<RestaurantMenuUiModel>> = Resource(),
)

data class CategoryInternalState(
    val query: String = "",
    val isFilterSheetVisible: Boolean = false,
    val isSortSheetVisible: Boolean = false,
)

data class FilterState(
    val sortList: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val priceRanges: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val ratingsOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val modesOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val cuisineOptions: List<FilterOptionUi<FilterCategory>> = emptyList(),
    val activeFilters: List<FilterOptionUi<FilterCategory>> = emptyList(),
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