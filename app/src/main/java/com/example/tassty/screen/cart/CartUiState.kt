package com.example.tassty.screen.cart

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CartGroupUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.core.ui.model.UserAddressUiModel
import kotlinx.collections.immutable.ImmutableList

data class CartUiState(
    val carts: CartGroupUiModel? = null,
    val selectedCart: CartItemUiModel? = null,
    val isSelectAll: Boolean = false,

    val selectedAddress: UserAddressUiModel? = null,
    val selectedVoucher: VoucherUiModel? = null,

    val subtotal: Int = 0,
    val deliveryFee: Int = 0,
    val voucherDiscount: Int = 0,
    val totalOrder: Int = 0,

    val isLocationSheetVisible: Boolean = false,
    val isVoucherSheetVisible: Boolean = false,
    val isRemoveItemSheetVisible: Boolean = false,
    val isDoubleCheckSheetVisible: Boolean = false,
    val isNoteSheetVisible: Boolean = false,
    val isDeleteAllSheetVisible: Boolean = false,

    val isCheckoutButtonEnabled: Boolean = false,
    val note: String = "",
)

data class UiFlags(
    val isLocationSheetVisible: Boolean,
    val isVoucherSheetVisible: Boolean,
    val isRemoveItemSheetVisible: Boolean,
    val isDoubleCheckSheetVisible: Boolean,
    val isDeleteAllSheetVisible: Boolean,
    val isNoteSheetVisible: Boolean,
    val selectedCart: CartItemUiModel?,
    val note: String
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
    val isNoteSheetVisible: Boolean = false,
    val isDeleteAllSheetVisible: Boolean = false,
    val selectedCart: CartItemUiModel? = null,
    val checkout: Resource<String> = Resource(),
    val revealedCartIds: Set<String> = emptySet(),
    val note: String = ""
)

sealed class CartUiEvent {
    data class OnCartSelectionChange(val cartId: String) : CartUiEvent()
    object OnSelectAllClicked : CartUiEvent()
    object OnDeleteAll : CartUiEvent()


    data class OnIncrementQuantity(val cartId: String) : CartUiEvent()
    data class OnDecrementQuantity(val cartId: String) : CartUiEvent()

    data class OnShowRemoveItemSheet(val cartId: String) : CartUiEvent()
    object OnDismissRemoveItemSheet : CartUiEvent()
    data class OnRemoveCartItem(val cartId: String) : CartUiEvent()

    object OnShowLocationSheet : CartUiEvent()
    object OnDismissLocationSheet : CartUiEvent()
    object OnSetLocationClicked : CartUiEvent()
    data class OnAddressSelectionChanged(val addressId: String) : CartUiEvent()


    object OnShowVoucherSheet : CartUiEvent()
    object OnDismissVoucherSheet : CartUiEvent()
    object OnApplyVoucherClicked : CartUiEvent()
    data class OnVoucherSelectionChanged(val voucherId: String) : CartUiEvent()


    data class OnNoteClicked(val cartId: String) : CartUiEvent()
    object OnDismissNoteSheet : CartUiEvent()
    data class OnUpdateNoteItem(val cartId: String, val notes: String) : CartUiEvent()
    data class OnNoteTextChange(val newNote: String) : CartUiEvent()
    data class OnRevealChange(val cartItemId: String, val isRevealed: Boolean) : CartUiEvent()

    object OnCheckoutClicked : CartUiEvent()
    object OnShowDoubleCheckSheet : CartUiEvent()
    object OnDismissDoubleCheckSheet : CartUiEvent()

    object OnShowDeleteAllSheet : CartUiEvent()
    object OnDismissDeleteAllSheet : CartUiEvent()
}

sealed interface CartUiEffect {
    data class ShowError(val message: String) : CartUiEffect
    data class CheckoutSuccess(val id: String, val total: String) : CartUiEffect
    data class NavigateDetailMenu(val id: String): CartUiEffect
}