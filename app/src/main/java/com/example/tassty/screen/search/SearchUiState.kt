package com.example.tassty.screen.search

import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.FilterState

sealed class SearchUiState {
    // Initial State (When the query is empty and the keyboard is not yet active)
    data class Initial(
        val data: DataState,
        val status: StatusState
    ) : SearchUiState()

    // State when a query is being processed (Global Loading).
    // Carries old dataState for a smooth Loading + Data UX.
    data class QueryLoading(
        val data: DataState,
        val status: StatusState
    ) : SearchUiState()

    // State when the query or filter results have been fetched/displayed.
    data class Result(
        val data: DataState,
        val status: StatusState
    ) : SearchUiState()

    // State when a fatal error occurs during the main process (e.g., primary API failure)
    data class ErrorFatal(
        val data: DataState,
        val status: StatusState,
        val message: String
    ) : SearchUiState()
}

// Resource: Wrapper State for individual data items (Local Loading, Error, Data)
data class Resource<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// StatusState: Global status affecting the entire UI (Input, Active Filters)
data class StatusState(
    val queryText: String = "",
    val activeFilters: FilterState = FilterState(),
    val isGlobalLoading: Boolean = false, // Loading for major transitions/refresh
    val errorMessage: String? = null
)

// DataState: All list data being loaded. Each list is wrapped in Resource<T>
data class DataState(
    // Initial Content
    val history: Resource<List<ChipFilterOption>> = Resource(emptyList()),
    val popularSearches: Resource<List<ChipFilterOption>> = Resource(emptyList()),
    val categories: Resource<List<CategoryUiModel>> = Resource(emptyList()),
    val rest : Resource<List<RestaurantUiModel>> = Resource(emptyList()),
    val menus: Resource<List<MenuUiModel>> = Resource(emptyList()),

    // Search Results
    val queryResult: Resource<List<RestaurantUiModel>> = Resource(emptyList()),
    val filterResult: Resource<List<RestaurantUiModel>> = Resource(emptyList())
)