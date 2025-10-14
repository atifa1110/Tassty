package com.example.tassty.screen.category

import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.model.FilterState

sealed class CategoryUiState {
    // Loading State
    object Loading : CategoryUiState()

    // Success State: Carries the data and the current filter status
    data class Success(
        val restaurants: List<RestaurantUiModel>,
        val totalCount: Int,
        // The property you mentioned
        val activeFilters: FilterState
    ) : CategoryUiState()

    // Error State
    data class Error(
        val message: String,
        // Can also carry the filters if you want the UI to show the error but keep the filters visible
        val activeFilters: FilterState
    ) : CategoryUiState()
}