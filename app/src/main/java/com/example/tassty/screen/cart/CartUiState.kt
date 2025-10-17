package com.example.tassty.screen.cart

import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.model.Cart
import com.example.tassty.model.UserAddress

data class CartState(
    // Data Keranjang & Restoran
    val carts: List<Cart> = emptyList(),
    val restaurantName: String = "Indah Cafe",
    val restaurantCity: String = "Praya",
    val restaurantRating: Double = 4.4,

    // Status Seleksi
    val isSelectAll: Boolean = false,

    // Informasi Pengiriman & Promo
    val selectedAddress: UserAddress? = null,
    val availableAddresses: List<UserAddress> = emptyList(),

    val selectedVoucher: VoucherUiModel? = null,
    val availableVouchers: List<VoucherUiModel> = emptyList(),

    val cartItemToRemove: Cart? = null,

    // Perhitungan Total
    val subtotal: Int = 0,
    val deliveryFee: Int = 0,
    val voucherDiscount: Int = 0,
    val totalOrder: Int = 0,

    // Status Visibilitas Sheets (Dikontrol oleh ViewModel)
    val isLocationSheetVisible: Boolean = false,
    val isVoucherSheetVisible: Boolean = false,
    val isRemoveItemSheetVisible: Boolean = false,

    // Status UI Lainnya
    val isLoading: Boolean = false,
    val isCheckoutButtonEnabled: Boolean = false
)

sealed class CartUiEvent {
    // Interaksi Keranjang
    data class OnCartSelectionChange(val cart: Cart) : CartUiEvent()
    object OnSelectAllClicked : CartUiEvent()
    object OnDeleteAllClicked : CartUiEvent()

    // Interaksi Kuantitas Baru
    data class OnIncrementQuantity(val cart: Cart) : CartUiEvent()
    data class OnDecrementQuantity(val cart: Cart) : CartUiEvent()

    data class OnShowRemoveItemSheet(val cart: Cart) : CartUiEvent()
    object OnDismissRemoveItemSheet : CartUiEvent()
    data class OnRemoveCartItem(val cart: Cart) : CartUiEvent()

    // Interaksi Lokasi
    object OnShowLocationSheet : CartUiEvent() // Open Sheet Location
    object OnDismissLocationSheet : CartUiEvent() // Close Sheet Location
    object OnSetLocationClicked : CartUiEvent() // Choose one of the address
    data class OnAddressSelectionChanged(val addressId: String) : CartUiEvent() // Check Address from list

    // Interaksi Voucher
    object OnShowVoucherSheet : CartUiEvent() // Buka Sheet
    object OnDismissVoucherSheet : CartUiEvent() // Tutup Sheet (tanpa menyimpan data)
    object OnApplyVoucherClicked : CartUiEvent()
    data class OnVoucherSelectionChanged(val voucherId: String) : CartUiEvent()

    // Finalisasi
    object OnCheckoutClicked : CartUiEvent()
    object OnAddMoreClicked : CartUiEvent()
}