package com.example.tassty.screen.bestseller

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel
import kotlinx.collections.immutable.ImmutableList

data class BestSellerUiState (
    val menus : Resource<ImmutableList<MenuUiModel>> = Resource(),
    val query: String = "",
    val imageUrl: String = "https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format",
    val totalItems: Int = 0,
    val totalPrice: Int = 0,
    val isAddCollectionSheet: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val collections : ImmutableList<CollectionUiModel>? = null,
    val selectedMenu: MenuUiModel? = null,
    val newCollectionName: String = ""
)

data class BestSellerInternalState (
    val query: String = "",
    val selectedMenu: MenuUiModel? = null,
    val selectedCollectionIds: Set<String> = emptySet(),
    val isAddCollectionSheet: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val newCollectionName: String = ""
)

sealed class BestSellerUiEffect {
    data class ShowMessage(val message: String) : BestSellerUiEffect()
}
