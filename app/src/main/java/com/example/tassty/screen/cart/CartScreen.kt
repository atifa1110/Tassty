package com.example.tassty.screen.cart

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.DiscountType
import com.example.core.ui.model.CartGroupUiModel
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CartDeliveryLocationContent
import com.example.tassty.component.CartDoubleCheckContent
import com.example.tassty.component.CartEditContent
import com.example.tassty.component.CartPromoContent
import com.example.tassty.component.CartRemoveMenuContent
import com.example.tassty.component.CartTopAppBar
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DeleteAllCartContent
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyCartContent
import com.example.tassty.component.FoodRatingAndCityRow
import com.example.tassty.component.HeaderListTitleButton
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.component.SelectLocationCard
import com.example.tassty.component.SelectVoucherCard
import com.example.tassty.component.cartVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.OrderPreviewData

@Composable
fun CartScreen(
    onNavigateToRecommended: () -> Unit,
    onNavigateToPayment: (String, String) -> Unit,
    onNavigateToDetailRest:(String) -> Unit,
    onNavigateToDetailMenu: (String) -> Unit,
    onNavigateToAddress: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val voucherState by viewModel.voucherState.collectAsStateWithLifecycle()
    val addressState by viewModel.addressState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event->
            when(event){
                is CartUiEffect.ShowError -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }

                is CartUiEffect.CheckoutSuccess -> {
                    onNavigateToPayment(event.id,event.total)
                }

                is CartUiEffect.NavigateDetailMenu -> {
                    onNavigateToDetailMenu(event.id)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CartContent(
            context = context,
            uiState = uiState,
            onNavigateToRecommended = onNavigateToRecommended,
            onNavigateToDetailRest = onNavigateToDetailRest,
            onSelectLocationClicked = { viewModel.onEvent(CartUiEvent.OnShowLocationSheet) },
            onSelectPromoClicked = { viewModel.onEvent(CartUiEvent.OnShowVoucherSheet) },
            onCartSelectionChange = { id -> viewModel.onEvent(CartUiEvent.OnCartSelectionChange(id)) },
            onSelectAllClicked = { viewModel.onEvent(CartUiEvent.OnSelectAllClicked) },
            onIncrementQuantity = { id -> viewModel.onEvent(CartUiEvent.OnIncrementQuantity(id)) },
            onDecrementQuantity = { id -> viewModel.onEvent(CartUiEvent.OnDecrementQuantity(id)) },
            onDeleteAllClicked = { viewModel.onEvent(CartUiEvent.OnShowDeleteAllSheet) },
            onRemoveItemClicked = { viewModel.onEvent(CartUiEvent.OnShowRemoveItemSheet(it)) },
            onContinuePayment = { viewModel.onEvent(CartUiEvent.OnShowDoubleCheckSheet) },
            onRevealChange = { id, reveal ->
                viewModel.onEvent(
                    CartUiEvent.OnRevealChange(
                        id,
                        reveal
                    )
                )
            },
            onEditNoteMenuClick = { viewModel.onEvent(CartUiEvent.OnNoteClicked(it)) }
        )
        LoadingOverlay(
            text = stringResource(R.string.load),
            isLoading = uiState.isLoading
        )
    }

    CustomBottomSheet(
        visible = uiState.isDeleteAllSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        DeleteAllCartContent(
            onDelete = {viewModel.onEvent(CartUiEvent.OnDeleteAll)},
            onDismissClick = {viewModel.onEvent(CartUiEvent.OnDismissDeleteAllSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isLocationSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissLocationSheet) }
    ) {
        CartDeliveryLocationContent(
            selectedId = uiState.selectedAddress?.id,
            resource = addressState,
            onAddressChange = { it -> viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(it)) },
            onSetLocationClicked = { viewModel.onEvent(CartUiEvent.OnSetLocationClicked)},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissLocationSheet) },
            onNavigateToAddress = onNavigateToAddress
        )
    }

    CustomBottomSheet(
        visible = uiState.isVoucherSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissVoucherSheet) }
    ) {
        CartPromoContent(
            resource = voucherState,
            selectedId = uiState.selectedVoucher?.id,
            onVoucherSelectionChanged = { viewModel.onEvent(CartUiEvent.OnVoucherSelectionChanged(it))},
            onApplyVoucherClicked = { viewModel.onEvent(CartUiEvent.OnApplyVoucherClicked)},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissVoucherSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isRemoveItemSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissRemoveItemSheet) }
    ) {
        CartRemoveMenuContent(
            cart = uiState.selectedCart,
            onRemoveCartItem = { viewModel.onEvent(CartUiEvent.OnRemoveCartItem(it))},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissRemoveItemSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isDoubleCheckSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissDoubleCheckSheet) }
    ) {
        CartDoubleCheckContent(
            onRecheck = {viewModel.onEvent(CartUiEvent.OnDismissDoubleCheckSheet)},
            onContinueToPayment = {viewModel.onEvent(CartUiEvent.OnCheckoutClicked)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isNoteSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CartEditContent(
            cartId = uiState.selectedCart?.cartId?:"",
            text = uiState.note,
            onTextChange = { viewModel.onEvent(CartUiEvent.OnNoteTextChange(it))},
            onUpdateCart = { id, notes-> viewModel.onEvent(CartUiEvent.OnUpdateNoteItem(id,notes))},
            onDismiss = { viewModel.onEvent(CartUiEvent.OnDismissNoteSheet) }
        )
    }
}

@Composable
fun CartContent(
    context: Context,
    uiState: CartUiState,
    onNavigateToRecommended: () -> Unit,
    onNavigateToDetailRest: (String) -> Unit,
    onSelectLocationClicked: () -> Unit,
    onSelectPromoClicked: () -> Unit,
    onCartSelectionChange: (String) -> Unit,
    onIncrementQuantity: (String) -> Unit,
    onDecrementQuantity: (String) -> Unit,
    onSelectAllClicked: (Boolean) -> Unit,
    onDeleteAllClicked: () -> Unit,
    onRemoveItemClicked: (String) -> Unit,
    onRevealChange:(String, Boolean) -> Unit,
    onContinuePayment: () -> Unit,
    onEditNoteMenuClick : (String) -> Unit
) {
    val cart = uiState.carts
    val menus = cart?.menus

    Scaffold(
        containerColor = LocalCustomColors.current.background,
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
                        .background(LocalCustomColors.current.background)
                )
            }

            menus.isNullOrEmpty() -> {
                Box(modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyCartContent(
                        onClick = onNavigateToRecommended
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    item(key = "header_cart") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = cart.restaurant.name,
                                style = LocalCustomTypography.current.h2Bold,
                                color = LocalCustomColors.current.headerText
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

                                Row(
                                    modifier = Modifier.clickable(onClick = { onNavigateToDetailRest(cart.restaurant.id) }),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.add_more),
                                        style = LocalCustomTypography.current.bodyMediumMedium,
                                        color = Orange500
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .align(Alignment.CenterVertically),
                                        ) {
                                        Icon(
                                            modifier = Modifier.fillMaxSize(),
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "",
                                            tint = Orange500
                                        )
                                    }
                                }
                            }
                        }
                        Divider32()
                    }

                    cartVerticalListBlock(
                        cart = menus,
                        headerText = context.getString(R.string.menus),
                        selectAll = uiState.isSelectAll,
                        onSelectAllClicked = onSelectAllClicked,
                        onCartSelectionChange = onCartSelectionChange,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                        onRemoveItemClicked = onRemoveItemClicked,
                        onRevealChange = onRevealChange,
                        onEditNoteMenuClick = onEditNoteMenuClick
                    )

                    item (key= "location_section"){
                        Divider32()
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            HeaderListTitleButton(
                                title = stringResource(R.string.delivery_location),
                                titleColor = LocalCustomColors.current.headerText,
                                textButton = stringResource(R.string.change_location),
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

                    item(key="voucher_section") {
                        Divider32()
                        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                            Text(
                                text = stringResource(R.string.payment_detail),
                                color = LocalCustomColors.current.headerText,
                                style = LocalCustomTypography.current.h5Bold
                            )
                            Spacer(Modifier.height(12.dp))
                            SelectVoucherCard(
                                voucher = uiState.selectedVoucher,
                                onClick = onSelectPromoClicked
                            )
                        }
                    }

                    item(key = "summary_section") {
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
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun CartLightPreview() {
    TasstyTheme {
        CartContent(
            uiState = CartUiState(
                carts = OrderPreviewData.cartUiModel
            ),
            onSelectLocationClicked = {},
            onSelectPromoClicked = {},
            onCartSelectionChange = {},
            onSelectAllClicked = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onDeleteAllClicked = {},
            onRemoveItemClicked = {},
            onNavigateToDetailRest = {},
            onRevealChange = { _, _ -> },
            onEditNoteMenuClick = {},
            onNavigateToRecommended = {},
            onContinuePayment = {},
            context = LocalContext.current
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun CartDarkPreview() {
    TasstyTheme(darkTheme = true) {
        CartContent(
            uiState = CartUiState(
                carts = OrderPreviewData.cartUiModel
            ),
            onSelectLocationClicked = {},
            onSelectPromoClicked = {},
            onCartSelectionChange = {},
            onSelectAllClicked = {},
            onIncrementQuantity = {},
            onDecrementQuantity = {},
            onDeleteAllClicked = {},
            onRemoveItemClicked = {},
            onNavigateToDetailRest = {},
            onRevealChange = { _, _ -> },
            onEditNoteMenuClick = {},
            onNavigateToRecommended = {},
            onContinuePayment = {},
            context = LocalContext.current
        )
    }
}