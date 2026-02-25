package com.example.tassty.screen.cart

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CartGroupUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.core.ui.model.UserAddressUiModel

data class CartUiState(
    // Data Keranjang & Restoran
    val carts: Resource<CartGroupUiModel> = Resource(),
    val cartItemToRemove: CartItemUiModel? = null,

    // Status Seleksi
    val isSelectAll: Boolean = false,

    // Informasi Pengiriman & Promo
    val selectedAddress: UserAddressUiModel? = null,
    val availableAddresses:  Resource<List<UserAddressUiModel>> = Resource(),

    val selectedVoucher: VoucherUiModel? = null,
    val availableVouchers: Resource<List<VoucherUiModel>> = Resource(),

    // Perhitungan Total
    val subtotal: Int = 0,
    val deliveryFee: Int = 0,
    val voucherDiscount: Int = 0,
    val totalOrder: Int = 0,

    // Status Visibilitas Sheets (Dikontrol oleh ViewModel)
    val isLocationSheetVisible: Boolean = false,
    val isVoucherSheetVisible: Boolean = false,
    val isRemoveItemSheetVisible: Boolean = false,
    val isDoubleCheckSheetVisible: Boolean = false,

    // Status UI Lainnya
    val isCheckoutButtonEnabled: Boolean = false
)

data class CartInternalState(
    val selectedCartIds: Set<String> = emptySet(),
    val selectedAddressId: String? = null,
    val selectedVoucherId: String? = null,
    val isSelectAll: Boolean = false,
    val isLocationSheetVisible: Boolean = false,
    val isDoubleCheckSheetVisible: Boolean = false,
    val isVoucherSheetVisible: Boolean = false,
    val isRemoveItemSheetVisible: Boolean = false,
    val cartItemToRemove: CartItemUiModel? = null
)

sealed class CartUiEvent {
    // Interaksi Keranjang
    data class OnCartSelectionChange(val cartId: String) : CartUiEvent()
    object OnSelectAllClicked : CartUiEvent()
    object OnDeleteAllClicked : CartUiEvent()

    // Interaksi Kuantitas Baru
    data class OnIncrementQuantity(val cartId: String) : CartUiEvent()
    data class OnDecrementQuantity(val cartId: String) : CartUiEvent()

    data class OnShowRemoveItemSheet(val cartId: String) : CartUiEvent()
    object OnDismissRemoveItemSheet : CartUiEvent()
    data class OnRemoveCartItem(val cartId: String) : CartUiEvent()

    // Interaksi Lokasi
    object OnShowLocationSheet : CartUiEvent()
    object OnDismissLocationSheet : CartUiEvent()
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

    object OnShowDoubleCheckSheet : CartUiEvent()
    object OnDismissDoubleCheckSheet : CartUiEvent()
}