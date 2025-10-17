package com.example.tassty.screen.setupcuisine

import com.example.core.ui.model.CategoryUiModel

data class SetupCuisineUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryUiModel> = emptyList(),
    val selectedCategoryIds: List<String> = emptyList(),
    val currentSearchQuery: String = "",
    val filteredCategories: List<CategoryUiModel> = emptyList(),
    val errorMessage: String? = null
) {
    val canProceed: Boolean
        get() = selectedCategoryIds.isNotEmpty()
}
