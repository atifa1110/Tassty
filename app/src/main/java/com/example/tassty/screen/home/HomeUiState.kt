package com.example.tassty.screen.home

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.screen.detailrestaurant.DetailRestaurantEvent
import kotlinx.collections.immutable.ImmutableList

data class HomeUiState(
    val userName: String ="",
    val profileImage: String="",
    val addressName: String="",

    val allCategories: Resource<ImmutableList<CategoryUiModel>> = Resource(),
    val recommendedRestaurants: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val nearbyRestaurants: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val todayVouchers: Resource<ImmutableList<VoucherUiModel>> = Resource(),
    val recommendedMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val suggestedMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val collections: ImmutableList<CollectionUiModel>? = null,

    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val isCollectionSheetVisible: Boolean = false,
    val isDetailMenuModalVisible: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String = "",
    val quantity: Int = 0,
    val isTokenExpired: Boolean = false
)

data class HomeContent(
    val categories: Resource<ImmutableList<CategoryUiModel>> = Resource(),
    val nearby: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val recommended: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val vouchers: Resource<ImmutableList<VoucherUiModel>> = Resource()
)

data class HomeMenuSection(
    val recommendedMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
    val suggestedMenus: Resource<ImmutableList<MenuUiModel>> = Resource(),
)

data class MenuInteractionData(
    val sections: HomeMenuSection,
    val collections: Resource<ImmutableList<CollectionUiModel>>
)

data class HomeUiFlags(
    val isRefreshingToken: Boolean = false,
    val isRefreshing: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val isDetailMenuModalVisible: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val newCollectionName: String = "",
    val errorMessage: String? = null,
    val quantity: Int,
)

data class HomeInternalState(
    val isRefreshing: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val isDetailMenuModalVisible: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val newCollectionName: String = "",
    val menuForDetail: MenuUiModel? = null,
    val menuForCollection: MenuUiModel? = null,
    val selectedCollectionIds: Set<String> = emptySet(),
    val errorMessage: String? = null,
    val quantity: Int = 1,
    val isEdit: Boolean = false
)

sealed class HomeEvent{
    data class OnFavoriteClick(val menu: MenuUiModel) : HomeEvent()
    object OnShowCollectionSheet : HomeEvent()
    object OnDismissCollectionSheet: HomeEvent()
    data class OnCollectionCheckChange(val collectionId: String, val isChecked: Boolean) : HomeEvent()
    object OnSaveToCollection : HomeEvent()

    object OnShowAddCollectionSheet : HomeEvent()
    object OnDismissAddCollectionSheet : HomeEvent()
    data class OnNewCollectionNameChange(val name: String) : HomeEvent()
    object OnCreateCollection : HomeEvent()

    data class OnShowDetailMenu(val id: String) : HomeEvent()
    data object OnRefreshToken: HomeEvent()
}

sealed class HomeEffect {
    data class ShowSnackbar(val message: String) : HomeEffect()
    data class NavigateToDetailMenu(val id: String): HomeEffect()
}