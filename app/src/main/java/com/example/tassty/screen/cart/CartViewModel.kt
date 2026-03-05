package com.example.tassty.screen.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateOrderUseCase
import com.example.core.domain.usecase.GetCartsUseCase
import com.example.core.domain.usecase.GetRestaurantVouchersUseCase
import com.example.core.domain.usecase.GetUserAddressUseCase
import com.example.core.domain.usecase.RemoveAllCartMenuUseCase
import com.example.core.domain.usecase.RemoveCartMenuUseCase
import com.example.core.domain.usecase.UpdateCartHiddenUseCase
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import com.example.core.domain.utils.mapToResource
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.domain.utils.toListState
import com.example.core.ui.mapper.toRequest
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.calculateCartSummary
import com.example.tassty.screen.detailcollection.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class CartViewModel @Inject  constructor(
    private val getCartsUseCase: GetCartsUseCase,
    private val removeCartMenuUseCase: RemoveCartMenuUseCase,
    private val removeAllCartMenuUseCase: RemoveAllCartMenuUseCase,
    private val getRestaurantVouchersUseCase: GetRestaurantVouchersUseCase,
    private val getUserAddressUseCase: GetUserAddressUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val updateCartHiddenUseCase: UpdateCartHiddenUseCase
): ViewModel(){

    private val _internalState = MutableStateFlow(CartInternalState())

    private val cartFlow = getCartsUseCase().map { it.mapToResource { cart-> cart.toUiModel() } }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val vouchersFlow = cartFlow
        .mapNotNull { it.data?.restaurant?.id }
        .distinctUntilChanged()
        .flatMapLatest { restaurantId ->
            getRestaurantVouchersUseCase(restaurantId)
                .map { it.toListState { v -> v.toUiModel() } }
        }

    private val addressFlow = getUserAddressUseCase().map {
        it.toListState { address-> address.toUiModel() }
    }

    private val _uiEvent = Channel<CartEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<CartUiState> = combine(
        _internalState,
        cartFlow,
        vouchersFlow,
        addressFlow
    ) { internal,cartRes, voucherRes, addressRes ->
        val cart = cartRes.data
        val updatedMenus = cart?.menus?.map { menu ->
            menu.copy(isSelected = internal.selectedCartIds.contains(menu.cartId))
        } ?: emptyList()

        val updatedCartData = cartRes.data?.copy(menus = updatedMenus)

        val updatedAddresses = addressRes.data?.map { address ->
            address.copy(isSelected = address.id == internal.selectedAddressId)
        } ?: emptyList()

        val updatedVouchers = voucherRes.data?.map { voucher ->
            voucher.copy(isSelected = voucher.id == internal.selectedVoucherId)
        } ?: emptyList()

        val isSelectAll = updatedMenus.isNotEmpty() && updatedMenus.all { it.isSelected }
        val currentAddress = updatedAddresses.find { it.isSelected }
        val currentVoucher = updatedVouchers.find { it.isSelected }

        val summary = calculateCartSummary(
            menus = updatedMenus,
            deliveryCost = cart?.restaurant?.deliveryCost ?: 0,
            voucher = currentVoucher
        )

        CartUiState(
            carts = cartRes.copy(data = updatedCartData),
            availableVouchers = voucherRes.copy(data = updatedVouchers),
            availableAddresses = addressRes.copy(data = updatedAddresses),
            subtotal = summary.subtotal,
            deliveryFee = summary.deliveryFee,
            voucherDiscount = summary.discount,
            totalOrder = summary.totalOrder,
            isSelectAll = isSelectAll,
            selectedAddress = currentAddress,
            selectedVoucher = currentVoucher,
            isLocationSheetVisible = internal.isLocationSheetVisible,
            isVoucherSheetVisible = internal.isVoucherSheetVisible,
            isRemoveItemSheetVisible = internal.isRemoveItemSheetVisible,
            isDoubleCheckSheetVisible = internal.isDoubleCheckSheetVisible,
            cartItemToRemove = internal.cartItemToRemove,
            isCheckoutButtonEnabled = summary.subtotal > 0 && currentAddress != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CartUiState()
    )

    /**
     * Main function to process all incoming UI events (user interactions).
     * It routes the event to the appropriate handler function.
     */
    fun onEvent(event: CartUiEvent) {
        when(event) {
            // Logic Cart
            is CartUiEvent.OnCartSelectionChange -> handleCartSelection(event.cartId)
            is CartUiEvent.OnSelectAllClicked -> handleSelectAll()
            is CartUiEvent.OnDeleteAllClicked -> handleDeleteAll()

            // Logic Increment & Decrement
            is CartUiEvent.OnDecrementQuantity -> handleDecrementQuantity(event.cartId)
            is CartUiEvent.OnIncrementQuantity -> handleIncrementQuantity(event.cartId)

            // Logic Remove Item
            is CartUiEvent.OnShowRemoveItemSheet -> handleShowRemoveSheet(event.cartId)
            is CartUiEvent.OnDismissRemoveItemSheet -> _internalState.update { it.copy(isRemoveItemSheetVisible = false, cartItemToRemove = null) }
            is CartUiEvent.OnRemoveCartItem -> handleRemoveItem(event.cartId)

            // Logic Location (Open/Close/Set from sheet)
            is CartUiEvent.OnShowLocationSheet-> _internalState.update { it.copy(isLocationSheetVisible = true) }
            is CartUiEvent.OnDismissLocationSheet -> _internalState.update { it.copy(isLocationSheetVisible = false) }
            is CartUiEvent.OnAddressSelectionChanged -> handleAddressSelectionChanged(event.addressId)
            is CartUiEvent.OnSetLocationClicked -> handleSetLocationClicked()

            // Logic Voucher (Open/Close/Set from sheet)
            is CartUiEvent.OnShowVoucherSheet -> _internalState.update { it.copy(isVoucherSheetVisible = true) }
            is CartUiEvent.OnDismissVoucherSheet -> _internalState.update { it.copy(isVoucherSheetVisible = false) }
            is CartUiEvent.OnApplyVoucherClicked -> handleApplyVoucherClicked()
            is CartUiEvent.OnVoucherSelectionChanged -> handleVoucherSelectionChanged(event.voucherId)

            // Logic Checkout
            is CartUiEvent.OnCheckoutClicked -> handleCheckOutClicked()
            is CartUiEvent.OnDismissDoubleCheckSheet -> _internalState.update { it.copy(isDoubleCheckSheetVisible = false) }
            is CartUiEvent.OnShowDoubleCheckSheet -> _internalState.update { it.copy(isDoubleCheckSheetVisible = true) }
        }
    }

    // Toggles the 'isChecked' status of a single cart item.
    private fun handleCartSelection(cartId: String) {
        _internalState.update { current ->
            val newSelected = if (current.selectedCartIds.contains(cartId)) {
                current.selectedCartIds - cartId
            } else {
                current.selectedCartIds + cartId
            }
            current.copy(selectedCartIds = newSelected)
        }
    }

    // Toggles the global 'Select All' status for all items in the cart.
    private fun handleSelectAll() {
        val allIds = uiState.value.carts.data?.menus?.map { it.cartId }?.toSet() ?: emptySet()
        _internalState.update { current ->
            val shouldSelectAll = !uiState.value.isSelectAll
            current.copy(
                selectedCartIds = if (shouldSelectAll) allIds else emptySet()
            )
        }
    }

    // Stores the specific Cart item into state and sets the flag to display the removal confirmation sheet.
    private fun handleShowRemoveSheet(cartId: String) {
        val cartItem = uiState.value.carts.data?.menus?.find { it.cartId == cartId }
        _internalState.update { it.copy(isRemoveItemSheetVisible = true, cartItemToRemove = cartItem) }
    }

    // Removes a specific item from the cart list and updates the global 'Select All' status.
    private fun handleRemoveItem(cartId: String) {
        viewModelScope.launch {
            removeCartMenuUseCase(cartId)
            _internalState.update { it.copy(isRemoveItemSheetVisible = false, cartItemToRemove = null) }
        }
    }

    private fun handleDeleteAll() {
        viewModelScope.launch {
            removeAllCartMenuUseCase("")
            _internalState.update { it.copy(selectedCartIds = emptySet()) }
        }
    }

    // Updates the 'isSelected' status of addresses within the modal (radio button behavior).
    private fun handleAddressSelectionChanged(selectedId: String) {
        _internalState.update { currentState ->
            currentState.copy(selectedAddressId = selectedId)
        }
    }

    // Finalizes the address selection, setting the chosen address to 'selectedAddress' and dismissing the modal.
    private fun handleSetLocationClicked() {
        _internalState.update { currentState ->
            currentState.copy(
                isLocationSheetVisible = false
            )
        }
    }

    // Increments the quantity of a specific cart item, limiting it by stock count.
    private fun handleIncrementQuantity(cartId: String) {
        viewModelScope.launch {
            updateCartQuantityUseCase(cartId, isIncrement = true)
        }
    }

    // Decrements the quantity of a specific cart item, preventing it from dropping below 1.
    private fun handleDecrementQuantity(cartId: String) {
        viewModelScope.launch {
            updateCartQuantityUseCase(cartId, isIncrement = false)
        }
    }

    // Updates the 'isSelected' status of vouchers within the modal (radio button behavior).
    private fun handleVoucherSelectionChanged(selectedId: String) {
        _internalState.update { currentState ->
            currentState.copy(selectedVoucherId = selectedId)
        }
    }

    // Setting the chosen voucher to 'selectedVoucher' and dismissing the modal.
    private fun handleApplyVoucherClicked() {
        _internalState.update { currentState ->
            currentState.copy(
                isVoucherSheetVisible = false
            )
        }
    }

    private fun handleCheckOutClicked() {
        val state = uiState.value
        val cart = state.carts.data ?: return
        val selected = cart.menus.filter { it.isSelected }
        val addressId = state.selectedAddress?.id?: return

        if (selected.isEmpty()) {
            viewModelScope.launch {
                _uiEvent.send(CartEvent.ShowError("Tolong pilih menu terlebih dahulu"))
            }
            return
        }

        viewModelScope.launch {
            createOrderUseCase(
                restaurantId = cart.restaurant.id,
                addressId = addressId,
                totalPrice = state.subtotal,
                deliveryFee = state.deliveryFee,
                discount = state.voucherDiscount,
                totalOrder = state.totalOrder,
                items = selected.map { it.toRequest() }
            ).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(checkout = Resource(isLoading = false)) }
                        _uiEvent.send(CartEvent.ShowError(result.meta.message))
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(checkout = Resource(isLoading = true)) }
                    }
                    is TasstyResponse.Success -> {
                        val selectedIds = selected.map { it.cartId }
                        updateCartHiddenUseCase(selectedIds, isHidden = true)

                        _internalState.update {
                            it.copy(
                                checkout = Resource(isLoading = false),
                                isDoubleCheckSheetVisible = false,
                                selectedCartIds = it.selectedCartIds - selectedIds.toSet()
                            )
                        }
                        Log.d("CartViewModel",result.data.toString())
                        _uiEvent.send(CartEvent.CheckoutSuccess(id=result.data.toString(), total = state.totalOrder.toCleanRupiahFormat()))
                    }
                }
            }

        }
    }

}
