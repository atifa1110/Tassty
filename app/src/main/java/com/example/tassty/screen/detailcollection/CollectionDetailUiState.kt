package com.example.tassty.screen.detailcollection

data class CollectionDetailUiState(
    val isEditCollection: Boolean = false,
    val isDeleteCollection: Boolean = false,
    val collectionName: String = "",
    val nameInput: String = "",
    val collectionImage: String = ""
)

sealed interface CollectionDetailEvent {
    // Event saat user ngetik nama baru
    data class OnNewCollectionNameChange(val newName: String) : CollectionDetailEvent

    // Event saat user klik "Update" di Bottom Sheet
    object OnUpdateCollection : CollectionDetailEvent

    // Event saat user mau buka sheet edit
    object OnEditSheetClick : CollectionDetailEvent

    // Event saat user membatalkan/tutup sheet
    object OnDismissAddCollectionSheet : CollectionDetailEvent

    // Event saat user mau buka sheet delete
    object OnDeleteSheetClick : CollectionDetailEvent

    // Event saat user membatalkan/tutup sheet
    object OnDismissDeleteCollectionSheet : CollectionDetailEvent

    object OnDeleteClick : CollectionDetailEvent

}

sealed interface UiEvent {
    data class NavigateBackWithResult(val message: String): UiEvent
    data class ShowError(val message: String) : UiEvent
}
