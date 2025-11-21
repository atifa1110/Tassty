package com.example.tassty.screen.recommended

import com.example.core.data.model.Resource
import com.example.core.ui.model.CategoryUiModel

data class RecommendedUiState(
    val allCategories: Resource<List<CategoryUiModel>> = Resource(),
)


