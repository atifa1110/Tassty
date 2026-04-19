package com.example.tassty.screen.setupcuisine

import com.example.core.ui.model.CategoryUiModel

data class SetupCuisineUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryUiModel> = emptyList(),
    val selectedCategoryIds: List<String> = emptyList(),
    val currentSearchQuery: String = "",
    val errorMessage: String? = null,
    val isEmptyResult : Boolean = false
) {
    val filteredCategories: List<CategoryUiModel>
        get() = if (currentSearchQuery.isBlank()) {
            categories
        } else {
            categories.filter { it.name.contains(currentSearchQuery, ignoreCase = true) }
        }
}

sealed class SetupCuisineEvent {
    data class ShowError(val message: String) : SetupCuisineEvent()
    data class NavigateToSetUpLocation(val cuisines : List<String>) : SetupCuisineEvent()
}
