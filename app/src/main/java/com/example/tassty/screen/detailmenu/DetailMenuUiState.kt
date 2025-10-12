package com.example.tassty.screen.detailmenu

import com.example.tassty.model.CollectionUiItem
import com.example.tassty.model.Menu
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.model.RestaurantStatus


data class DetailMenuUiState(
    val menu: Menu, // Data menu utama
    val restaurantStatus: RestaurantStatus = RestaurantStatus.OPEN,
    val menuChoiceSections: List<MenuChoiceSection>, // Daftar pilihan yang dinamis
    val notesValue: String = "", // Catatan dari pengguna
    val quantity: Int = 1, // Kuantitas produk

    val cartTotalPrice: Int = 0,// finalPricePerItem * quantity

    val isCollectionSheetVisible: Boolean = false,
    val collections: List<CollectionUiItem> = emptyList(),

    val isSuccessSheetVisible: Boolean = false,
    val savedCollectionName: String = "",

    val isAddCollectionSheet : Boolean = false
)

sealed class DetailMenuEvent {
    data class OnQuantityIncrease(val quantity: Int) : DetailMenuEvent()
    data class OnQuantityDecrease(val quantity: Int) : DetailMenuEvent()
    data class OnNotesChange(val notes: String) : DetailMenuEvent()

    data class OnOptionToggle(
        val section: MenuChoiceSection,
        val option: MenuItemOption
    ) : DetailMenuEvent()

    object OnAddToCartClick : DetailMenuEvent()

    object OnShowCollectionSheet : DetailMenuEvent()
    object OnDismissCollectionSheet: DetailMenuEvent()
    data class OnCollectionSelected(val collectionId: String) : DetailMenuEvent()
    object OnSaveCollectionClick : DetailMenuEvent()
    object OnDismissSuccessSheet : DetailMenuEvent()

    object OnShowAddCollectionSheet : DetailMenuEvent()
    object OnDismissAddCollectionSheet : DetailMenuEvent()
}