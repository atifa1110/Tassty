package com.example.tassty.screen.detailrestaurant

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantLocationArgs
import com.example.core.ui.model.ReviewUiModel
import com.example.core.ui.model.VoucherUiModel
import kotlinx.collections.immutable.ImmutableList

data class DetailRestaurantUiState(
    val restaurantResource: Resource<DetailRestaurantUiModel> = Resource(),
    val allMenusResource: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val recommendedMenusResource: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val bestSellerMenusResource: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val reviewsResource: Resource<ImmutableList<ReviewUiModel>> = Resource(),
    val vouchersResource: Resource<ImmutableList<VoucherUiModel>> = Resource(),
    val collectionsResource: ImmutableList<CollectionUiModel>? = null,
    val searchResultsResource: Resource<ImmutableList<MenuUiModel>> = Resource(),

    val isCollectionSheetVisible: Boolean = false,
    val isAddCollectionSheetVisible: Boolean = false,
    val isFavoriteModalVisible: Boolean = false,
    val isScheduleModalVisible: Boolean = false,
    val isShowCloseModalVisible: Boolean = false,
    val isSearchModalVisible: Boolean = false,

    val menu: MenuUiModel? = null,
    val searchQuery: String = "",
    val totalItems : Int = 0,
    val totalPrice: Int = 0,
    val newCollectionName: String = ""
)

data class DetailInternalState(
    val selectedCollectionIds: Set<String> = emptySet(),
    val selectedMenu: MenuUiModel? = null,
    val isCollectionSheetVisible: Boolean = false,
    val isAddCollectionSheetVisible: Boolean = false,
    val isFavoriteModalVisible: Boolean = false,
    val isScheduleModalVisible: Boolean = false,
    val isShowCloseModalVisible: Boolean = false,
    val isSearchModalVisible: Boolean = false,
    val searchQuery: String = "",
    val newCollectionName: String = ""
)

data class DetailListContent(
    val allMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val recommendedMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val bestSellerMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val reviews: Resource<ImmutableList<ReviewUiModel>> = Resource(),
    val vouchers: Resource<ImmutableList<VoucherUiModel>> = Resource(),
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
    data class OnNewCollectionNameChange(val name: String) : DetailRestaurantEvent()
    data object OnCreateCollection: DetailRestaurantEvent()

    data class OnMenuFavoriteClick(val menu: MenuUiModel) : DetailRestaurantEvent()
}

sealed interface DetailUiEvent {
    data class ShowSnackbar(val message: String) : DetailUiEvent
    data class NavigateToLocation(val data: RestaurantLocationArgs) : DetailUiEvent
}
