package com.example.tassty.screen.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.CategoryCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.FoodGridCard
import com.example.tassty.component.GridMenuListSection
import com.example.tassty.component.HeaderListTitleButton
import com.example.tassty.component.HorizontalTitleButtonSection
import com.example.tassty.component.RestaurantGridCard
import com.example.tassty.component.VoucherLargeCard
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange900
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.component.ErrorListState
import kotlinx.coroutines.launch
import kotlin.collections.orEmpty
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.util.categories
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.HomeProfile
import com.example.tassty.component.NearbyMapBox
import com.example.tassty.component.ProfileImage
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerGridMenuListPlaceholder
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.component.ShimmerRestaurantGridCard
import com.example.tassty.component.ShimmerVoucherLargeCard
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.menusItem
import com.example.tassty.util.restaurantUiModel
import com.example.tassty.util.voucherUiModel

@Composable
fun HomeScreen(
    onNavigateToDetailRest: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToVoucher: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event->
            when(event){
                is HomeUiEffect.ShowSnackbar -> {
                    snackHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    HomeContent(
        snackHostState = snackHostState,
        uiState = uiState,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToDetail = onNavigateToDetailRest,
        onNavigateToCategory = onNavigateToCategory,
        onNavigateToRecommended = onNavigateToRecommended,
        onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
        onNavigateToDetailMenu = onNavigateToDetailMenu,
        onNavigateToVoucher = onNavigateToVoucher,
        onRefresh = {viewModel.onPullToRefresh()},
        onFavoriteClicked = { viewModel.onEvent(HomeEvent.OnFavoriteClick(it)) }
    )

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onEvent(HomeEvent.OnDismissCollectionSheet) }
    ) {
        CollectionContent(
            resource = uiState.collectionsResource,
            onCollectionSelected = { id, check ->
                viewModel.onEvent(HomeEvent.OnCollectionCheckChange(id,check))
            },
            onSaveCollectionClick = {viewModel.onEvent(HomeEvent.OnSaveToCollection)},
            onAddCollectionClick = { viewModel.onEvent(HomeEvent.OnShowAddCollectionSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent (
            collectionName = uiState.newCollectionName,
            onValueName = {viewModel.onEvent(HomeEvent.OnNewCollectionNameChange(it))},
            onDismissClick = { viewModel.onEvent(HomeEvent.OnDismissAddCollectionSheet) },
            onAddCollection = {viewModel.onEvent(HomeEvent.OnCreateCollection)}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    snackHostState : SnackbarHostState,
    uiState: HomeUiState,
    onRefresh: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToCategory:(String,String, String) -> Unit,
    onNavigateToRecommended:() -> Unit,
    onNavigateToNearbyRestaurant:() -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
    onNavigateToVoucher:() -> Unit,
    onFavoriteClicked: (MenuUiModel) -> Unit
) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()

    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )

    Scaffold(
        containerColor = LocalCustomColors.current.background,
        snackbarHost = { SnackbarHost(snackHostState) },
    ) { padding ->
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenHeight = maxHeight

            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .pullToRefresh(
                        state = refreshState,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = {
                            scope.launch { onRefresh() }
                        }
                    ),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item(key = "header_section"){
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.5f)
                            .drawBehind {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colorStops = arrayOf(
                                            0.0f to Color(0xFF737373),
                                            0.57f to Color(0xFF3E3E3E),
                                            1.0f to Color(0xFF343333)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height)
                                    )
                                )
                            }
                        )
                        Column(modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(80.dp))
                            HeaderSection(
                                userName = uiState.userName,
                                onClick = onNavigateToSearch
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            BannerSection()
                        }
                    }
                }
                item(key = "category_section") {
                    Spacer(Modifier.height(24.dp))
                    CategorySection(
                        resource = uiState.allCategories,
                        onNavigateToCategory = onNavigateToCategory
                    )
                    Divider32()
                }
                item(key = "recommend_menu_section") {
                    RecommendationSection(
                        resource = uiState.recommendedMenus,
                        onFavoriteClicked = onFavoriteClicked,
                        onNavigateToDetailMenu = onNavigateToDetailMenu,
                        onAddCart = {}
                    )
                    Divider32()
                }
                item(key = "nearby_map_section") {
                    RestaurantNearby(
                        resource = uiState.nearbyRestaurants,
                        onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant
                    )
                    Spacer(Modifier.height(32.dp))
                }
                item(key = "recommend_restaurant_section") {
                    RecommendationRestaurant(
                        resource = uiState.recommendedRestaurants,
                        onNavigateToRecommended = onNavigateToRecommended,
                        onNavigateToDetail = onNavigateToDetail
                    )
                    Spacer(Modifier.height(32.dp))
                }
                item(key = "voucher_section"){
                    TodayDeal(
                        resource = uiState.todayVouchers,
                        onNavigateToVoucher = onNavigateToVoucher
                    )
                    Spacer(Modifier.height(32.dp))
                }
                item(key = "suggested_section"){
                    SuggestedMenu(
                        resource = uiState.suggestedMenus,
                        onFavoriteClicked = onFavoriteClicked,
                        onAddCart = {},
                        onNavigateToDetailMenu = onNavigateToDetailMenu
                    )
                }
            }

            TopAppBarSection(
                userName = uiState.userName,
                profileImage = uiState.profileImage,
                addressName = uiState.addressName,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(
                        Orange500.copy(alpha = appBarAlpha)
                    )
            )

            PullToRefreshDefaults.Indicator(
                state = refreshState,
                isRefreshing = uiState.isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = 80.dp)
            )
        }
    }
}

@Composable
fun TopAppBarSection(
    userName: String,
    profileImage: String,
    addressName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
           HomeProfile(
                imageUrl = profileImage,
                name = userName
            )

            Row (
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = Neutral10.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = addressName,
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral10
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(44.dp)
                    .border(
                        width = 1.dp,
                        color = Neutral10.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    userName: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)) {
        Text(
            text = "Hi $userName,",
            color = Color.White,
            style = LocalCustomTypography.current.h2Regular
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Good Morning!",
                color = Color.White,
                style = LocalCustomTypography.current.h2Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "👋",
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            modifier = Modifier.clickable(onClick = onClick),
            onValueChange = {},
            placeholder = stringResource(R.string.search_delicacies),
            isTransparentMode = true,
            enabled = false
        )
    }
}

@Composable
fun BannerSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListTitleButton(
            title= "Special Offers",
            titleColor = Neutral10,
            onClick = {}
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(174.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFFFFCF24),
                            0.60f to Color(0xFFF07C2A),
                            0.82f to Color(0xFFD76413)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Happy Sunday",
                            color = Color.White,
                            style = LocalCustomTypography.current.h2ExtraBold
                        )
                        Text(
                            text = "Get 50%+ Discount!",
                            color = Color.White,
                            style = LocalCustomTypography.current.bodyMediumMedium
                        )

                    }
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange900
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Get Now", color = Neutral10,
                                style = LocalCustomTypography.current.bodySmallMedium)
                            Icon(
                                painter = painterResource(R.drawable.arrow_left_up),
                                contentDescription = "arrow left up",
                                tint = Neutral10
                            )
                        }
                    }
                }

                Image(
                    painter = painterResource(R.drawable.kiwi),
                    contentDescription = "Banner Image",
                    modifier = Modifier
                        .size(185.dp)
                        .offset(y = 8.dp)
                        .zIndex(1f)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
fun CategorySection(
    resource: Resource<List<CategoryUiModel>>,
    onNavigateToCategory:(String,String, String) -> Unit
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading -> {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(8){
                    Spacer(modifier = Modifier.size(50.dp).clip(CircleShape)
                        .shimmerLoadingAnimation()
                    )
                }
            }
        }

        resource.errorMessage!= null || items.isEmpty() -> {
            ErrorListState("") { }
        }

        else ->{
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items (items = items, key = { it.id }) { category ->
                    CategoryCard(
                        category = category,
                        onClick = {
                            onNavigateToCategory(category.id,category.name,
                                category.imageUrl)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationSection(
    resource : Resource<List<MenuUiModel>>,
    onFavoriteClicked: (MenuUiModel) -> Unit,
    onAddCart:(MenuUiModel) -> Unit,
    onNavigateToDetailMenu:(String) -> Unit,
) {
    val items = resource.data.orEmpty()
    when {
        resource.isLoading -> {
            ShimmerHorizontalTitleButtonSection {
                items(4) {
                    ShimmerFoodGridCard()
                }
            }
        }

        resource.errorMessage != null || items.isEmpty() -> {
            ErrorListState(
                title = "Recommended for you",
                onRetry = {}
            )
        }

        else -> {
            HorizontalTitleButtonSection(
                title = "Recommended for you",
                onClick = {}
            ) {
                itemsIndexed(
                    items = items,
                    key = { index, menu -> menu.id }
                ) { index, menu ->
                    FoodGridCard(
                        menu = menu,
                        onFavoriteClick = onFavoriteClicked,
                        onAddToCart = {
                            if (menu.customizable) onNavigateToDetailMenu(menu.id)
                            else onAddCart(menu)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantNearby(
    resource: Resource<List<RestaurantUiModel>>,
    onNavigateToNearbyRestaurant:() -> Unit,
) {
    val items = resource.data.orEmpty()

    when{
        resource.isLoading ->{
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.4f).clip(RoundedCornerShape(20.dp))
                            .height(14.dp)
                            .shimmerLoadingAnimation()
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.4f).clip(RoundedCornerShape(20.dp))
                            .height(14.dp)
                            .shimmerLoadingAnimation()
                    )
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Neutral20
                    ),
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(200.dp)
                            .shimmerLoadingAnimation()
                    )
                }
            }
        }
        resource.errorMessage!=null || items.isEmpty() -> {
            ErrorListState("Resto Nearby you") { }
        }
        else -> {
            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderListTitleButton(
                    title = "Restos Nearby you",
                    titleColor = LocalCustomColors.current.headerText,
                    onClick = onNavigateToNearbyRestaurant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                NearbyMapBox(restaurant = items)
            }
        }
    }
}

@Composable
fun RecommendationRestaurant(
    resource : Resource<List<RestaurantUiModel>>,
    onNavigateToRecommended:() -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    val items = resource.data.orEmpty()

    when{
        resource.isLoading -> {
            ShimmerHorizontalTitleButtonSection {
                items(4){
                    ShimmerRestaurantGridCard()
                }
            }
        }

        resource.errorMessage!=null || items.isEmpty() -> {
            ErrorListState(
                title = "Recommended Restaurant",
                onRetry = {}
            )
        }

        else ->{
            HorizontalTitleButtonSection(
                title = "Recommended Restaurant",
                onClick = onNavigateToRecommended
            ) {
                itemsIndexed(
                    items = items,
                    key = { index, item -> item.id }
                ) { index, restaurant ->
                    RestaurantGridCard(
                        restaurant = restaurant,
                        onNavigateToDetail = {
                            onNavigateToDetail(restaurant.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodayDeal(
    resource: Resource<List<VoucherUiModel>>,
    onNavigateToVoucher: () -> Unit
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading -> {
            ShimmerHorizontalTitleButtonSection {
                items(4){
                    ShimmerVoucherLargeCard()
                }
            }
        }

        resource.errorMessage!=null || items.isEmpty() -> {
            ErrorListState("Today's deals") { }
        }

        else ->{
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.cardBackground)
                .padding(vertical = 24.dp)
            ) {
                HorizontalTitleButtonSection(
                    title= "Today's deals",
                    onClick = onNavigateToVoucher
                ) {
                    items(items = items, key = {it.id}) {
                        VoucherLargeCard(voucher = it)
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestedMenu(
    resource: Resource<List<MenuUiModel>>,
    onFavoriteClicked: (MenuUiModel) -> Unit,
    onAddCart:(MenuUiModel) -> Unit,
    onNavigateToDetailMenu : (String) -> Unit
) {
    val items = resource.data.orEmpty()

    when {
        resource.isLoading -> {
            ShimmerGridMenuListPlaceholder()
        }

        resource.errorMessage != null || items.isEmpty() -> {
            ErrorListState(
                title = "Suggested menu for you!",
                onRetry = {}
            )
        }

        else -> {
            GridMenuListSection(
                title = "Suggested menu for you!",
                menuItems = items,
                onFavoriteClick = onFavoriteClicked,
                onAddToCartClick = onAddCart,
                onNavigateToDetailMenu = onNavigateToDetailMenu
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun HomeLightPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = false){
//        HomeContent(
//            snackHostState = snackHostState,
//            uiState = HomeUiState(
//                userName = "Atifa",
//                addressName = "Guest",
//                allCategories = Resource(data = categories, isLoading = false),
//                recommendedRestaurants = Resource(data = restaurantUiModel, isLoading = false),
//                nearbyRestaurants = Resource(data = restaurantUiModel, isLoading = false),
//                todayVouchers = Resource(data = emptyList(), isLoading = false),
//                recommendedMenus = Resource(data = menusItem, isLoading = false),
//                suggestedMenus = Resource(data = menusItem, isLoading = false),
//            ),
//            onRefresh = {},
//            onFavoriteClicked = {},
//            onNavigateToDetail = {},
//            onNavigateToSearch = {},
//            onNavigateToRecommended = {},
//            onNavigateToCategory = { _, _, _ -> },
//            onNavigateToNearbyRestaurant = {},
//            onNavigateToDetailMenu = {},
//            onNavigateToVoucher = {}
//        )
//    }
//}
//
@Preview(showBackground = true, name = "DarkMode")
@Composable
fun HomeDarkPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme(darkTheme = true){
        HomeContent(
            snackHostState = snackHostState,
            uiState = HomeUiState(
                userName = "Guest",
                addressName = "Guest",
                allCategories = Resource(data = categories, isLoading = false),
                recommendedRestaurants = Resource(data = restaurantUiModel, isLoading = false),
                nearbyRestaurants = Resource(data = restaurantUiModel, isLoading = false),
                todayVouchers = Resource(data = voucherUiModel, isLoading = false),
                recommendedMenus = Resource(data = menusItem, isLoading = false),
                suggestedMenus = Resource(data = menusItem, isLoading = false),
            ),
            onRefresh = {},
            onFavoriteClicked = {},
            onNavigateToDetail = {},
            onNavigateToSearch = {},
            onNavigateToRecommended = {},
            onNavigateToCategory = { _, _, _ -> },
            onNavigateToNearbyRestaurant = {},
            onNavigateToDetailMenu = {},
            onNavigateToVoucher = {}
        )
    }
}