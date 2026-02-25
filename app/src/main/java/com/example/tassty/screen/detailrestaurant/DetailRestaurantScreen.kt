package com.example.tassty.screen.detailrestaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.ReviewUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.component.CartAddItemButton
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DetailScheduleContent
import com.example.tassty.component.DetailTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.FoodGridSoldCard
import com.example.tassty.component.GridMenuListSection
import com.example.tassty.component.HorizontalTitleButtonSection
import com.example.tassty.component.HorizontalTitleItemCountButtonSection
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.MenuAddToCartContent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.RankBadgeIcon
import com.example.tassty.component.RestaurantCloseModal
import com.example.tassty.component.RestaurantCloseStatus
import com.example.tassty.component.RestaurantInfoCard
import com.example.tassty.component.ReviewCard
import com.example.tassty.component.StatusItemImage
import com.example.tassty.component.SuccessImage
import com.example.tassty.component.VoucherCard
import com.example.tassty.component.menuItemCountVerticalListBlock
import com.example.tassty.menuDetailItem
import com.example.tassty.menusItem
import com.example.tassty.restaurantDetailItem
import com.example.tassty.reviews
import com.example.tassty.screen.detailmenu.UiEvent
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.voucherUiModel
import kotlin.collections.orEmpty

@Composable
fun DetailRestaurantScreen(
    snackHostState : SnackbarHostState,
    onNavigateToDetailMenu:(String) -> Unit = {},
    onNavigateToBestSeller:(String) -> Unit = {},
    viewModel: DetailRestaurantViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResultsUiState.collectAsStateWithLifecycle()
    val detailMenu by viewModel.detailMenuFlow.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEffect) {
        viewModel.uiEffect.collect { event ->
            when(event) {
                is UiEvent.NavigateBackWithResult -> TODO()
                is UiEvent.ShowSnackbar -> {
                    snackHostState.showSnackbar(event.message)
                }
            }
        }
    }

    DetailRestaurantContent(
        snackHostState = snackHostState,
        uiState = uiState,
        onShowSchedule = { viewModel.onEvent(DetailRestaurantEvent.OnShowScheduleSheet) },
        onRestaurantFavoriteClick = { viewModel.onEvent(DetailRestaurantEvent.OnRestaurantFavoriteSheet) },
        onMenuFavoriteClick = { viewModel.onEvent(DetailRestaurantEvent.OnMenuFavoriteClick(it))},
        onShowSearchSheet = { viewModel.onEvent(DetailRestaurantEvent.OnShowSearchSheet) },
        onMenuAddToCartClick = {viewModel.onEvent(DetailRestaurantEvent.OnMenuAddToCartClick(it))},
        onNavigateToDetailMenu = onNavigateToDetailMenu,
        onNavigateToBestSeller = onNavigateToBestSeller
    )

    CustomBottomSheet(
        visible = uiState.isScheduleModalVisible,
        onDismiss = { viewModel.onEvent(DetailRestaurantEvent.OnDismissScheduleSheet)}
    ) {
        DetailScheduleContent(
            restaurant = uiState.restaurantResource.data
        )
    }

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onEvent(DetailRestaurantEvent.OnDismissCollectionSheet) }
    ) {
        CollectionContent(
            resource = uiState.collectionsResource,
            onCollectionSelected = {id, check -> viewModel.onEvent(DetailRestaurantEvent.OnCollectionCheckChange(id,check))},
            onSaveCollectionClick = {viewModel.onEvent(DetailRestaurantEvent.OnSaveToCollection)},
            onAddCollectionClick = { viewModel.onEvent(DetailRestaurantEvent.OnShowAddCollectionSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isShowCloseModalVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        RestaurantCloseModal (onDismiss = {viewModel.onEvent(DetailRestaurantEvent.OnDismissCloseSheet)})
    }

    CustomBottomSheet(
        visible = uiState.isDetailMenuModalVisible,
        onDismiss = {viewModel.onEvent(DetailRestaurantEvent.OnDismissDetailMenuSheet)}
    ) {
        MenuAddToCartContent(
            isEditMode = uiState.isEditMode,
            quantity = uiState.quantity,
            resource = detailMenu,
            onIncreaseQuantity = {viewModel.onEvent(DetailRestaurantEvent.OnQuantityIncrease(uiState.quantity))},
            onDecreaseQuantity = {viewModel.onEvent(DetailRestaurantEvent.OnQuantityDecrease(uiState.quantity))},
            onAddToCart = {viewModel.onEvent(DetailRestaurantEvent.OnAddToCart(it))}
        )
    }

    CustomBottomSheet(
        visible = uiState.isFavoriteModalVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = "Saved to my favorite restaurants!",
            subtitle = "Your favorite preference has been updated.",
            buttonTitle ="Confirm",
            onClick = { viewModel.onEvent(DetailRestaurantEvent.OnRestaurantDismissFavoriteSheet) }
        ){
            SuccessImage()
        }
    }

    if (uiState.isSearchModalVisible) {
        AnimatedVisibility(
            visible = uiState.isSearchModalVisible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(200)),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(200))
        ) {
            DetailSearchScreen(
                query = uiState.searchQuery,
                status = uiState.restaurantResource.data?.statusResult?.status ?: RestaurantStatus.CLOSED,
                resource = searchResult,
                onQueryChange = { viewModel.onEvent(DetailRestaurantEvent.OnSearchQueryChange(it)) },
                onClose = { viewModel.onEvent(DetailRestaurantEvent.OnDismissSearchSheet) }
            )
        }
    }
}

@Composable
fun DetailRestaurantContent(
    snackHostState : SnackbarHostState,
    uiState: DetailRestaurantUiState,
    onRestaurantFavoriteClick: () -> Unit,
    onMenuFavoriteClick: (MenuUiModel) -> Unit,
    onMenuAddToCartClick:(MenuUiModel)-> Unit,
    onShowSchedule: () -> Unit,
    onShowSearchSheet: () -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        snackbarHost = { SnackbarHost(snackHostState) },
        bottomBar = {
            if (uiState.totalItems>0) {
                ShoppingCartBottomBar(
                    itemCount = uiState.totalItems,
                    totalPrice = uiState.totalPrice,
                    onCartClick = {}
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Neutral10),
        ) {
            val restaurantResource = uiState.restaurantResource
            // Header Section
            restaurantResource.data?.let { restaurant ->
                item {
                    HeaderWithVoucherSection(
                        restaurant = restaurant,
                        voucherResource = uiState.vouchersResource,
                        onFavoriteClick = onRestaurantFavoriteClick,
                        onShowSearch = onShowSearchSheet,
                        onShowSchedule = onShowSchedule
                    )
                }

                // Divider
                item { Divider32() }

                // Best Seller Section
                item {
                    BestSellerSection(
                        resource = uiState.bestSellerMenusResource,
                        onFavoriteClick = onMenuFavoriteClick,
                        onAddToCartClick = onMenuAddToCartClick,
                        onNavigateToDetailMenu = onNavigateToDetailMenu,
                        onSeeAllClick = { onNavigateToBestSeller(restaurant.id) }
                    )
                    Spacer(Modifier.height(32.dp))
                }
            }

            // Reviews Section
            item {
                ReviewSection(resource = uiState.reviewsResource)
                Spacer(Modifier.height(32.dp))
            }

            // Recommended Menu Section
            item {
                RecommendedMenuSection(
                    resource = uiState.recommendedMenusResource,
                    onFavoriteClick = onMenuFavoriteClick,
                    onAddToCartClick = onMenuAddToCartClick,
                    onNavigateToDetailMenu = onNavigateToDetailMenu
                )
                Divider32()
            }

            // All Menu Section
            allMenuSection(
                resource = uiState.allMenusResource,
                onFavoriteClick = onMenuFavoriteClick,
                onAddToCart = onMenuAddToCartClick,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )

            // Bottom spacer
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}


@Composable
fun HeaderWithVoucherSection(
    restaurant: DetailRestaurantUiModel,
    voucherResource: Resource<List<VoucherUiModel>>,
    onFavoriteClick: () -> Unit,
    onShowSearch: () -> Unit,
    onShowSchedule: () -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val headerHeight = screenHeight * 0.60f
    val imageHeight = headerHeight * 0.55f
    val cardHeight = headerHeight * 0.55f

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(headerHeight + 80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .drawBehind {
                        drawRect(
                            brush = Brush.radialGradient(
                                colorStops = arrayOf(
                                    0.31f to Color(0xFF737373),
                                    0.69f to Color(0xFF3E3E3E),
                                    0.86f to Color(0xFF1F1E1E)
                                ),
                                center = center,
                                radius = 1900f,
                            )
                        )
                    }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) {
                StatusItemImage(
                    imageUrl = restaurant.imageUrl,
                    name = "detail header image",
                    status = restaurant.statusResult.status,
                    modifier = Modifier
                        .fillMaxSize()
                )

                DetailTopAppBar(
                    isFavorite = restaurant.isWishlist,
                    onShowSearch = onShowSearch,
                    onBackClick = {},
                    onFavoriteClick = onFavoriteClick,
                    onShareClick = {}
                )
            }

            Box(
                modifier = Modifier
                    .background(Neutral10.copy(alpha = 0.9f))
                    .fillMaxWidth()
                    .height(cardHeight)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 36.dp, bottom = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = restaurant.name,
                                style = LocalCustomTypography.current.h3Bold,
                                color = Neutral100
                            )
                            RankBadgeIcon()
                        }

                        Text(
                            text = restaurant.categories,
                            style = LocalCustomTypography.current.bodySmallMedium,
                            color = Neutral70
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        items(1) {
                            RestaurantInfoCard(
                                restaurant = restaurant,
                                operationalHour = restaurant.todayHour?:"",
                                onReviewsClick = {},
                                onLocationClick = {},
                                onScheduleClick = onShowSchedule
                            )
                        }
                    }
                }

                if (restaurant.statusResult.status == RestaurantStatus.CLOSED) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-24).dp)
                            .padding(horizontal = 24.dp)
                    ) {
                        RestaurantCloseStatus(statusMessage =restaurant.statusResult.message)
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            VoucherSection(
                resource = voucherResource,
                status = restaurant.statusResult.status
            )
        }
    }
}

@Composable
fun VoucherSection(
    resource: Resource<List<VoucherUiModel>>,
    status: RestaurantStatus
){
    val voucher = resource.data.orEmpty()

    when{
        resource.isLoading -> LoadingRowState()
        resource.errorMessage!= null || voucher.isEmpty() ->{
            ErrorListState(
                title = "${voucher.size} Vouchers",
                onRetry = {}
            )
        }
        else -> {
            HorizontalTitleItemCountButtonSection(
                itemCount = voucher.size,
                title = "Vouchers",
                onClick = {}
            ) {
                items(voucher, key = {it.id}) { item ->
                    VoucherCard(
                        voucher = item,
                        status = status
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingCartBottomBar(
    itemCount: Int,
    totalPrice: Int,
    onCartClick:()-> Unit
) {
    Row(
        modifier = Modifier.padding(top=24.dp,
            bottom = 36.dp,start=24.dp,end=24.dp)
            .background(Neutral10).clickable{onCartClick()}
    ) {
        CartAddItemButton(
            totalPrice = totalPrice,
            itemCount = itemCount
        )
    }
}

@Composable
fun ReviewSection(
    resource : Resource<List<ReviewUiModel>>
){
    val reviewItems = resource.data.orEmpty()
    when{
        resource.isLoading -> { LoadingRowState() }
        resource.errorMessage!= null || reviewItems.isEmpty() ->{
            ErrorListState(
                title = "What people say about us",
                onRetry = {}
            )
        }
        else ->{
            Box(Modifier.fillMaxWidth().background(Neutral20)
                .padding(top = 20.dp, bottom = 24.dp)
            ) {
                HorizontalTitleButtonSection(
                    title = "What people say about us",
                    onClick = {}
                ) {
                    items(items = reviewItems, key = { it.id}) { item ->
                        ReviewCard(review = item)
                    }
                }
            }
        }
    }
}


fun LazyListScope.allMenuSection(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCart: (MenuUiModel) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit
) {
    val menuItems = resource.data.orEmpty()
    when{
        resource.isLoading -> {
            item { LoadingRowState() }
        }
        resource.errorMessage != null -> {
            item {
                ErrorListState(
                    title = "Our recommended menu",
                    onRetry = {}
                )
            }
        }

        menuItems.isEmpty() ->{
            item {
                ErrorListState(
                    title = "Our recommended menu",
                    onRetry = {}
                )
            }
        }

        else -> {
            menuItemCountVerticalListBlock(
                headerText = "All menus",
                menus = menuItems,
                onFavoriteClick = onFavoriteClick,
                onAddToCart = onAddToCart,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )
        }
    }
}

@Composable
fun BestSellerSection(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCartClick:(MenuUiModel)-> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onSeeAllClick:() -> Unit = {}
) {
    val menuItems = resource.data.orEmpty()
    when {
        resource.isLoading -> {
            LoadingRowState()
        }

        resource.errorMessage != null || menuItems.isEmpty() -> {
            ErrorListState(
                title = "Our best seller",
                onRetry = {}
            )
        }

        else -> {
            HorizontalTitleButtonSection(
                title = "Our best seller",
                onClick = onSeeAllClick
            ) {
                items(menuItems) { item ->
                    FoodGridSoldCard(
                        menu = item,
                        onFavoriteClick = onFavoriteClick,
                        onAddToCart = {
                            if(item.customizable) onNavigateToDetailMenu(item.id)
                            else onAddToCartClick(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedMenuSection(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onAddToCartClick:(MenuUiModel)-> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
) {
    val menuItems = resource.data.orEmpty()

    when{
        resource.isLoading -> {
            LoadingRowState()
        }

        resource.errorMessage != null -> {
            ErrorListState(
                title = "Our recommended menu",
                onRetry = {}
            )
        }

        menuItems.isEmpty() ->{
            ErrorListState(
                title = "Our recommended menu",
                onRetry = {}
            )
        }

        else ->{
            GridMenuListSection(
                isDetail = true,
                title = "Our recommended menu",
                menuItems = menuItems,
                onFavoriteClick = onFavoriteClick,
                onAddToCartClick=onAddToCartClick,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailClosePreview() {
    val snackHostState = remember { SnackbarHostState() }
    DetailRestaurantContent(
        snackHostState = snackHostState,
        uiState = DetailRestaurantUiState(
            restaurantResource = Resource(data = restaurantDetailItem,isLoading = false),
            reviewsResource = Resource(data = reviews,isLoading = false),
            vouchersResource = Resource(data= voucherUiModel,isLoading = false),
            bestSellerMenusResource = Resource(data = menusItem,isLoading = false),
            recommendedMenusResource = Resource(data = menusItem,isLoading = false),
            allMenusResource = Resource(data = menusItem,isLoading = false),
            totalItems = 10,
            totalPrice = 100000,
        ),
        onRestaurantFavoriteClick = {},
        onShowSearchSheet = {},
        onShowSchedule = {},
        onMenuFavoriteClick = {},
        onMenuAddToCartClick = {},
        onNavigateToDetailMenu = {},
        onNavigateToBestSeller = {},
    )
}

