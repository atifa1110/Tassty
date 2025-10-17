package com.example.tassty.screen.cart

import androidx.lifecycle.ViewModel
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.VoucherType
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.addresses
import com.example.tassty.carts
import com.example.tassty.filterVouchersByRestaurant
import com.example.tassty.model.Cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min
import kotlin.math.roundToInt

class CartViewModel : ViewModel() {
    // Initializes the MutableStateFlow with initial dummy data
    private val _state = MutableStateFlow(CartState(
        carts = carts,
        availableAddresses = addresses,
        availableVouchers = filterVouchersByRestaurant("1")
    ))

    // Exposes the immutable StateFlow for the UI to observe.
    val state: StateFlow<CartState> = _state

    // Called once when the ViewModel is created to set initial totals.
    init {
        calculateTotals()
    }

    /**
     * Main function to process all incoming UI events (user interactions).
     * It routes the event to the appropriate handler function.
     */
    fun onEvent(event: CartUiEvent) {
        when(event) {
            // Logic Cart
            is CartUiEvent.OnCartSelectionChange -> handleCartSelection(event.cart)
            is CartUiEvent.OnSelectAllClicked -> handleSelectAll()
            is CartUiEvent.OnDeleteAllClicked -> handleDeleteAll()

            // Logic Increment & Decrement
            is CartUiEvent.OnDecrementQuantity -> handleDecrementQuantity(event.cart)
            is CartUiEvent.OnIncrementQuantity -> handleIncrementQuantity(event.cart)

            // Logic Remove Item
            is CartUiEvent.OnShowRemoveItemSheet -> handleShowRemoveSheet(event.cart)
            is CartUiEvent.OnDismissRemoveItemSheet -> _state.update { it.copy(isRemoveItemSheetVisible = false, cartItemToRemove = null) }
            is CartUiEvent.OnRemoveCartItem -> handleRemoveItem(event.cart)

            // Logic Location (Open/Close/Set from sheet)
            is CartUiEvent.OnShowLocationSheet-> _state.update { it.copy(isLocationSheetVisible = true) }
            is CartUiEvent.OnDismissLocationSheet -> _state.update { it.copy(isLocationSheetVisible = false) }
            is CartUiEvent.OnAddressSelectionChanged -> handleAddressSelectionChanged(event.addressId)
            is CartUiEvent.OnSetLocationClicked -> handleSetLocationClicked()

            // Logic Voucher (Open/Close/Set from sheet)
            is CartUiEvent.OnShowVoucherSheet -> _state.update { it.copy(isVoucherSheetVisible = true) }
            is CartUiEvent.OnDismissVoucherSheet -> _state.update { it.copy(isVoucherSheetVisible = false) }
            is CartUiEvent.OnApplyVoucherClicked -> handleApplyVoucherClicked()
            is CartUiEvent.OnVoucherSelectionChanged -> handleVoucherSelectionChanged(event.voucherId)

            // Logic Checkout
            is CartUiEvent.OnCheckoutClicked -> {/* TODO: Implement checkout logic */ }
            is CartUiEvent.OnAddMoreClicked -> {/* TODO: Navigate to menu */ }
        }
    }

    // Stores the specific Cart item into state and sets the flag to display the removal confirmation sheet.
    private fun handleShowRemoveSheet(cart: Cart) {
        _state.update {
            it.copy(
                isRemoveItemSheetVisible = true,
                cartItemToRemove = cart
            )
        }
    }

    // Removes a specific item from the cart list and updates the global 'Select All' status.
    private fun handleRemoveItem(cart: Cart) {
        _state.update { currentState ->
            // Filters the cart list, removing the item by ID.
            val newCarts = currentState.carts.filter { it.id != cart.id }

            // Updates 'isSelectAll': false if empty, otherwise checks if all remaining are checked.
            val newIsSelectAll = if (newCarts.isEmpty()) false else newCarts.all { it.isChecked }

            currentState.copy(
                carts = newCarts,
                isSelectAll = newIsSelectAll,
                isRemoveItemSheetVisible = false,
                cartItemToRemove = null
            )
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Updates the 'isSelected' status of addresses within the modal (radio button behavior).
    private fun handleAddressSelectionChanged(selectedId: String) {
        _state.update { currentState ->
            val updatedAddresses = currentState.availableAddresses.map { address ->
                // Sets the clicked address as selected and deselects all others.
                address.copy(isSelected = address.id == selectedId)
            }
            currentState.copy(availableAddresses = updatedAddresses)
        }
    }

    // Finalizes the address selection, setting the chosen address to 'selectedAddress' and dismissing the modal.
    private fun handleSetLocationClicked() {
        _state.update { currentState ->
            // Finds the address that was selected within the modal.
            val finalAddress = currentState.availableAddresses.firstOrNull { it.isSelected }

            // Updates the final state and closes the sheet.
            currentState.copy(
                selectedAddress = finalAddress,
                isLocationSheetVisible = false
            )
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Toggles the 'isChecked' status of a single cart item.
    private fun handleCartSelection(cart: Cart) {
        _state.update { currentState ->
            val updatedCarts = currentState.carts.map { c ->
                if (c.id == cart.id) c.copy(isChecked = !c.isChecked) else c
            }
            // Updates 'isSelectAll' based on the new list state.
            val newIsSelectAll = updatedCarts.all { it.isChecked }
            currentState.copy(carts = updatedCarts, isSelectAll = newIsSelectAll)
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Toggles the global 'Select All' status for all items in the cart.
    private fun handleSelectAll() {
        _state.update { currentState ->
            val selectAll = !currentState.isSelectAll
            // Applies the new 'selectAll' status to every item.
            val updatedCarts = currentState.carts.map { it.copy(isChecked = selectAll) }
            currentState.copy(carts = updatedCarts, isSelectAll = selectAll)
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Clears the entire cart list and resets the 'Select All' status.
    private fun handleDeleteAll() {
        _state.update { currentState ->
            currentState.copy(
                carts = emptyList(),
                isSelectAll = false
            )
        }
        // Recalculates totals (should result in 0).
        calculateTotals()
    }

    // Increments the quantity of a specific cart item, limiting it by stock count.
    private fun handleIncrementQuantity(cart: Cart) {
        _state.update { currentState ->
            val updatedCarts = currentState.carts.map { c ->
                // Increments only if the quantity is below the stock limit.
                if (c.id == cart.id && c.quantity < c.stock) {
                    c.copy(quantity = c.quantity + 1)
                } else {
                    c
                }
            }
            currentState.copy(carts = updatedCarts)
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Decrements the quantity of a specific cart item, preventing it from dropping below 1.
    private fun handleDecrementQuantity(cart: Cart) {
        _state.update { currentState ->
            val updatedCarts = currentState.carts.map { c ->
                // Decrements only if the quantity is greater than 1.
                if (c.id == cart.id && c.quantity > 1) {
                    c.copy(quantity = c.quantity - 1)
                } else {
                    c
                }
            }
            currentState.copy(carts = updatedCarts)
        }
        // Recalculates totals.
        calculateTotals()
    }

    // Updates the 'isSelected' status of vouchers within the modal (radio button behavior).
    private fun handleVoucherSelectionChanged(selectedId: String) {
        _state.update { currentState ->
            val updatedVouchers = currentState.availableVouchers.map { voucher ->
                // Sets the clicked voucher as selected and deselects all others.
                voucher.copy(isSelected = voucher.voucher.id == selectedId)
            }
            currentState.copy(availableVouchers = updatedVouchers)
        }
    }

    // Setting the chosen voucher to 'selectedVoucher' and dismissing the modal.
    private fun handleApplyVoucherClicked() {
        _state.update { currentState ->
            // Finds the voucher that was selected within the modal.
            val finalVoucher = currentState.availableVouchers.firstOrNull { it.isSelected }

            // Updates the final state and closes the sheet.
            currentState.copy(
                selectedVoucher = finalVoucher,
                isVoucherSheetVisible = false
            )
        }
        // Recalculates totals because the discount/delivery fee has potentially changed.
        calculateTotals()
    }

    private fun calculateDiscountAmount(subtotal: Int, voucher: VoucherUiModel): Int {
        // 1. Check Minimum Order Requirement
        if (subtotal < voucher.voucher.minOrderValue) {
            return 0
        }

        val rawDiscount = when (voucher.voucher.discountType) {
            DiscountType.PERCENTAGE -> {
                // Calculate percentage and round it to the nearest integer
                (subtotal * (voucher.voucher.discountValue / 100.0)).roundToInt()
            }
            DiscountType.FIXED -> {
                // For fixed, the discount is the value itself
                voucher.voucher.discountValue
            }
        }

        // 2. Apply Maximum Discount Cap
        return min(rawDiscount, voucher.voucher.maxDiscount)
    }

    //Core logic: Recalculates all totals, discounts, and delivery fees based on the current state (selected items, voucher, etc.).
    private fun calculateTotals() {
        _state.update { currentState ->
            // Only consider checked items for calculations.
            val selectedItems = currentState.carts.filter { it.isChecked }
            val subtotal = selectedItems.sumOf { it.price * it.quantity }
            val voucher = currentState.selectedVoucher

            // Default values
            var deliveryFee = 0
            var itemDiscount = 0
            var shippingDiscount = 0
            var cashbackValue = 0 // Cashback doesn't affect the payable total

            // --- A. Process Voucher (if valid and subtotal > 0) ---
            if (subtotal > 0 && voucher != null) {
                // 1. Calculate the raw value the voucher is worth
                deliveryFee = 20000
                val calculatedValue = calculateDiscountAmount(subtotal, voucher)

                when (voucher.voucher.type) {
                    VoucherType.DISCOUNT -> {
                        // Applies the discount directly to the item subtotal
                        itemDiscount = calculatedValue
                    }
                    VoucherType.SHIPPING -> {
                        // Applies the discount to the delivery fee
                        shippingDiscount = calculatedValue
                        // The new delivery fee is the base fee minus the discount,
                    }
                    VoucherType.CASHBACK -> {
                        // Records the value for UI display/point accumulation.
                        // Does NOT affect the price the user pays now.
                        cashbackValue = calculatedValue
                    }
                }
            }

            val totalDiscount = itemDiscount + shippingDiscount
            val totalOrder: Int = subtotal + deliveryFee - totalDiscount

            // Enabled only if items are selected & address is chosen.
            val isButtonEnabled = subtotal > 0 && currentState.selectedAddress != null

            currentState.copy(
                subtotal = subtotal,
                deliveryFee = deliveryFee,
                voucherDiscount = totalDiscount,
                totalOrder = totalOrder,
                isCheckoutButtonEnabled = isButtonEnabled
            )
        }
    }
}