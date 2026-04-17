package com.example.tassty.screen.collection


import com.example.core.ui.model.CollectionUiModel
import kotlinx.collections.immutable.ImmutableList

data class CollectionUiState (
    val collections: ImmutableList<CollectionUiModel>? = null
)

data class CollectionInternalState (
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String =""
)

sealed class CollectionEvent{
    object OnShowAddCollectionSheet : CollectionEvent()
    object OnDismissAddCollectionSheet : CollectionEvent()
    data class OnNewCollectionNameChange(val name: String) : CollectionEvent()
    object OnCreateCollection : CollectionEvent()
}