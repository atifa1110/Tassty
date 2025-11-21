package com.example.tassty.screen.home

import com.example.core.data.model.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel

data class HomeUiState(
    val allCategories: Resource<List<CategoryUiModel>> = Resource(),
    val recommendedRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val nearbyRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val suggestedMenus: Resource<List<MenuUiModel>> = Resource(),
    val recommendedMenus: Resource<List<MenuUiModel>> = Resource(),
    val todayVouchers: Resource<List<VoucherUiModel>> = Resource(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,

    val isCollectionSheetVisible: Boolean = false,
    val collectionsResource: Resource<List<CollectionUiModel>> = Resource(isLoading = false),

    val isAddCollectionSheet: Boolean = false,
    val createCollectionResource: Resource<Boolean> = Resource(isLoading = false),

    val newCollectionName: String = "",
    val menuIdToSave: String? = null,
)

sealed class HomeEvent{

    data class ShowSnackbar(val message: String) : HomeEvent()
    object OnShowCollectionSheet : HomeEvent()
    object OnDismissCollectionSheet: HomeEvent()

    object OnShowAddCollectionSheet : HomeEvent()
    object OnDismissAddCollectionSheet : HomeEvent()

    data class OnFavoriteClick(val menuId: String) : HomeEvent()

    data class OnNewCollectionNameChange(val name: String) : HomeEvent()

    object OnCreateCollection : HomeEvent()

    data class OnCollectionCheckChange(val collectionId: Int, val isChecked: Boolean) : HomeEvent()

    object OnSaveToCollection : HomeEvent()
}