package com.example.tassty.screen.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateOrderUseCase
import com.example.core.domain.usecase.GetCartsUseCase
import com.example.core.domain.usecase.GetRestaurantVouchersUseCase
import com.example.core.domain.usecase.GetUserAddressUseCase
import com.example.core.domain.usecase.RemoveAllCartMenuUseCase
import com.example.core.domain.usecase.RemoveCartMenuUseCase
import com.example.core.domain.usecase.UpdateCartHiddenUseCase
import com.example.core.domain.usecase.UpdateCartNotesUseCase
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import com.example.core.ui.utils.mapToResource
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toRequest
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.tassty.CartSummary
import com.example.tassty.calculateCartSummary
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
    private val updateCartHiddenUseCase: UpdateCartHiddenUseCase,
    private val updateCartNotesUseCase: UpdateCartNotesUseCase
): ViewModel(){

    private val _internalState = MutableStateFlow(CartInternalState())

    private val _uiEffect = Channel<CartUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val cartFlow = getCartsUseCase().map {
        it.mapToResource { cart -> cart.toUiModel() } }
            .distinctUntilChanged()

    private val addressFlow = getUserAddressUseCase().map {
        it.toListState { address -> address.toUiModel() } }
            .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val vouchersFlow = cartFlow
        .mapNotNull { it.data?.restaurant?.id }
        .distinctUntilChanged()
        .flatMapLatest {
            restaurantId -> getRestaurantVouchersUseCase(restaurantId)
                .map { it.toListState { v -> v.toUiModel() } }
        }

    private val selectedMenusFlow = combine(
        cartFlow,
        _internalState.map { it.selectedCartIds }.distinctUntilChanged(),
        _internalState.map { it.revealedCartIds }.distinctUntilChanged()
    ) { cartRes, selectedIds, revealedIds ->
        cartRes.data?.menus?.map { item ->
            item.copy(
                isSelected = selectedIds.contains(item.cartId),
                isSwipeActionVisible = revealedIds.contains(item.cartId)
            )
        } ?: emptyList()
    }

    private val selectedVoucherFlow = combine(
        vouchersFlow,
        _internalState.map { it.selectedVoucherId }.distinctUntilChanged()
    ) { voucherRes, id ->
        voucherRes.data?.applySingleSelection(
            selectedId = id,
            getId = { it.id },
            setSelected = { item, isSelected -> item.copy(isSelected = isSelected) }
        ) ?: emptyList()
    }

    private val selectedAddressFlow = combine(
        addressFlow,
        _internalState.map { it.selectedAddressId }.distinctUntilChanged()
    ) { addressRes, id ->
        addressRes.data?.applySingleSelection(
            selectedId = id,
            getId = { it.id },
            setSelected = { item, isSelected -> item.copy(isSelected = isSelected) }
        ) ?: emptyList()
    }

    private val summaryInputFlow = combine(
        cartFlow.map { it.data?.menus ?: emptyList() }.distinctUntilChanged(),
        _internalState.map { it.selectedCartIds }.distinctUntilChanged()
    ) { allMenus, selectedIds ->
        allMenus.filter { selectedIds.contains(it.cartId) }
    }.distinctUntilChanged { old, new ->
        old.map { it.cartId to it.quantity } == new.map { it.cartId to it.quantity }
    }

    private val summaryFlow = combine(
        summaryInputFlow,
        cartFlow.map { it.data?.restaurant?.deliveryCost ?: 0 }.distinctUntilChanged(),
        selectedVoucherFlow.map { it.find { v -> v.isSelected } }.distinctUntilChanged()
    ) { selectedMenus, deliveryCost, selectedVoucher ->

        Log.d("BENCHMARK", "summaryFlow recomputed")

        calculateCartSummary(
            selectedMenus = selectedMenus,
            deliveryCost = deliveryCost,
            voucher = selectedVoucher
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
        CartSummary()
    )

    private val uiFlagsFlow = _internalState
        .map {
            UiFlags(
                isLocationSheetVisible = it.isLocationSheetVisible,
                isVoucherSheetVisible = it.isVoucherSheetVisible,
                isRemoveItemSheetVisible = it.isRemoveItemSheetVisible,
                isDoubleCheckSheetVisible = it.isDoubleCheckSheetVisible,
                isNoteSheetVisible = it.isNoteSheetVisible,
                isDeleteAllSheetVisible = it.isDeleteAllSheetVisible,
                selectedCart = it.selectedCart,
                note = it.note
            )
        }
        .distinctUntilChanged()

    private val baseUiFlow = combine(
        cartFlow,
        vouchersFlow,
        addressFlow
    ) { cartRes, voucherRes, addressRes ->
        Triple(cartRes, voucherRes, addressRes)
    }

    val selectionFlow = combine(
        selectedMenusFlow,
        selectedVoucherFlow,
        selectedAddressFlow
    ) { menus, vouchers, addresses ->
        Triple(menus, vouchers, addresses)
    }

    val uiState: StateFlow<CartUiState> =
        combine(
            baseUiFlow,
            selectionFlow,
            summaryFlow,
            uiFlagsFlow
        ) { base, selection, summary, internal ->

            val (cartRes, voucherRes, addressRes) = base
            val (menus, vouchers, addresses) = selection

            val cart = cartRes.data
            val updatedCart = cart?.copy(menus = menus)

            val currentAddress = addresses.find { it.isSelected }
            val currentVoucher = vouchers.find { it.isSelected }

            CartUiState(
                carts = cartRes.copy(data = updatedCart),
                availableVouchers = voucherRes.copy(data = vouchers),
                availableAddresses = addressRes.copy(data = addresses),

                subtotal = summary.subtotal,
                deliveryFee = summary.deliveryFee,
                voucherDiscount = summary.discount,
                totalOrder = summary.totalOrder,

                isSelectAll = menus.isNotEmpty() && menus.all { it.isSelected },
                selectedAddress = currentAddress,
                selectedVoucher = currentVoucher,
                isLocationSheetVisible = internal.isLocationSheetVisible,
                isVoucherSheetVisible = internal.isVoucherSheetVisible,
                isRemoveItemSheetVisible = internal.isRemoveItemSheetVisible,
                isDoubleCheckSheetVisible = internal.isDoubleCheckSheetVisible,
                isDeleteAllSheetVisible = internal.isDeleteAllSheetVisible,
                selectedCart = internal.selectedCart,
                isNoteSheetVisible = internal.isNoteSheetVisible,
                note = internal.note,
                isCheckoutButtonEnabled = summary.subtotal > 0 && currentAddress != null
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CartUiState()
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

            // Delete All
            is CartUiEvent.OnDismissDeleteAllSheet -> _internalState.update { it.copy(isDeleteAllSheetVisible = false) }
            is CartUiEvent.OnShowDeleteAllSheet -> handleShowDeleteAllSheet()
            is CartUiEvent.OnDeleteAll-> handleDeleteAll()

            // Increment & Decrement
            is CartUiEvent.OnDecrementQuantity -> handleDecrementQuantity(event.cartId)
            is CartUiEvent.OnIncrementQuantity -> handleIncrementQuantity(event.cartId)

            // Remove Item
            is CartUiEvent.OnShowRemoveItemSheet -> handleShowRemoveSheet(event.cartId)
            is CartUiEvent.OnDismissRemoveItemSheet -> _internalState.update { it.copy(isRemoveItemSheetVisible = false, selectedCart = null, revealedCartIds = emptySet()) }
            is CartUiEvent.OnRemoveCartItem -> handleRemoveItem(event.cartId)

            // Location (Open/Close/Set from sheet)
            is CartUiEvent.OnShowLocationSheet-> _internalState.update { it.copy(isLocationSheetVisible = true) }
            is CartUiEvent.OnDismissLocationSheet -> _internalState.update { it.copy(isLocationSheetVisible = false, selectedAddressId = null) }
            is CartUiEvent.OnAddressSelectionChanged -> handleAddressSelectionChanged(event.addressId)
            is CartUiEvent.OnSetLocationClicked -> handleSetLocationClicked()

            // Voucher (Open/Close/Set from sheet)
            is CartUiEvent.OnShowVoucherSheet -> _internalState.update { it.copy(isVoucherSheetVisible = true) }
            is CartUiEvent.OnDismissVoucherSheet -> _internalState.update { it.copy(isVoucherSheetVisible = false, selectedVoucherId = null) }
            is CartUiEvent.OnApplyVoucherClicked -> handleApplyVoucherClicked()
            is CartUiEvent.OnVoucherSelectionChanged -> handleVoucherSelectionChanged(event.voucherId)

            // Checkout
            is CartUiEvent.OnCheckoutClicked -> handleCheckOutClicked()
            is CartUiEvent.OnDismissDoubleCheckSheet -> _internalState.update { it.copy(isDoubleCheckSheetVisible = false) }
            is CartUiEvent.OnShowDoubleCheckSheet -> _internalState.update { it.copy(isDoubleCheckSheetVisible = true) }
            is CartUiEvent.OnRevealChange -> handleOnRevealChange(event.cartItemId,event.isRevealed)

            // Update Menu
            is CartUiEvent.OnDismissNoteSheet -> handleDismissEditNote()
            is CartUiEvent.OnUpdateNoteItem-> handleUpdateNotesById(event.cartId,event.notes)
            is CartUiEvent.OnNoteTextChange -> onNoteTextChanged(event.newNote)
            is CartUiEvent.OnNoteClicked -> handleClickEditNote(event.cartId)
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
        _internalState.update { it.copy(isRemoveItemSheetVisible = true, selectedCart = cartItem) }
    }

    // Removes a specific item from the cart list and updates the global 'Select All' status.
    private fun handleRemoveItem(cartId: String) {
        viewModelScope.launch {
            removeCartMenuUseCase(cartId)
            _internalState.update { it.copy(isRemoveItemSheetVisible = false, selectedCart = null, revealedCartIds = emptySet()) }
        }
    }

    private fun handleShowDeleteAllSheet() {
        val carts = uiState.value.carts.data?: return
        viewModelScope.launch {
            if(carts.menus.isEmpty()){
                _uiEffect.send(CartUiEffect.ShowError("There is no menu to delete"))
            }else {
                _internalState.update { it.copy(isDeleteAllSheetVisible = true) }
            }
        }
    }

    private fun handleDeleteAll() {
        val id = uiState.value.carts.data?.restaurant?.id?:""
        viewModelScope.launch {
            removeAllCartMenuUseCase(id)
            _internalState.update { it.copy(selectedCartIds = emptySet(), isDeleteAllSheetVisible = false) }
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
        val currentState = uiState.value
        val cartData = currentState.carts.data ?: return
        val selectedMenus = cartData.menus.filter { it.isSelected }
        val selectedAddressId = currentState.selectedAddress?.id ?: return
        val selectedVoucherId = currentState.selectedVoucher?.id

        if (selectedMenus.isEmpty()) {
            viewModelScope.launch {
                _uiEffect.send(CartUiEffect.ShowError("Please select the menu first"))
            }
            return
        }

        viewModelScope.launch {
            createOrderUseCase(
                restaurantId = cartData.restaurant.id,
                voucherId = selectedVoucherId?:"",
                addressId = selectedAddressId,
                totalPrice = currentState.subtotal,
                deliveryFee = currentState.deliveryFee,
                discount = currentState.voucherDiscount,
                totalOrder = currentState.totalOrder,
                items = selectedMenus.map { it.toRequest() }
            ).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(checkout = Resource(isLoading = false)) }
                        _uiEffect.send(CartUiEffect.ShowError(result.meta.message))
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(checkout = Resource(isLoading = true)) }
                    }
                    is TasstyResponse.Success -> {
                        val selectedIds = selectedMenus.map { it.cartId }
                        updateCartHiddenUseCase(cartIds = selectedIds, isHidden = true)

                        _internalState.update {
                            it.copy(
                                checkout = Resource(isLoading = false),
                                isDoubleCheckSheetVisible = false,
                                selectedCartIds = it.selectedCartIds - selectedIds.toSet()
                            )
                        }
                        _uiEffect.send(CartUiEffect.CheckoutSuccess(id = result.data.toString(),
                            total = currentState.totalOrder.toCleanRupiahFormat())
                        )
                    }
                }
            }
        }
    }

    private fun handleOnRevealChange(cartId: String, isRevealed: Boolean) {
        _internalState.update { state ->
            state.copy(
                revealedCartIds = if (isRevealed) {
                    setOf(cartId)
                } else {
                    emptySet()
                }
            )
        }
    }


    private fun handleClickEditNote(cartId: String) = viewModelScope.launch {
        val currentCartItem = uiState.value.carts.data?.menus?.find { it.cartId == cartId }?: return@launch
        if(currentCartItem.customizable){
            _uiEffect.send(CartUiEffect.NavigateDetailMenu(currentCartItem.menuId))
        }else {
            _internalState.update {
                it.copy(
                    isNoteSheetVisible = true,
                    note = currentCartItem.notes
                )
            }
        }
    }

    private fun handleDismissEditNote(){
        _internalState.update { it.copy(isNoteSheetVisible = false) }
    }

    private fun onNoteTextChanged(text: String) {
        _internalState.update { it.copy(note = text)}
    }

    private fun handleUpdateNotesById(cartId: String, notes: String){
        viewModelScope.launch {
            updateCartNotesUseCase(cartId, notes)
            _internalState.update { it.copy(isNoteSheetVisible = false) }
        }
    }
}

fun <T> List<T>.applySingleSelection(
    selectedId: String?,
    getId: (T) -> String,
    setSelected: (T, Boolean) -> T
): List<T> {
    return map { item ->
        setSelected(item, getId(item) == selectedId)
    }
}
