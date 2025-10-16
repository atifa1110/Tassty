package com.example.tassty.screen.home

import com.example.core.domain.model.Menu
import com.example.core.domain.model.Voucher
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.screen.search.Resource

data class HomeUiState(
    val recommendedRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val nearbyRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val suggestedMenus: Resource<List<MenuUiModel>> = Resource(),
    val recommendedMenus: Resource<List<MenuUiModel>> = Resource(),
    val todayVouchers: Resource<List<VoucherUiModel>> = Resource(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)