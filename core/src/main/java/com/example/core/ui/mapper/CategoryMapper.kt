package com.example.core.ui.mapper

import com.example.core.domain.model.Category
import com.example.core.ui.model.CategoryUiModel

fun Category.toUiModel() : CategoryUiModel {
    return CategoryUiModel(
        category = this
    )
}