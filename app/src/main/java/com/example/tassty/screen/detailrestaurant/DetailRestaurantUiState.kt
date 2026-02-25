package com.example.tassty.screen.detailrestaurant

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.ReviewUiModel
import com.example.core.ui.model.VoucherUiModel

data class DetailRestaurantUiState(
    val restaurantResource: Resource<DetailRestaurantUiModel> = Resource(),
    val allMenusResource: Resource<List<MenuUiModel>> = Resource(),
    val recommendedMenusResource: Resource<List<MenuUiModel>> = Resource(),
    val bestSellerMenusResource: Resource<List<MenuUiModel>> = Resource(),
    val reviewsResource: Resource<List<ReviewUiModel>> = Resource(),
    val vouchersResource: Resource<List<VoucherUiModel>> = Resource(),
    val collectionsResource: Resource<List<CollectionUiModel>> = Resource(),
    val searchResultsResource: Resource<List<MenuUiModel>> = Resource(),

    val isCollectionSheetVisible: Boolean = false,
    val isFavoriteModalVisible: Boolean = false,
    val isScheduleModalVisible: Boolean = false,
    val isShowCloseModalVisible: Boolean = false,
    val isSearchModalVisible: Boolean = false,
    val isDetailMenuModalVisible: Boolean = false,

    val menu: MenuUiModel? = null,
    val searchQuery: String = "",
    val quantity: Int = 1,
    val isEditMode: Boolean = false,
    val totalItems : Int = 0,
    val totalPrice: Int = 0
)

data class DetailInternalState(
    val selectedCollectionIds: Set<String> = emptySet(),
    val selectedMenu: MenuUiModel? = null,
    val isCollectionSheetVisible: Boolean = false,
    val isFavoriteModalVisible: Boolean = false,
    val isScheduleModalVisible: Boolean = false,
    val isShowCloseModalVisible: Boolean = false,
    val isSearchModalVisible: Boolean = false,
    val isDetailMenuModalVisible: Boolean = false,
    val searchQuery: String = "",
    val quantity: Int = 1,
    val isEditMode: Boolean = false
)


data class DetailListContent(
    val allMenus: Resource<List<MenuUiModel>> = Resource(),
    val recommendedMenus: Resource<List<MenuUiModel>> = Resource(),
    val bestSellerMenus: Resource<List<MenuUiModel>> = Resource(),
    val reviews: Resource<List<ReviewUiModel>> = Resource(),
    val vouchers: Resource<List<VoucherUiModel>> = Resource(),
)

sealed class DetailRestaurantEvent {
    object OnShowScheduleSheet : DetailRestaurantEvent()
    object OnDismissScheduleSheet : DetailRestaurantEvent()

    object OnDismissCloseSheet : DetailRestaurantEvent()
    object OnShowSearchSheet : DetailRestaurantEvent()
    object OnDismissSearchSheet : DetailRestaurantEvent()
    data class OnSearchQueryChange(val newQuery: String) : DetailRestaurantEvent()

    object OnRestaurantFavoriteSheet : DetailRestaurantEvent()
    object OnRestaurantDismissFavoriteSheet : DetailRestaurantEvent()

    object OnShowCollectionSheet : DetailRestaurantEvent()
    object OnDismissCollectionSheet: DetailRestaurantEvent()

    data class OnCollectionCheckChange(val collectionId: String, val isChecked: Boolean) : DetailRestaurantEvent()
    object OnSaveToCollection : DetailRestaurantEvent()

    object OnShowAddCollectionSheet : DetailRestaurantEvent()
    object OnDismissAddCollectionSheet : DetailRestaurantEvent()
    data class OnMenuFavoriteClick(val menu: MenuUiModel) : DetailRestaurantEvent()

    data class OnMenuAddToCartClick(val menu: MenuUiModel): DetailRestaurantEvent()
    object OnDismissDetailMenuSheet: DetailRestaurantEvent()
    data class OnAddToCart(val menu: DetailMenuUiModel) : DetailRestaurantEvent()
    data class OnQuantityIncrease(val quantity: Int) : DetailRestaurantEvent()
    data class OnQuantityDecrease(val quantity: Int) : DetailRestaurantEvent()
}