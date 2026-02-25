package com.example.core.ui.model

import com.example.core.ui.mapper.FilterCategory

data class FilterOptionUi(
    val key: String,
    val label: String,
    val category: FilterCategory,
    val iconRes: String? = null,
    val isSelected: Boolean = false
)
