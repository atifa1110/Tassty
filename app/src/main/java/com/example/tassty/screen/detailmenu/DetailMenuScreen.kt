package com.example.tassty.screen.detailmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.MenuStatus
import com.example.core.ui.model.OptionGroupUiModel
import com.example.tassty.R
import com.example.tassty.component.CartAddButton
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CollectionSaveContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DashedDivider
import com.example.tassty.component.DetailMenuTopAppBar
import com.example.tassty.component.DetailNotesSection
import com.example.tassty.component.Divider32
import com.example.tassty.component.FoodPriceBigText
import com.example.tassty.component.FoodPriceLineText
import com.example.tassty.component.MenuStockStatus
import com.example.tassty.component.OptionCard
import com.example.tassty.component.QuantityTextButton
import com.example.tassty.component.RestaurantMenuInfoCard
import com.example.tassty.component.StatusItemImage
import com.example.tassty.component.TextSection
import com.example.tassty.getPickMenuSubtitle
import com.example.tassty.menuDetailItem
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun DetailMenuScreen(
    onNavigateBack:()-> Unit,
    onAddCartSuccess:(String,String) -> Unit,
    viewModel: DetailMenuViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { event ->
            when(event){
                is UiEvent.ShowSnackbar -> {
                    snackHostState.showSnackbar(event.message)
                }

                is UiEvent.NavigateBackWithResult -> {
                    onAddCartSuccess(event.id,event.message)
                    onNavigateBack()
                }
            }
        }
    }

    DetailMenuContent(
        uiState = uiState,
        snackHostState = snackHostState,
        onOptionToggle = { group,option ->
            viewModel.onEvent(DetailMenuEvent.OnOptionToggle(group,option))},
        onIncrementQuantity = {viewModel.onEvent(DetailMenuEvent.OnQuantityIncrease(it))},
        onDecreaseQuantity = {viewModel.onEvent(DetailMenuEvent.OnQuantityDecrease(it))},
        onNotesChange = {viewModel.onEvent(DetailMenuEvent.OnNotesChange(it))},
        onShowCollectionSheet = {viewModel.onEvent(DetailMenuEvent.OnShowCollectionSheet)},
        onAddToCartClick = {viewModel.onEvent(DetailMenuEvent.OnAddToCartClick)},
        onNavigateBack = onNavigateBack
    )

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onEvent(DetailMenuEvent.OnDismissCollectionSheet)}
    ) {
        CollectionContent(
            resource = uiState.collections,
            onCollectionSelected = { id, isCheck -> viewModel.onEvent(DetailMenuEvent.OnCollectionCheckChange(id,isCheck))},
            onSaveCollectionClick = { viewModel.onEvent(DetailMenuEvent.OnSaveCollectionClick)},
            onAddCollectionClick = { viewModel.onEvent(DetailMenuEvent.OnShowAddCollectionSheet)}
        )
    }

    val successTitle = if (uiState.savedCollectionName.isEmpty()) {
        "Successfully updated your collections!"
    } else {
        "Saved to “${uiState.savedCollectionName}” Collection!"
    }
    CustomBottomSheet(
        visible = uiState.isSuccessSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionSaveContent(
            title = successTitle,
            subtitle = "Your menu collection preference has been updated.",
            onCheckCollection = {viewModel.onEvent(DetailMenuEvent.OnShowCollectionSheet)},
            onConfirmClick = { viewModel.onEvent(DetailMenuEvent.OnDismissSuccessSheet)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = uiState.newCollectionName,
            onValueName = {viewModel.onEvent(DetailMenuEvent.OnNewCollectionNameChange(it))},
            onDismissClick = { viewModel.onEvent(DetailMenuEvent.OnDismissAddCollectionSheet) },
            onAddCollection = {viewModel.onEvent(DetailMenuEvent.OnCreateCollection)}
        )
    }
}

@Composable
fun DetailMenuContent(
    uiState: DetailMenuUiState,
    snackHostState: SnackbarHostState,
    onOptionToggle:(String, String) -> Unit,
    onDecreaseQuantity: (Int) -> Unit,
    onIncrementQuantity: (Int) -> Unit,
    onNotesChange:(String) -> Unit,
    onShowCollectionSheet: () -> Unit,
    onAddToCartClick: () -> Unit,
    onNavigateBack:()-> Unit,
) {
    Scaffold(
        containerColor = Neutral10,
        snackbarHost = { SnackbarHost(snackHostState) },
        bottomBar = {
            uiState.detail.data?.let { menu ->
                if(menu.menuStatus == MenuStatus.AVAILABLE) {
                    ProductAddToCartBottomBar(
                        buttonText = uiState.addToCartButtonText,
                        quantity = uiState.quantity,
                        totalPrice = uiState.cartTotalPrice,
                        onIncreaseQuantity = { onIncrementQuantity(uiState.quantity) },
                        onDecreaseQuantity = { onDecreaseQuantity(uiState.quantity) },
                        onAddToCartClick = onAddToCartClick
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            uiState.detail.data?.let { menu ->
                item {
                    Box(Modifier.height(370.dp).fillMaxWidth()) {
                        StatusItemImage(
                            imageUrl = menu.imageUrl,
                            name = menu.name,
                            status = menu.menuStatus,
                            modifier = Modifier.fillMaxSize()
                        )
                        DetailMenuTopAppBar(
                            isFavorite = menu.isWishlist,
                            onBackClick = onNavigateBack,
                            onShareClick = {},
                            onFavoriteClick = onShowCollectionSheet
                        )

                        if (menu.menuStatus != MenuStatus.AVAILABLE) {
                            Box(
                                modifier = Modifier.padding(horizontal = 24.dp)
                                    .align(Alignment.BottomCenter)
                                    .offset(y = 20.dp)
                            ) {
                                MenuStockStatus()
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = if (menu.menuStatus == MenuStatus.AVAILABLE) 24.dp else 44.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = menu.name,
                                    style = LocalCustomTypography.current.h3Bold,
                                    color = Neutral100
                                )
                                Text(
                                    text = menu.description,
                                    style = LocalCustomTypography.current.bodyMediumRegular,
                                    color = Neutral70
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center
                            ) {
                                FoodPriceBigText(
                                    price = menu.formatPriceDiscount,
                                    color = Orange500
                                )

                                if (menu.promo) {
                                    FoodPriceLineText(
                                        price = menu.formatPrice,
                                        color = Neutral60
                                    )
                                }
                            }
                        }
                        RestaurantMenuInfoCard(
                            rating = menu.restaurant.formatRating,
                            review = menu.restaurant.formatReviewCount,
                            deliveryCost = menu.formatDeliveryCost,
                            deliveryTime = menu.restaurant.deliveryTime,
                            onReviewsClick = {}
                        )
                    }
                }

                item {
                    Divider32()
                }

                itemsIndexed(menu.optionGroups) { index, group ->
                    ChoiceSection(group = group, onOptionToggle = onOptionToggle)
                    if (index < menu.optionGroups.lastIndex) {
                        Divider32()
                    }
                }

                item {
                    Divider32()
                }

                item {
                    DetailNotesSection(
                        text = uiState.notesValue,
                        onTextChanged = onNotesChange,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChoiceSection(
    group: OptionGroupUiModel,
    onOptionToggle:(String, String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = buildAnnotatedString {
                    append(group.title)
                    if (group.required) {
                        append(" ")
                        withStyle(
                            style = SpanStyle(color = Orange500)
                        ) {
                            append("*")
                        }
                    }
                },
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )

            Text(
                text = getPickMenuSubtitle(group.required,group.maxPick),
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Neutral70
            )
        }

        Spacer(Modifier.height(8.dp))

        // List of options
        group.options.forEachIndexed { index, option ->
            OptionCard(
                maxPick = group.maxPick,
                enabled = option.isAvailable,
                option = option,
                onClick = {
                    onOptionToggle(group.id,option.id)
                }
            )

            if (index < group.options.size-1) {
                DashedDivider(
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProductAddToCartBottomBar(
    buttonText: String,
    quantity : Int,
    totalPrice : Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxWidth().background(Neutral10)
            .padding(start= 24.dp, end = 24.dp,top = 24.dp, bottom = 36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        QuantityTextButton(
            quantity = quantity,
            onIncreaseQuantity = onIncreaseQuantity,
            onDecreaseQuantity = onDecreaseQuantity
        )
        CartAddButton(
            buttonText = buttonText,
            totalPrice = totalPrice,
            onClick = onAddToCartClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuPreview() {
    val snackHostState = remember { SnackbarHostState() }
    DetailMenuContent(
        uiState = DetailMenuUiState(
            isEditMode = true,
            addToCartButtonText = "Update Order",
            detail = Resource(data=menuDetailItem)
        ),
        snackHostState = snackHostState,
        onOptionToggle = {_,_->},
        onIncrementQuantity = {},
        onDecreaseQuantity = {},
        onNotesChange = {},
        onAddToCartClick = {},
        onShowCollectionSheet = {},
        onNavigateBack = {}
    )
}
