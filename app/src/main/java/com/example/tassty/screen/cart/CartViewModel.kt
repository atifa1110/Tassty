package com.example.tassty.screen.cart

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
import com.example.core.domain.usecase.UpdateCartNotesUseCase
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.mapper.toRequest
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.core.utils.toImmutableListState
import com.example.tassty.CartSummary
import com.example.tassty.calculateCartSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    private val _voucherState = MutableStateFlow<Resource<ImmutableList<VoucherUiModel>>>(Resource())
    val voucherState: StateFlow<Resource<ImmutableList<VoucherUiModel>>> = _voucherState

    private val _addressState = MutableStateFlow<Resource<ImmutableList<UserAddressUiModel>>>(Resource())
    val addressState: StateFlow<Resource<ImmutableList<UserAddressUiModel>>> = _addressState

    private val _uiEffect = Channel<CartUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val cartFlow = getCartsUseCase().map { cart -> cart.toUiModel() }

    private val selectedMenusFlow = combine(
        cartFlow,
        _internalState.map { it.selectedCartIds }.distinctUntilChanged(),
        _internalState.map { it.revealedCartIds }.distinctUntilChanged()
    ) { cartRes, selectedIds, revealedIds ->
        cartRes.menus.withUiState(selectedIds,revealedIds)
    }

    private val selectedVoucherFlow = combine(
        voucherState,
        _internalState.map { it.selectedVoucherId }.distinctUntilChanged()
    ) { voucherRes, selectedId ->
        voucherRes.data?.find { it.id == selectedId }
    }

    private val selectedAddressFlow = combine(
        addressState,
        _internalState.map { it.selectedAddressId }.distinctUntilChanged()
    ) { addressRes, selectedId ->
        addressRes.data?.find { it.id == selectedId }
    }

    private val summaryInputFlow = selectedMenusFlow.map { menus ->
            menus.filter { it.isSelected }
        }
        .distinctUntilChanged { old, new ->
            old.map { it.cartId to it.quantity } == new.map { it.cartId to it.quantity }
        }

    private val summaryFlow = combine(
        summaryInputFlow,
        cartFlow.map { it.restaurant.deliveryCost }.distinctUntilChanged(),
        selectedVoucherFlow
    ) { selectedMenus, deliveryCost, selectedVoucher ->
        calculateCartSummary(
            selectedMenus = selectedMenus,
            deliveryCost = deliveryCost,
            voucher = selectedVoucher
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartSummary()
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

    val selectionFlow = combine(
        selectedMenusFlow,
        selectedVoucherFlow,
        selectedAddressFlow
    ) { menus, vouchers, addresses ->
        Triple(menus, vouchers, addresses)
    }

    val uiState: StateFlow<CartUiState> = combine(
            cartFlow,
            selectionFlow,
            summaryFlow,
            uiFlagsFlow
        ) { carts, selection, summary, internal ->
            val (menus, selectedVoucher, selectedAddress) = selection

            CartUiState(
                carts = carts.copy(menus = menus),
                subtotal = summary.subtotal,
                deliveryFee = summary.deliveryFee,
                voucherDiscount = summary.discount,
                totalOrder = summary.totalOrder,
                isSelectAll = menus.isNotEmpty() && menus.all { it.isSelected },
                selectedAddress = selectedAddress,
                selectedVoucher = selectedVoucher,
                isLocationSheetVisible = internal.isLocationSheetVisible,
                isVoucherSheetVisible = internal.isVoucherSheetVisible,
                isRemoveItemSheetVisible = internal.isRemoveItemSheetVisible,
                isDoubleCheckSheetVisible = internal.isDoubleCheckSheetVisible,
                isDeleteAllSheetVisible = internal.isDeleteAllSheetVisible,
                selectedCart = internal.selectedCart,
                isNoteSheetVisible = internal.isNoteSheetVisible,
                note = internal.note,
                isCheckoutButtonEnabled = summary.subtotal > 0 && selectedAddress != null
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
            is CartUiEvent.OnShowLocationSheet-> {
                _internalState.update { it.copy(isLocationSheetVisible = true) }
                loadAddresses()
            }

            is CartUiEvent.OnDismissLocationSheet -> _internalState.update { it.copy(isLocationSheetVisible = false, selectedAddressId = null) }
            is CartUiEvent.OnAddressSelectionChanged -> handleAddressSelectionChanged(event.addressId)
            is CartUiEvent.OnSetLocationClicked -> handleSetLocationClicked()

            // Voucher (Open/Close/Set from sheet)
            is CartUiEvent.OnShowVoucherSheet -> {
                _internalState.update { it.copy(isVoucherSheetVisible = true) }
                loadVouchers()
            }

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

    private fun loadVouchers() {
        val restaurantId = uiState.value.carts?.restaurant?.id ?: ""
        viewModelScope.launch {
            if(restaurantId.isEmpty()){
                _uiEffect.send(CartUiEffect.ShowError("Restaurant Id is not exist"))
            }else{
                getRestaurantVouchersUseCase(id = restaurantId).collect { result ->
                    _voucherState.update { currentState ->
                        val newUiModels = result.toImmutableListState { it.toUiModel() }

                        if(result is TasstyResponse.Loading && currentState.data!=null){
                            newUiModels.copy(data = currentState.data)
                        }else{
                            newUiModels
                        }
                    }
                }
            }
        }
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            getUserAddressUseCase().collect { result ->
                _addressState.update { currentState ->
                    val newUiModels = result.toImmutableListState { it.toUiModel() }

                    if (result is TasstyResponse.Loading && currentState.data != null) {
                        newUiModels.copy(data = currentState.data)
                    } else {
                        newUiModels
                    }
                }
            }
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
        val allIds = uiState.value.carts?.menus?.map { it.cartId }?.toSet() ?: emptySet()
        _internalState.update { current ->
            val shouldSelectAll = !uiState.value.isSelectAll
            current.copy(
                selectedCartIds = if (shouldSelectAll) allIds else emptySet()
            )
        }
    }

    // Stores the specific Cart item into state and sets the flag to display the removal confirmation sheet.
    private fun handleShowRemoveSheet(cartId: String) {
        val cartItem = uiState.value.carts?.menus?.find { it.cartId == cartId }
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
        val carts = uiState.value.carts?: return
        viewModelScope.launch {
            if(carts.menus.isEmpty()){
                _uiEffect.send(CartUiEffect.ShowError("There is no menu to delete"))
            }else {
                _internalState.update { it.copy(isDeleteAllSheetVisible = true) }
            }
        }
    }

    private fun handleDeleteAll() {
        val id = uiState.value.carts?.restaurant?.id?:""
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
        viewModelScope.launch {
            val selectedMenus = selectedMenusFlow.first().filter { it.isSelected }
            val selectedAddress = selectedAddressFlow.first()
            val selectedVoucher = selectedVoucherFlow.first()
            val cart = cartFlow.first()
            val summary = summaryFlow.first()

            if (selectedMenus.isEmpty()) {
                _uiEffect.send(CartUiEffect.ShowError("Please select the menu first"))
                return@launch
            }

            if (selectedAddress == null) {
                _uiEffect.send(CartUiEffect.ShowError("Please select an address"))
                return@launch
            }


            createOrderUseCase(
                restaurantId = cart.restaurant.id,
                voucherId = selectedVoucher?.id ?:"",
                addressId = selectedAddress.id,
                totalPrice = summary.subtotal,
                deliveryFee = summary.deliveryFee,
                discount = summary.discount,
                totalOrder = summary.totalOrder,
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
                            total = summary.totalOrder.toCleanRupiahFormat())
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
        val currentCartItem = uiState.value.carts?.menus?.find { it.cartId == cartId }?: return@launch
        if(currentCartItem.customizable){
            _uiEffect.send(CartUiEffect.NavigateDetailMenu(currentCartItem.menuId))
        }else {
            _internalState.update {
                it.copy(
                    isNoteSheetVisible = true,
                    note = currentCartItem.notes,
                    selectedCart = currentCartItem
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


fun List<CartItemUiModel>.withUiState(
    selectedIds: Set<String>,
    revealedIds: Set<String>
): List<CartItemUiModel> {
    return map { item ->
        item.copy(
            isSelected = item.cartId in selectedIds,
            isSwipeActionVisible = item.cartId in revealedIds
        )
    }
}
