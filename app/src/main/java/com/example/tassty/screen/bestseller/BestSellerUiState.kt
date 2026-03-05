package com.example.tassty.screen.bestseller

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel

data class BestSellerUiState (
    val menus : Resource<List<MenuUiModel>> = Resource(),
    val query: String = "",
    val totalItems: Int = 0,
    val totalPrice: Int = 0,
    val isCollectionSheetVisible: Boolean = false,
    val collections : Resource<List<CollectionUiModel>> = Resource(),
    val menu: MenuUiModel? = null,
)

data class BestSellerInternalState (
    val query: String = "",
    val menu: MenuUiModel? = null,
    val selectedCollectionIds: Set<String> = emptySet(),
    val isCollectionSheetVisible: Boolean = false,
)

