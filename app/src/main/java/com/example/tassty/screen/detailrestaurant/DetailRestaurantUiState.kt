package com.example.tassty.screen.detailrestaurant


import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantDetailUiModel
import com.example.core.ui.model.RestaurantStatusResult
import com.example.tassty.model.Cart
import com.example.tassty.model.Review
import com.example.tassty.model.Voucher
import com.example.tassty.screen.search.Resource

// --- 1. UiState (PERSISTENT DATA) ---
data class DetailRestaurantUiState(
    val restaurantResource: Resource<RestaurantDetailUiModel> = Resource(isLoading = true),

    val allMenusResource: Resource<List<MenuUiModel>> = Resource(isLoading = true),
    val bestSellersResource: Resource<List<MenuUiModel>> = Resource(isLoading = true),
    val recommendedMenusResource: Resource<List<MenuUiModel>> = Resource(isLoading = true),
    val reviewsResource: Resource<List<Review>> = Resource(isLoading = true),
    val voucherResource: Resource<List<Voucher>> = Resource(isLoading = true),

    // Derived Status (only available if restaurant is success)
    val status: RestaurantStatusResult? = null,
    val todayHoursString: String = "",

    // Global UI State
    val isShowCloseModalVisible: Boolean = false,
    val isScheduleModalVisible: Boolean = false,
    val isFavoriteModalVisible: Boolean = false,
    val isFavorite: Boolean = false,

    val showSearch: Boolean = false,
    val searchQuery: String = "",
    val searchResultsResource: Resource<List<MenuUiModel>> = Resource(),

    // Bottom Bar (Checkout)
    val cartItemsResource: List<Cart> = emptyList(),

    // Modal State for item configuration
    val itemModalToConfigure: MenuUiModel? = null
){
    // 🌟 Hitung nilai turunan di dalam data class (atau ViewModel)
    val cartItemCount: Int
        get() = cartItemsResource.sumOf { it.quantity }

    val cartTotalPrice: Int
        get() = cartItemsResource.sumOf { it.price * it.quantity }
}


// --- 2. UiEvent (ONE-TIME ACTIONS) ---
sealed class DetailRestaurantEvent {
    object OnShowScheduleSheet : DetailRestaurantEvent()
    object OnDismissScheduleSheet : DetailRestaurantEvent()

    object OnDismissCloseSheet : DetailRestaurantEvent()
    object OnShowSearchSheet : DetailRestaurantEvent()
    object OnDismissSearchSheet : DetailRestaurantEvent()
    data class OnSearchQueryChange(val newQuery: String) : DetailRestaurantEvent()

    object OnRestaurantFavoriteSheet : DetailRestaurantEvent()
    object OnRestaurantDismissFavoriteSheet : DetailRestaurantEvent()

    data class OnAllMenuFavorite(val menuId: String) : DetailRestaurantEvent()
    data class OnRecommendedFavorite(val menuId: String) : DetailRestaurantEvent()
    data class OnBestSellerFavorite(val menuId: String) : DetailRestaurantEvent()

    data class OnAddToCart(val menu: MenuUiModel) : DetailRestaurantEvent()
}