package com.example.tassty.screen.detailcollection

data class CollectionDetailUiState(
    val collectionName: String = "",
    val collectionImage: String = "",
    val isEditCollection: Boolean = false,
    val isDeleteCollection: Boolean = false,
    val nameInput: String = "",
)

sealed interface CollectionDetailEvent {
    data class OnNewCollectionNameChange(val newName: String) : CollectionDetailEvent
    object OnUpdateCollection : CollectionDetailEvent
    object OnEditSheetClick : CollectionDetailEvent
    object OnDismissAddCollectionSheet : CollectionDetailEvent
    object OnDeleteSheetClick : CollectionDetailEvent
    object OnDismissDeleteCollectionSheet : CollectionDetailEvent
    object OnDeleteClick : CollectionDetailEvent

}

sealed interface UiEvent {
    data class NavigateBackWithResult(val message: String): UiEvent
    data class ShowError(val message: String) : UiEvent
}
