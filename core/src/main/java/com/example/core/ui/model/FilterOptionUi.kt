package com.example.core.ui.model


data class FilterOptionUi<T>(
    val key: String,
    val label: String,
    val category: T,
    val iconRes: String? = null,
    val isSelected: Boolean = false
)