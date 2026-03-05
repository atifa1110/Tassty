package com.example.tassty.screen.detailmenu

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailMenuUiModel

data class DetailMenuInternalState(
    val quantity: Int = 1,
    val notesValue: String = "",
    val selectedOptionIds: Set<String> = emptySet(),
    val isCollectionSheetVisible: Boolean = false,
    val isSuccessSheetVisible: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String = "",
    val selectedCollectionIds: Set<String> = emptySet(),
    val savedCollectionName: String = "",
    val isEditMode: Boolean = false
)

// Public: Nampan yang disajikan ke Compose
data class DetailMenuUiState(
    val detail: Resource<DetailMenuUiModel> = Resource(isLoading = false),
    val collections: Resource<List<CollectionUiModel>> = Resource(isLoading = false),
    val quantity: Int = 1,
    val notesValue: String = "",
    val cartTotalPrice: Int = 0,
    val isEditMode: Boolean = false,
    val addToCartButtonText: String = "Add to Cart",
    val isCollectionSheetVisible: Boolean = false,
    val isSuccessSheetVisible: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String = "",
    val savedCollectionName: String = ""
)

sealed class DetailMenuEvent {
    data class OnQuantityIncrease(val quantity: Int) : DetailMenuEvent()
    data class OnQuantityDecrease(val quantity: Int) : DetailMenuEvent()
    data class OnNotesChange(val notes: String) : DetailMenuEvent()

    data class OnOptionToggle(val groupId: String, val optionId: String) : DetailMenuEvent()

    object OnAddToCartClick : DetailMenuEvent()

    object OnShowCollectionSheet : DetailMenuEvent()
    object OnDismissCollectionSheet: DetailMenuEvent()
    data class OnCollectionCheckChange(val collectionId: String, val isChecked: Boolean) : DetailMenuEvent()
    object OnSaveCollectionClick : DetailMenuEvent()
    object OnDismissSuccessSheet : DetailMenuEvent()

    object OnShowAddCollectionSheet : DetailMenuEvent()
    object OnDismissAddCollectionSheet : DetailMenuEvent()
    data class OnNewCollectionNameChange(val name: String) : DetailMenuEvent()
    object OnCreateCollection : DetailMenuEvent()
}

sealed interface UiEvent {
    data class NavigateBackWithResult(val id: String,val message: String): UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
}
