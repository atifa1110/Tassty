package com.example.core.ui.mapper

import com.example.core.domain.model.CollectionListItem
import com.example.core.ui.model.CollectionUiModel

fun CollectionListItem.toUiModel(isSelected: Boolean) : CollectionUiModel {
    return CollectionUiModel(
        collection = this,
        isSelected = isSelected
    )
}