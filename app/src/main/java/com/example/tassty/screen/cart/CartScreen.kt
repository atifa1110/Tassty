package com.example.tassty.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.carts
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CartDeliveryLocationContent
import com.example.tassty.component.CartPromoContent
import com.example.tassty.component.CartRemoveMenuContent
import com.example.tassty.component.CartTopAppBar
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCartContent
import com.example.tassty.component.FoodRatingAndCityRow
import com.example.tassty.component.HeaderListTitleButton
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.component.SelectLocationCard
import com.example.tassty.component.SelectPaymentCard
import com.example.tassty.component.TextButton
import com.example.tassty.component.cartVerticalListBlock
import com.example.tassty.model.Cart
import com.example.tassty.model.DiscountType
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500

@Composable
fun CartScreenRoute(
    viewModel: CartViewModel = viewModel()
){

    val state = viewModel.state.collectAsStateWithLifecycle()

    CartScreen(
        uiState = state.value,
        onSelectLocationClicked = { viewModel.onEvent(CartUiEvent.OnShowLocationSheet) },
        onSelectPromoClicked = { viewModel.onEvent(CartUiEvent.OnShowVoucherSheet) },
        onCartSelectionChange = { cart -> viewModel.onEvent(CartUiEvent.OnCartSelectionChange(cart))},
        onSelectAllClicked = { viewModel.onEvent(CartUiEvent.OnSelectAllClicked) },
        onIncrementQuantity = { cart ->  viewModel.onEvent(CartUiEvent.OnIncrementQuantity(cart)) },
        onDecrementQuantity = { cart -> viewModel.onEvent(CartUiEvent.OnDecrementQuantity(cart)) },
        onDeleteAllClicked = { viewModel.onEvent(CartUiEvent.OnDeleteAllClicked)},
        onRemoveItemClicked = { viewModel.onEvent(CartUiEvent.OnShowRemoveItemSheet(it))}
    )

    CustomBottomSheet(
        visible = state.value.isLocationSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissLocationSheet) }
    ) {
        CartDeliveryLocationContent(
            address = state.value.availableAddresses,
            onAddressChange = { it -> viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(it)) },
            onSetLocationClicked = { viewModel.onEvent(CartUiEvent.OnSetLocationClicked)},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissLocationSheet) }
        )
    }

    CustomBottomSheet(
        visible = state.value.isVoucherSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissVoucherSheet) }
    ) {
        CartPromoContent(
            voucher = state.value.availableVouchers,
            onVoucherSelectionChanged = { viewModel.onEvent(CartUiEvent.OnVoucherSelectionChanged(it))},
            onApplyVoucherClicked = { viewModel.onEvent(CartUiEvent.OnApplyVoucherClicked)},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissVoucherSheet) }
        )
    }

    CustomBottomSheet(
        visible = state.value.isRemoveItemSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissRemoveItemSheet) }
    ) {
        CartRemoveMenuContent(
            cart = state.value.cartItemToRemove,
            onRemoveCartItem = { viewModel.onEvent(CartUiEvent.OnRemoveCartItem(it))},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissRemoveItemSheet) }
        )
    }
}

@Composable
fun CartScreen(
    uiState: CartState,
    onSelectLocationClicked:() -> Unit,
    onSelectPromoClicked:() -> Unit,
    onCartSelectionChange:(Cart) -> Unit,
    onIncrementQuantity:(Cart) -> Unit,
    onDecrementQuantity:(Cart) -> Unit,
    onSelectAllClicked:(Boolean) -> Unit,
    onDeleteAllClicked:() -> Unit,
    onRemoveItemClicked: (Cart) -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            CartTopAppBar(onDeleteClick = onDeleteAllClicked)
        },
        bottomBar = {
            if (uiState.carts.isNotEmpty() && uiState.selectedAddress != null) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    ButtonComponent(
                        enabled = uiState.isCheckoutButtonEnabled,
                        labelResId = R.string.continue_payment,
                    ) { }
                }
            }
        }
    ) { padding ->

        // Show empty state when list is empty
        if (uiState.carts.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Neutral10),
                contentAlignment = Alignment.Center
            ) {
                EmptyCartContent()
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Neutral10)
            ) {
                item {
                    Spacer(Modifier.height(24.dp))
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                        Text(
                            text = uiState.restaurantName,
                            style = LocalCustomTypography.current.h2Bold,
                            color = Neutral100
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FoodRatingAndCityRow(
                                city = uiState.restaurantCity,
                                rating = uiState.restaurantRating,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(text = "Add more+", textColor = Orange500, onClick = {})
                        }
                    }
                    Divider32()
                }

                cartVerticalListBlock(
                    cart = uiState.carts,
                    headerText = "Menus",
                    selectAll = uiState.isSelectAll,
                    onSelectAllClicked = onSelectAllClicked,
                    onCartSelectionChange = onCartSelectionChange,
                    onIncrementQuantity = onIncrementQuantity,
                    onDecrementQuantity = onDecrementQuantity,
                    onRemoveItemClicked =  onRemoveItemClicked,
                    onRevealChange = { index, isRevealed ->
                        //cartsDummy[index] = cartsDummy[index].copy(isSwipeActionVisible = isRevealed)
                    }
                )

                item {
                    Divider32()
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        HeaderListTitleButton(
                            title = "Delivery location",
                            titleColor = Neutral100,
                            textButton = "Change location",
                            onClick = {
                                if(uiState.selectedAddress!=null){
                                    onSelectLocationClicked()
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        SelectLocationCard(
                            address = uiState.selectedAddress,
                            onClick = onSelectLocationClicked
                        )
                    }
                }

                item {
                    Divider32()
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(
                            text = "Payment detail",
                            color = Neutral100,
                            style = LocalCustomTypography.current.h5Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        SelectPaymentCard(
                            voucher = uiState.selectedVoucher,
                            onClick = onSelectPromoClicked
                        )
                    }
                }

                item {
                    Divider32()
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        OrderSummaryCard(
                            isPercentageDiscount = uiState.selectedVoucher?.discountType == DiscountType.PERCENTAGE,
                            totalPrice = uiState.subtotal,
                            deliveryFee = uiState.deliveryFee,
                            voucherDiscount = uiState.voucherDiscount,
                            voucherDiscountPercent = uiState.selectedVoucher?.discountValue,
                            totalOrder = uiState.totalOrder
                        )
                    }
                }

                item{
                    Spacer(Modifier.height(24.dp))
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreen(
        uiState = CartState(
            carts = carts
        ),
        onSelectLocationClicked = {},
        onSelectPromoClicked = {},
        onCartSelectionChange = {},
        onSelectAllClicked = {},
        onIncrementQuantity = {},
        onDecrementQuantity = {},
        onDeleteAllClicked = {},
        onRemoveItemClicked = {}
    )
}