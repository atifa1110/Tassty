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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantDetailUiModel
import com.example.core.ui.model.RestaurantStatusResult
import com.example.tassty.component.CartAddItemButton
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DetailScheduleContent
import com.example.tassty.component.DetailTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodGridCard
import com.example.tassty.component.GridMenuListSection
import com.example.tassty.component.HorizontalTitleButtonSection
import com.example.tassty.component.HorizontalTitleItemCountButtonSection
import com.example.tassty.component.LoadingRowState
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
import com.example.tassty.getSampleVouchers
import com.example.tassty.menus
import com.example.tassty.model.Review
import com.example.tassty.model.Voucher
import com.example.tassty.restaurantDetailItem
import com.example.tassty.reviews
import com.example.tassty.screen.DetailSearchScreen
import com.example.tassty.screen.search.Resource
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import kotlin.collections.orEmpty

@Composable
fun DetailRestaurantScreen(
    viewModel: DetailRestaurantViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailRestaurantContent(
        uiState = uiState,
        status = uiState.status?: RestaurantStatusResult(RestaurantStatus.CLOSED,""),
        onShowSchedule = { viewModel.onEvent(DetailRestaurantEvent.OnShowScheduleSheet) },
        onRestaurantFavoriteClick = { viewModel.onEvent(DetailRestaurantEvent.OnRestaurantFavoriteSheet) },
        onAllMenuFavoriteClick = { viewModel.onEvent(DetailRestaurantEvent.OnAllMenuFavorite(it))},
        onRecommendedFavoriteClick = { viewModel.onEvent(DetailRestaurantEvent.OnRecommendedFavorite(it))},
        onBestSellerFavorite = { viewModel.onEvent(DetailRestaurantEvent.OnBestSellerFavorite(it))},
        onAddToCart = { menu -> viewModel.onEvent(DetailRestaurantEvent.OnAddToCart(menu))},
        onShowSearchSheet = { viewModel.onEvent(DetailRestaurantEvent.OnShowSearchSheet) }
    )

    CustomBottomSheet(
        visible = uiState.isScheduleModalVisible,
        onDismiss = { viewModel.onEvent(DetailRestaurantEvent.OnDismissScheduleSheet)}
    ) {
        DetailScheduleContent(
            operationalHours = uiState.restaurantResource.data?.detail?.restaurant?.operationalHours?:emptyList()
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
        visible = uiState.isFavoriteModalVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = "Saved to my favorite restaurants!",
            subtitle = "Lorem ipsum dolor sit amet, consectetur \nadipiscing elit, sed do eiusmod.",
            buttonTitle ="Confirm",
            onClick = { viewModel.onEvent(DetailRestaurantEvent.OnRestaurantDismissFavoriteSheet) }
        ){
            SuccessImage()
        }
    }

    if (uiState.showSearch) {
        AnimatedVisibility(
            visible = uiState.showSearch,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // masuk dari kanan
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(200)),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // keluar ke kanan lagi
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(200))
        ) {
            DetailSearchScreen(
                query = uiState.searchQuery,
                status = uiState.status?.status ?: RestaurantStatus.CLOSED,
                resource = uiState.searchResultsResource,
                onQueryChange = { viewModel.onEvent(DetailRestaurantEvent.OnSearchQueryChange(it)) },
                onClose = { viewModel.onEvent(DetailRestaurantEvent.OnDismissSearchSheet) }
            )
        }
    }
}

@Composable
fun DetailRestaurantContent(
    uiState: DetailRestaurantUiState,
    status : RestaurantStatusResult,
    onRestaurantFavoriteClick : () -> Unit,
    onAllMenuFavoriteClick: (String) -> Unit,
    onRecommendedFavoriteClick: (String) -> Unit,
    onBestSellerFavorite: (String) -> Unit,
    onAddToCart: (MenuUiModel) -> Unit,
    onShowSchedule : () -> Unit,
    onShowSearchSheet : () -> Unit,
) {
    Scaffold (
        containerColor = Neutral10,
        bottomBar = {
            if(uiState.cartItemsResource.isNotEmpty()) {
                ShoppingCartBottomBar(
                    itemCount = uiState.cartItemCount,
                    totalPrice = uiState.cartTotalPrice,
                    onCartClick = {}
                )
            }
        }
    ){ padding->
        if(uiState.restaurantResource.errorMessage!=null){
            ErrorScreen()
        }else {
            LazyColumn(
                modifier = Modifier.padding(padding)
                    .fillMaxSize().background(Neutral10),
            ) {
                // 1. Header Section (Single Item)
                item {
                    HeaderWithVoucherSection(
                        restaurant = uiState.restaurantResource.data,
                        voucherResource = uiState.voucherResource,
                        status = status,
                        isFavorite = uiState.isFavorite,
                        operationalHour = uiState.todayHoursString,
                        onFavoriteClick = onRestaurantFavoriteClick,
                        onShowSearch = onShowSearchSheet,
                        onShowSchedule = onShowSchedule
                    )
                }

                // 2. Divider Section (Single Item)
                item {
                    Divider32()
                }

                // 3. Our Best Seller (Single Item - Horizontal LazyRow inside)
                item {
                    BestSellerSection(
                        resource = uiState.bestSellersResource,
                        onFavoriteClick = onBestSellerFavorite,
                        onAddToCart = onAddToCart
                    )
                    Spacer(Modifier.height(32.dp))
                }

                // 4. Reviews Section
                item {
                    ReviewSection(resource = uiState.reviewsResource)
                    Spacer(Modifier.height(32.dp))
                }

                item {
                    RecommendedMenuSection(
                        resource = uiState.recommendedMenusResource,
                        onFavoriteClick = onRecommendedFavoriteClick
                    )
                    Divider32()
                }

                allMenuSection(
                    resource = uiState.allMenusResource,
                    status = status.status,
                    onFavoriteClick = onAllMenuFavoriteClick
                )

                item {
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderWithVoucherSection(
    restaurant: RestaurantDetailUiModel?,
    voucherResource: Resource<List<Voucher>>,
    status: RestaurantStatusResult,
    isFavorite: Boolean,
    operationalHour: String,
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
                    imageUrl = restaurant?.detail?.restaurant?.imageUrl?:"",
                    name = "detail header image",
                    status = status.status,
                    modifier = Modifier
                        .fillMaxSize()
                )
                // ==== Top Bar ====
                DetailTopAppBar(
                    isFavorite = isFavorite,
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
                                text = restaurant?.detail?.restaurant?.name?:"",
                                style = LocalCustomTypography.current.h3Bold,
                                color = Neutral100
                            )
                            RankBadgeIcon()
                        }

                        Text(
                            text = restaurant?.formattedCategories?:"",
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
                                restaurant = restaurantDetailItem,
                                operationalHour = operationalHour,
                                onReviewsClick = {},
                                onLocationClick = {},
                                onScheduleClick = onShowSchedule
                            )
                        }
                    }
                }

                if (status.status == RestaurantStatus.CLOSED) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-24).dp)
                            .padding(horizontal = 24.dp)
                    ) {
                        RestaurantCloseStatus(statusMessage = status.message)
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
                status = status.status
            )
        }
    }
}

@Composable
fun VoucherSection(
    resource: Resource<List<Voucher>>,
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
    resource : Resource<List<Review>>
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
                    items(reviews, key = { it.userName }) { item ->
                        ReviewCard(review = item)
                    }
                }
            }
        }
    }
}


fun LazyListScope.allMenuSection(
    resource: Resource<List<MenuUiModel>>,
    status: RestaurantStatus,
    onFavoriteClick: (String) -> Unit
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
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun BestSellerSection(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (MenuUiModel) -> Unit
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
                onClick = {}
            ) {
                items(menuItems) { item ->
                    FoodGridCard(
                        menu = item,
                        onFavoriteClick = onFavoriteClick,
                        onAddToCart = onAddToCart
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedMenuSection(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClick: (String) -> Unit,
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
                title = "Our recommended menu",
                menuItems = menuItems,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailClosePreview() {
    DetailRestaurantContent(
        uiState = DetailRestaurantUiState(
            restaurantResource = Resource(data = restaurantDetailItem,isLoading = false),
            bestSellersResource = Resource(data = menus,isLoading = false),
            recommendedMenusResource = Resource(data = menus,isLoading = false),
            allMenusResource = Resource(data = menus,isLoading = false),
            reviewsResource = Resource(data = reviews,isLoading = false),
            voucherResource = Resource(data= getSampleVouchers(),isLoading = false)
        ),
        status = RestaurantStatusResult(RestaurantStatus.OPEN,""),
        onRestaurantFavoriteClick = {},
        onShowSearchSheet = {},
        onShowSchedule = {},
        onAllMenuFavoriteClick = {},
        onRecommendedFavoriteClick = {},
        onBestSellerFavorite = {},
        onAddToCart = {}
    )
}

