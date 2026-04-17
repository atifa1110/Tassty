package com.example.tassty.screen.detailrestaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantLocationArgs
import com.example.core.ui.model.ReviewUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.R
import com.example.tassty.component.CartAddItemButton
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DetailScheduleContent
import com.example.tassty.component.DetailTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodGridSoldCard
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
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.component.StatusItemImage
import com.example.tassty.component.SuccessIcon
import com.example.tassty.component.VoucherCard
import com.example.tassty.component.menuItemCountVerticalListBlock
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.menusItem
import com.example.tassty.util.restaurantDetailItem
import com.example.tassty.util.reviews
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.voucherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.collections.orEmpty

@Composable
fun DetailRestaurantScreen(
    snackHostState : SnackbarHostState,
    onNavigateBack: () -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit,
    onNavigateToReview:(String) -> Unit,
    onNavigateToVoucher:() -> Unit,
    onNavigateToDetailLocation: (RestaurantLocationArgs) -> Unit,
    viewModel: DetailRestaurantViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResultsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event ->
            when(event) {
                is DetailUiEvent.ShowSnackbar -> {
                    snackHostState.showSnackbar(event.message)
                }
                is DetailUiEvent.NavigateToLocation ->{
                    onNavigateToDetailLocation(event.data)
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
        onNavigateToDetailMenu = onNavigateToDetailMenu,
        onNavigateToBestSeller = onNavigateToBestSeller,
        onNavigateToReview = onNavigateToReview,
        onNavigateBack = onNavigateBack,
        onNavigateToVoucher = onNavigateToVoucher,
        onLocationClick = viewModel::onLocationClick
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
           items = uiState.collectionsResource,
            onCollectionSelected = {id, check -> viewModel.onEvent(DetailRestaurantEvent.OnCollectionCheckChange(id,check))},
            onSaveCollectionClick = {viewModel.onEvent(DetailRestaurantEvent.OnSaveToCollection)},
            onAddCollectionClick = { viewModel.onEvent(DetailRestaurantEvent.OnShowAddCollectionSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = uiState.newCollectionName,
            onValueName = {viewModel.onEvent(DetailRestaurantEvent.OnNewCollectionNameChange(it))},
            onDismissClick = { viewModel.onEvent(DetailRestaurantEvent.OnDismissAddCollectionSheet) },
            onAddCollection = {viewModel.onEvent(DetailRestaurantEvent.OnCreateCollection)}
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
            title = stringResource(R.string.saved_to_my_favorite_restaurants),
            subtitle = stringResource(R.string.your_favorite_preference_has_been_updated),
            buttonTitle= stringResource(R.string.confirm),
            onClick = { viewModel.onEvent(DetailRestaurantEvent.OnRestaurantDismissFavoriteSheet) }
        ) {
            SuccessIcon()
        }
    }

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
            resource = searchResult,
            onQueryChange = { viewModel.onEvent(DetailRestaurantEvent.OnSearchQueryChange(it)) },
            onClose = { viewModel.onEvent(DetailRestaurantEvent.OnDismissSearchSheet) }
        )
    }
}

@Composable
fun DetailRestaurantContent(
    snackHostState : SnackbarHostState,
    uiState: DetailRestaurantUiState,
    onLocationClick:() -> Unit,
    onRestaurantFavoriteClick: () -> Unit,
    onMenuFavoriteClick: (MenuUiModel) -> Unit,
    onShowSchedule: () -> Unit,
    onShowSearchSheet: () -> Unit,
    onNavigateBack:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit,
    onNavigateToReview:(String) -> Unit,
    onNavigateToVoucher:() -> Unit,
) {
    val resource = uiState.restaurantResource.data
    val isRestaurantLoading = uiState.restaurantResource.isLoading

    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    val iconBgColor by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.background,
        animationSpec = tween(300),
        label = "iconBackground"
    )

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    val background = LocalCustomColors.current.background
    Scaffold(
        containerColor = background,
        snackbarHost = { SnackbarHost(snackHostState) },
        bottomBar = {
            if (uiState.totalItems > 0) {
                ShoppingCartBottomBar(
                    itemCount = uiState.totalItems,
                    totalPrice = uiState.totalPrice,
                    onCartClick = {}
                )
            }
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if(isRestaurantLoading){
                    loadingSection()
                }else if(uiState.restaurantResource.errorMessage!= null) {
                    errorSection()
                }else{
                    contentSection(
                        headerHeight = maxHeight,
                        uiState = uiState,
                        onMenuFavoriteClick = onMenuFavoriteClick,
                        onShowSchedule = onShowSchedule,
                        onNavigateToDetailMenu = onNavigateToDetailMenu,
                        onNavigateToBestSeller = onNavigateToBestSeller,
                        onNavigateToReview = onNavigateToReview,
                        onNavigateToVoucher = onNavigateToVoucher,
                        onLocationClick = onLocationClick
                    )
                }
            }

            DetailTopAppBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .drawBehind {
                        drawRect(color = background.copy(alpha = appBarAlpha))
                    },
                iconBackground = iconBgColor,
                isFavorite = resource?.isWishlist ?: false,
                onShowSearch = onShowSearchSheet,
                onBackClick = onNavigateBack,
                onFavoriteClick = onRestaurantFavoriteClick,
                onShareClick = {}
            )
        }
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

fun LazyListScope.contentSection(
    uiState: DetailRestaurantUiState,
    headerHeight: Dp,
    onLocationClick:() -> Unit,
    onMenuFavoriteClick: (MenuUiModel) -> Unit,
    onShowSchedule: () -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToBestSeller:(String) -> Unit,
    onNavigateToReview:(String) -> Unit,
    onNavigateToVoucher:() -> Unit
){
    item(key = "header_section") {
        uiState.restaurantResource.data?.let { rest->
            HeaderSection(
                fixedHeight = headerHeight,
                restaurant = rest,
                voucherResource = uiState.vouchersResource,
                onShowSchedule = onShowSchedule,
                onNavigateToReview = onNavigateToReview,
                onNavigateToVoucher = onNavigateToVoucher,
                onLocationClick = onLocationClick
            )
        }
    }

    item(key = "best_seller_section") {
        Divider32()
        uiState.restaurantResource.data?.let { rest ->
            BestSellerSection(
                resource = uiState.bestSellerMenusResource,
                onFavoriteClick = onMenuFavoriteClick,
                onNavigateToDetailMenu = onNavigateToDetailMenu,
                onSeeAllClick = { onNavigateToBestSeller(rest.id) }
            )
        }
    }
    item(key = "review_section") {
        Spacer(modifier = Modifier.height(32.dp))
        uiState.restaurantResource.data?.let { rest->
            ReviewSection(
                resource = uiState.reviewsResource,
                onReviewClick = { onNavigateToReview(rest.id) }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

    item(key = "recommended_section") {
        RecommendedMenuSection(
            resource = uiState.recommendedMenusResource,
            onFavoriteClick = onMenuFavoriteClick,
            onNavigateToDetailMenu = onNavigateToDetailMenu
        )
        Divider32()
    }

    allMenuSection(
        resource = uiState.allMenusResource,
        onFavoriteClick = onMenuFavoriteClick,
        onNavigateToDetailMenu = onNavigateToDetailMenu
    )
}
fun LazyListScope.loadingSection() {
    item(key="load_header_section") {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .shimmerLoadingAnimation()
        )
    }

    item(key="load_menu_section") {
        Spacer(Modifier.height(24.dp))
        ShimmerHorizontalTitleButtonSection {
            items(4) {
                ShimmerFoodGridCard()
            }
        }
        Spacer(Modifier.height(32.dp))
    }

    item(key="load_review_section") {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .shimmerLoadingAnimation()
        )
    }
}

@Composable
fun HeaderSection(
    fixedHeight: Dp,
    restaurant: DetailRestaurantUiModel,
    voucherResource: Resource<ImmutableList<VoucherUiModel>>,
    onShowSchedule: () -> Unit,
    onLocationClick:() -> Unit,
    onNavigateToReview:(String) -> Unit,
    onNavigateToVoucher:() -> Unit
) {
    val imageHeight = fixedHeight * 0.4f
    val drawImage = fixedHeight * 0.55f
    val voucherHeight = 150.dp

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(drawImage + (voucherHeight / 2))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(drawImage)
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.31f to Color(0xFF737373),
                                0.69f to Color(0xFF3E3E3E),
                                0.86f to Color(0xFF1F1E1E)
                            ),
                            center = center,
                            radius = size.width * 1.5f
                        )
                    )
                }
        )

        StatusItemImage(
            imageUrl = restaurant.imageUrl,
            name = "detail header image",
            status = restaurant.statusResult.status,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(drawImage * 0.8f)
                .align(Alignment.BottomCenter)
                .padding(bottom = voucherHeight / 2)
                .background(
                    color = LocalCustomColors.current.modalBackgroundFrame
                )
                .offset(y = if (restaurant.statusResult.status == RestaurantStatus.CLOSED) (-30).dp else 0.dp)
        ) {
            if (restaurant.statusResult.status == RestaurantStatus.CLOSED) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)){
                    RestaurantCloseStatus(statusMessage = restaurant.statusResult.message)
                }
                Spacer(Modifier.height(16.dp))
            }

            RestaurantInfoDetails(
                restaurant = restaurant,
                onShowSchedule = onShowSchedule,
                onLocationClick = onLocationClick,
                onReviewsClick = { onNavigateToReview(restaurant.id) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(voucherHeight)
                .align(Alignment.BottomCenter)
        ) {
            VoucherSection(
                resource = voucherResource,
                status = restaurant.statusResult.status,
                onClick = onNavigateToVoucher
            )
        }
    }
}

@Composable
fun RestaurantInfoDetails(
    restaurant: DetailRestaurantUiModel,
    onShowSchedule: () -> Unit,
    onLocationClick:() -> Unit,
    onReviewsClick:() -> Unit
) {
    val status = restaurant.statusResult.status
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (status == RestaurantStatus.CLOSED) 0.dp else 24.dp,
                    start = 24.dp, end = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = restaurant.name,
                    style = LocalCustomTypography.current.h3Bold,
                    color = LocalCustomColors.current.headerText
                )
                if(restaurant.rank!=0) {
                    Spacer(Modifier.width(8.dp))
                    RankBadgeIcon(
                        restaurant.rank
                    )
                }
            }

            Text(
                text = restaurant.categories,
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.text
            )
        }

        Spacer(Modifier.height(12.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            item {
                RestaurantInfoCard(
                    restaurant = restaurant,
                    operationalHour = restaurant.todayHour,
                    onReviewsClick = onReviewsClick,
                    onLocationClick = onLocationClick,
                    onScheduleClick = onShowSchedule
                )
            }
        }
        if(status == RestaurantStatus.CLOSED) {
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun VoucherSection(
    resource: Resource<ImmutableList<VoucherUiModel>>,
    status: RestaurantStatus,
    onClick: () -> Unit
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
                title = stringResource(R.string.vouchers),
                onClick = onClick
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
    Row(modifier = Modifier
        .padding(top = 24.dp, bottom = 36.dp, start = 24.dp, end = 24.dp)
        .clickable { onCartClick() }
    ) {
        CartAddItemButton(
            totalPrice = totalPrice,
            itemCount = itemCount
        )
    }
}

@Composable
fun ReviewSection(
    resource : Resource<ImmutableList<ReviewUiModel>>,
    onReviewClick:() -> Unit,
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
            Box(Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.cardBackground)
                .padding(top = 20.dp, bottom = 24.dp)
            ) {
                HorizontalTitleButtonSection(
                    title = stringResource(R.string.what_people_say_about_us),
                    onClick = onReviewClick
                ) {
                    items(items = reviewItems, key = { it.id }) { item ->
                        ReviewCard(review = item)
                    }
                }
            }
        }
    }
}


fun LazyListScope.allMenuSection(
    resource: Resource<ImmutableList<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
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
                    title = stringResource(R.string.our_recommended_menu),
                    onRetry = {}
                )
            }
        }

        menuItems.isEmpty() ->{
            item {
                ErrorListState(
                    title = stringResource(R.string.our_recommended_menu),
                    onRetry = {}
                )
            }
        }

        else -> {
            menuItemCountVerticalListBlock(
                menus = menuItems,
                onFavoriteClick = onFavoriteClick,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )
        }
    }
}

@Composable
fun BestSellerSection(
    resource: Resource<ImmutableList<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
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
                title = stringResource(R.string.our_best_seller),
                onRetry = {}
            )
        }

        else -> {
            HorizontalTitleButtonSection(
                title = stringResource(R.string.our_best_seller),
                onClick = onSeeAllClick
            ) {
                items(menuItems) { item ->
                    FoodGridSoldCard(
                        menu = item,
                        onFavoriteClick = onFavoriteClick,
                        onAddToCart = { onNavigateToDetailMenu(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedMenuSection(
    resource: Resource<ImmutableList<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
) {
    val menuItems = resource.data.orEmpty()

    when{
        resource.isLoading -> {
            LoadingRowState()
        }

        resource.errorMessage != null || menuItems.isEmpty()-> {
            ErrorListState(
                title = stringResource(R.string.our_recommended_menu),
                onRetry = {}
            )
        }

        else ->{
            GridMenuListSection(
                isDetail = true,
                title = stringResource(R.string.our_recommended_menu),
                menuItems = menuItems,
                onFavoriteClick = onFavoriteClick,
                onAddToCartClick= onNavigateToDetailMenu
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun DetailRestLightPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme {
        DetailRestaurantContent(
            snackHostState = snackHostState,
            uiState = DetailRestaurantUiState(
                restaurantResource = Resource(data = restaurantDetailItem),
                reviewsResource = Resource(data = reviews.toImmutableList(), isLoading = false),
                vouchersResource = Resource(data = voucherUiModel.toImmutableList(), isLoading = false),
                bestSellerMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                recommendedMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                allMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                totalItems = 0,
                totalPrice = 0,
            ),
            onRestaurantFavoriteClick = {},
            onShowSearchSheet = {},
            onShowSchedule = {},
            onMenuFavoriteClick = {},
            onNavigateToDetailMenu = {},
            onNavigateToBestSeller = {},
            onNavigateToReview = {},
            onNavigateBack = {},
            onNavigateToVoucher = {},
            onLocationClick = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun DetailRestDarkPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme(darkTheme = true) {
        DetailRestaurantContent(
            snackHostState = snackHostState,
            uiState = DetailRestaurantUiState(
                restaurantResource = Resource(data = restaurantDetailItem),
                reviewsResource = Resource(data = reviews.toImmutableList(), isLoading = false),
                vouchersResource = Resource(data = voucherUiModel.toImmutableList(), isLoading = false),
                bestSellerMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                recommendedMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                allMenusResource = Resource(data = menusItem.toImmutableList(), isLoading = false),
                totalItems = 0,
                totalPrice = 0,
            ),
            onRestaurantFavoriteClick = {},
            onShowSearchSheet = {},
            onShowSchedule = {},
            onMenuFavoriteClick = {},
            onNavigateToDetailMenu = {},
            onNavigateToBestSeller = {},
            onNavigateToReview = {},
            onNavigateBack = {},
            onNavigateToVoucher = {},
            onLocationClick = {}
        )
    }
}

