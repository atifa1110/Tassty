package com.example.core.ui.model

import com.example.core.domain.model.CollectionListItem

data class CollectionUiModel (
    val collection : CollectionListItem,
    val isSelected: Boolean = false
)