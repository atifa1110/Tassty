package com.example.tassty.screen.detailmenu

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.tassty.component.CartAddButton
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CollectionSaveContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DashedDivider
import com.example.tassty.component.DetailMenuTopAppBar
import com.example.tassty.component.DetailNotesSection
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodPriceBigText
import com.example.tassty.component.FoodPriceLineText
import com.example.tassty.component.MenuStockStatus
import com.example.tassty.component.OptionCard
import com.example.tassty.component.QuantityTextButton
import com.example.tassty.component.RestaurantMenuInfoCard
import com.example.tassty.component.StatusItemImage
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.menuDetailItem
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme

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

    CustomBottomSheet(
        visible = uiState.isSuccessSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionSaveContent(
            title = uiState.titleSuccess,
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
    val scrollState = rememberLazyListState()

    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 200
        }
    }

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween (300),
        label = "alpha"
    )

    val iconBackground by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.background,
        animationSpec = tween(300),
        label = "iconBackground"
    )

    val isLoading = uiState.detail.isLoading
    val errorMessage = uiState.detail.errorMessage
    val menu = uiState.detail.data

    Scaffold(
        containerColor = LocalCustomColors.current.background,
        snackbarHost = { SnackbarHost(snackHostState) },
        bottomBar = {
            if(uiState.canShowBottomBar) {
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
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (isLoading) {
                    loadingMenuSection()
                } else if(errorMessage!=null) {
                    errorSection()
                }else if(menu != null){
                    item {
                        Box(Modifier.height(370.dp).fillMaxWidth()) {
                            StatusItemImage(
                                imageUrl = menu.imageUrl,
                                name = menu.name,
                                status = menu.menuStatus,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (menu.menuStatus != MenuStatus.AVAILABLE) {
                                Box(modifier = Modifier.padding(horizontal = 24.dp)
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
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp,
                                top = if (menu.menuStatus == MenuStatus.AVAILABLE) 24.dp else 44.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            HeaderNamePrice(
                                name = menu.name,
                                description = menu.description,
                                price = menu.formatPrice,
                                priceDiscount = menu.formatPriceDiscount,
                                promo = menu.promo
                            )

                            RestaurantMenuInfoCard(
                                rating = menu.restaurant.formatRating,
                                review = menu.restaurant.formatReviewCount,
                                deliveryCost = menu.formatDeliveryCost,
                                deliveryTime = menu.restaurant.deliveryTime,
                                onReviewsClick = {}
                            )
                        }
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
                        DetailNotesSection(
                            text = uiState.notesValue,
                            onTextChanged = onNotesChange,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }

            if (menu != null) {
                DetailMenuTopAppBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(
                            Neutral10.copy(alpha = appBarAlpha)
                        )
                        .statusBarsPadding(),
                    iconBackground = iconBackground,
                    isFavorite = menu.isWishlist,
                    onBackClick = onNavigateBack,
                    onShareClick = {},
                    onFavoriteClick = onShowCollectionSheet
                )
            }
        }
    }
}

fun LazyListScope.loadingMenuSection() {
    item(key = "load_image_section") {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
                .shimmerLoadingAnimation()
        )
    }

    item(key = "load_info_section") {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.width(200.dp).height(24.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerLoadingAnimation())
            Box(modifier = Modifier.fillMaxWidth().height(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerLoadingAnimation())


            Box(modifier = Modifier.fillMaxWidth().height(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerLoadingAnimation())
        }
        Divider32()
    }

    items(2) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.width(150.dp).height(20.dp)
                .clip(RoundedCornerShape(10.dp)).shimmerLoadingAnimation())

            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(100.dp).height(16.dp)
                        .clip(RoundedCornerShape(10.dp)).shimmerLoadingAnimation())
                    Box(modifier = Modifier.size(20.dp).clip(CircleShape)
                        .clip(RoundedCornerShape(10.dp)).shimmerLoadingAnimation())
                }
            }
        }
        Divider32()
    }
}

fun LazyListScope.errorSection(){
    item {
        Box(
            modifier = Modifier.fillParentMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            ErrorScreen()
        }
    }
}
@Composable
fun HeaderNamePrice(
    name: String,
    description: String,
    priceDiscount: String,
    price: String,
    promo: Boolean
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = LocalCustomTypography.current.h3Bold,
                color = LocalCustomColors.current.headerText
            )
            Text(
                text = description,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            FoodPriceBigText(
                price = priceDiscount,
                color = Orange500
            )

            if (promo) {
                FoodPriceLineText(
                    price = price,
                    color = Neutral60
                )
            }
        }
    }
}

@Composable
fun ChoiceSection(
    group: OptionGroupUiModel,
    onOptionToggle:(String, String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
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
                color = LocalCustomColors.current.headerText
            )

            Text(
                text = group.subtitle,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text
            )
        }

        Spacer(Modifier.height(8.dp))

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
            .fillMaxWidth().background(LocalCustomColors.current.modalBackgroundFrame)
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

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun DetailMenuLightPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme {
//        DetailMenuContent(
//            uiState = DetailMenuUiState(
//                isEditMode = true,
//                addToCartButtonText = "Update Order",
//                detail = Resource(data = menuDetailItem)
//            ),
//            snackHostState = snackHostState,
//            onOptionToggle = { _, _ -> },
//            onIncrementQuantity = {},
//            onDecreaseQuantity = {},
//            onNotesChange = {},
//            onAddToCartClick = {},
//            onShowCollectionSheet = {},
//            onNavigateBack = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun DetailMenuDarkPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = true) {
//        DetailMenuContent(
//            uiState = DetailMenuUiState(
//                isEditMode = true,
//                addToCartButtonText = "Update Order",
//                detail = Resource(data = menuDetailItem)
//            ),
//            snackHostState = snackHostState,
//            onOptionToggle = { _, _ -> },
//            onIncrementQuantity = {},
//            onDecreaseQuantity = {},
//            onNotesChange = {},
//            onAddToCartClick = {},
//            onShowCollectionSheet = {},
//            onNavigateBack = {}
//        )
//    }
//}
