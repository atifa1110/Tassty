package com.example.tassty.screen.collection

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel

data class CollectionUiState (
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String ="",
    val collections: Resource<List<CollectionUiModel>> = Resource(isLoading = false)
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