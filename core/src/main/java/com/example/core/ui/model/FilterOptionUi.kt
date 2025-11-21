package com.example.core.ui.model

data class FilterOptionUi(
    val key: String,
    val label: String,
    val iconRes: Int? = null,
    val isSelected: Boolean = false
)
