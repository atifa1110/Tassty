package com.example.tassty.screen.bestseller

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.MenuUiModel

data class BestSellerUiState (
    val menus : Resource<List<MenuUiModel>> = Resource(),
    val query: String = "",
    val totalItems: Int = 0,
    val totalPrice: Int = 0
)

data class BestSellerInternalState (
    val query: String = ""
)