package com.example.tassty.screen.setupcuisine

import com.example.tassty.model.Category

data class SetupCuisineUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val selectedCategoryIds: List<Int> = emptyList(),
    val currentSearchQuery: String = "",
    val filteredCategories: List<Category> = emptyList(),
    val errorMessage: String? = null
) {
    val canProceed: Boolean
        get() = selectedCategoryIds.isNotEmpty()
}
