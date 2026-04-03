package com.example.tassty.screen.home

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel

data class HomeUiState(
    val userName: String ="",
    val profileImage: String="",
    val addressName: String="",

    val allCategories: Resource<List<CategoryUiModel>> = Resource(),
    val recommendedRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val nearbyRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val todayVouchers: Resource<List<VoucherUiModel>> = Resource(),
    val recommendedMenus: Resource<List<MenuUiModel>> = Resource(),
    val suggestedMenus: Resource<List<MenuUiModel>> = Resource(),
    val collectionsResource: Resource<List<CollectionUiModel>> = Resource(),

    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val isCollectionSheetVisible: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val newCollectionName: String = ""
)

data class HomeContent(
    val categories: Resource<List<CategoryUiModel>> = Resource(),
    val nearby: Resource<List<RestaurantUiModel>> = Resource(),
    val recommended: Resource<List<RestaurantUiModel>> = Resource(),
    val vouchers: Resource<List<VoucherUiModel>> = Resource()
)

data class HomeMenuSection(
    val recommendedMenus: Resource<List<MenuUiModel>>,
    val suggestedMenus: Resource<List<MenuUiModel>>
)

data class HomeUiFlags(
    val isRefreshing: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val newCollectionName: String = "",
    val errorMessage: String? = null,
)

data class HomeInternalState(
    val isRefreshing: Boolean = false,
    val isAddCollectionSheet: Boolean = false,
    val isCollectionSheetVisible: Boolean = false,
    val newCollectionName: String = "",
    val selectedMenu: MenuUiModel? = null,
    val selectedCollectionIds: Set<String> = emptySet(),
    val errorMessage: String? = null,
)

sealed class HomeEvent{
    object OnShowCollectionSheet : HomeEvent()
    object OnDismissCollectionSheet: HomeEvent()
    data class OnFavoriteClick(val menu: MenuUiModel) : HomeEvent()
    data class OnCollectionCheckChange(val collectionId: String, val isChecked: Boolean) : HomeEvent()
    object OnSaveToCollection : HomeEvent()

    object OnShowAddCollectionSheet : HomeEvent()
    object OnDismissAddCollectionSheet : HomeEvent()
    data class OnNewCollectionNameChange(val name: String) : HomeEvent()
    object OnCreateCollection : HomeEvent()
}

sealed class HomeUiEffect {
    data class ShowSnackbar(val message: String) : HomeUiEffect()
}