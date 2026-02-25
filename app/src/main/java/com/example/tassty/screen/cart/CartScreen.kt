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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.DiscountType
import com.example.tassty.R
import com.example.tassty.cartUiModel
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CartDeliveryLocationContent
import com.example.tassty.component.CartDoubleCheckContent
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
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500

@Composable
fun CartRoute(
    onNavigateToDetail:(String) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
){
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    CartScreen(
        uiState = state.value,
        onSelectLocationClicked = { viewModel.onEvent(CartUiEvent.OnShowLocationSheet) },
        onSelectPromoClicked = { viewModel.onEvent(CartUiEvent.OnShowVoucherSheet) },
        onCartSelectionChange = { id -> viewModel.onEvent(CartUiEvent.OnCartSelectionChange(id))},
        onSelectAllClicked = { viewModel.onEvent(CartUiEvent.OnSelectAllClicked) },
        onIncrementQuantity = {id ->  viewModel.onEvent(CartUiEvent.OnIncrementQuantity(id)) },
        onDecrementQuantity = { id -> viewModel.onEvent(CartUiEvent.OnDecrementQuantity(id)) },
        onDeleteAllClicked = { viewModel.onEvent(CartUiEvent.OnDeleteAllClicked)},
        onRemoveItemClicked = { viewModel.onEvent(CartUiEvent.OnShowRemoveItemSheet(it))},
        onContinuePayment = {viewModel.onEvent(CartUiEvent.OnShowDoubleCheckSheet)},
        onNavigateToDetail = onNavigateToDetail,
    )

    CustomBottomSheet(
        visible = state.value.isLocationSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissLocationSheet) }
    ) {
        CartDeliveryLocationContent(
            resource = state.value.availableAddresses,
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
            resource = state.value.availableVouchers,
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

    CustomBottomSheet(
        visible = state.value.isDoubleCheckSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissDoubleCheckSheet) }
    ) {
        CartDoubleCheckContent(
            onRecheck = {viewModel.onEvent(CartUiEvent.OnDismissDoubleCheckSheet)},
            onContinueToPayment = {}
        )
    }
}

@Composable
fun CartScreen(
    uiState: CartUiState,
    onSelectLocationClicked: () -> Unit,
    onSelectPromoClicked: () -> Unit,
    onCartSelectionChange: (String) -> Unit,
    onIncrementQuantity: (String) -> Unit,
    onDecrementQuantity: (String) -> Unit,
    onSelectAllClicked: (Boolean) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onContinuePayment: () -> Unit,
) {
    val cart = uiState.carts.data
    val menus = cart?.menus

    Scaffold(
        containerColor = Neutral10,
        topBar = {
            CartTopAppBar(onDeleteClick = onDeleteAllClicked)
        },
        bottomBar = {
            if (!menus.isNullOrEmpty() && uiState.selectedAddress != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    ButtonComponent(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isCheckoutButtonEnabled,
                        labelResId = R.string.continue_payment,
                        onClick = onContinuePayment
                    )
                }
            }
        }
    ) { padding ->
        when {
            cart == null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Neutral10)
                )
            }

            // STATE 2 — empty cart
            menus.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Neutral10),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyCartContent()
                }
            }

            // ✅ STATE 3 — ada data
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Neutral10)
                ) {

                    item {
                        Spacer(Modifier.height(24.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = cart.restaurant.name,
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
                                    city = cart.restaurant.locationDetail.city,
                                    rating = cart.restaurant.rating,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    text = "Add more+",
                                    textColor = Orange500,
                                    onClick = { onNavigateToDetail(cart.restaurant.id) }
                                )
                            }
                        }
                        Divider32()
                    }

                    cartVerticalListBlock(
                        cart = menus,
                        headerText = "Menus",
                        selectAll = uiState.isSelectAll,
                        onSelectAllClicked = onSelectAllClicked,
                        onCartSelectionChange = onCartSelectionChange,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                        onRemoveItemClicked = onRemoveItemClicked,
                        onRevealChange = { _, _ -> }
                    )

                    item {
                        Divider32()
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            HeaderListTitleButton(
                                title = "Delivery location",
                                titleColor = Neutral100,
                                textButton = "Change location",
                                onClick = {
                                    if (uiState.selectedAddress != null) {
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
                                isPercentageDiscount =
                                    uiState.selectedVoucher?.discountType == DiscountType.PERCENTAGE,
                                totalPrice = uiState.subtotal,
                                deliveryFee = uiState.deliveryFee,
                                voucherDiscount = uiState.voucherDiscount,
                                totalOrder = uiState.totalOrder
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    CartScreen(
        uiState = CartUiState(
            carts = Resource(cartUiModel)
        ),
        onSelectLocationClicked = {},
        onSelectPromoClicked = {},
        onCartSelectionChange = {},
        onSelectAllClicked = {},
        onIncrementQuantity = {},
        onDecrementQuantity = {},
        onDeleteAllClicked = {},
        onRemoveItemClicked = {},
        onNavigateToDetail = {},
        onContinuePayment = {}
    )
}