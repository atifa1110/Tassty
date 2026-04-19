package com.example.tassty.screen.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
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
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.component.ErrorListState
import kotlin.collections.orEmpty
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.component.CollectionAddContent
import com.example.tassty.component.CollectionContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HomeProfile
import com.example.tassty.component.NearbyMapBox
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerGridMenuListPlaceholder
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.component.ShimmerRestaurantGridCard
import com.example.tassty.component.ShimmerVoucherLargeCard
import com.example.tassty.component.SpecialCardOffer
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.MenuPreviewData
import com.example.tassty.util.RestaurantPreviewData
import com.example.tassty.util.VoucherData
import kotlinx.collections.immutable.ImmutableList

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
                is HomeEffect.ShowSnackbar -> {
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
        onNavigateToVoucher = onNavigateToVoucher,
        onNavigateToNearbyRestaurant = onNavigateToNearbyRestaurant,
        onCartClick =  onNavigateToDetailMenu,
        onFavoriteClick = { viewModel.onEvent(HomeEvent.OnFavoriteClick(it)) },
        onRefresh = viewModel::onPullToRefresh
    )

    CustomBottomSheet(
        visible = uiState.isCollectionSheetVisible,
        onDismiss = { viewModel.onEvent(HomeEvent.OnDismissCollectionSheet) }
    ) {
        CollectionContent (
            items = uiState.collections,
            onCollectionSelected = { id, check ->
                viewModel.onEvent(HomeEvent.OnCollectionCheckChange(id, check))
            },
            onSaveCollectionClick = { viewModel.onEvent(HomeEvent.OnSaveToCollection) },
            onAddCollectionClick = { viewModel.onEvent(HomeEvent.OnShowAddCollectionSheet) }
        )
    }

    CustomBottomSheet(
        visible = uiState.isAddCollectionSheet,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CollectionAddContent(
            collectionName = uiState.newCollectionName,
            onValueName = { viewModel.onEvent(HomeEvent.OnNewCollectionNameChange(it)) },
            onDismissClick = { viewModel.onEvent(HomeEvent.OnDismissAddCollectionSheet) },
            onAddCollection = { viewModel.onEvent(HomeEvent.OnCreateCollection) }
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
    onNavigateToVoucher:() -> Unit,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onCartClick: (String) -> Unit,
) {
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

            if(uiState.allCategories.errorMessage!=null){
                Box(modifier = Modifier.fillMaxSize().matchParentSize(),
                    contentAlignment = Alignment.Center
                ){
                    ErrorScreen(onClick = onRefresh)
                }
            }else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .pullToRefresh(
                            state = refreshState,
                            isRefreshing = uiState.isRefreshing,
                            onRefresh = onRefresh
                        ),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item(key = "header_section") {
                        HeaderSection(
                            fixedHeight = screenHeight,
                            userName = uiState.userName,
                            onClick = onNavigateToSearch
                        )
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
                            onFavoriteClicked = onFavoriteClick,
                            onCartClick = onCartClick
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
                    item(key = "voucher_section") {
                        TodayDeal(
                            resource = uiState.todayVouchers,
                            onNavigateToVoucher = onNavigateToVoucher
                        )
                        Spacer(Modifier.height(32.dp))
                    }
                    item(key = "suggested_section") {
                        SuggestedMenu(
                            resource = uiState.suggestedMenus,
                            onFavoriteClick = onFavoriteClick,
                            onCartClick = onCartClick,
                        )
                    }
                }
            }

            TopAppBarSection(
                userName = uiState.userName,
                profileImage = uiState.profileImage,
                addressName = uiState.addressName,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .drawBehind {
                        drawRect(color = Orange500.copy(alpha = appBarAlpha))
                    }

            )

            PullToRefreshDefaults.Indicator(
                state = refreshState,
                isRefreshing = uiState.isRefreshing,
                modifier = Modifier
                    .align(Alignment.TopCenter)
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
    Box(modifier = modifier
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
                        tint = Neutral10,
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
                    tint = Neutral10,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    fixedHeight: Dp,
    userName: String,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(fixedHeight * 0.5f)
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
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(80.dp))
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)) {
                Text(
                    text = stringResource(R.string.hi, userName),
                    color = Neutral10,
                    style = LocalCustomTypography.current.h2Regular
                )
                Text(
                    text = "Good Morning! 👋",
                    color = Neutral10,
                    style = LocalCustomTypography.current.h2Bold
                )
                SearchBar(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable(onClick = onClick),
                    onValueChange = {},
                    placeholder = stringResource(R.string.search_delicacies),
                    isTransparentMode = true,
                    enabled = false
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            BannerSection()
        }
    }
}

@Composable
fun BannerSection() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListTitleButton(
            title = stringResource(R.string.special_offers),
            titleColor = Neutral10,
            onClick = {}
        )
        SpecialCardOffer()
    }
}

@Composable
fun CategorySection(
    resource: Resource<ImmutableList<CategoryUiModel>>,
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
                    Spacer(modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
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
    resource : Resource<ImmutableList<MenuUiModel>>,
    onFavoriteClicked: (MenuUiModel) -> Unit,
    onCartClick: (String) -> Unit
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
                title = stringResource(R.string.recommended_for_you),
                onRetry = {}
            )
        }

        else -> {
            HorizontalTitleButtonSection(
                title = stringResource(R.string.recommended_for_you),
                onClick = {}
            ) {
                itemsIndexed(
                    items = items,
                    key = { index, menu -> menu.id }
                ) { index, menu ->
                    FoodGridCard(
                        menu = menu,
                        onFavoriteClick = onFavoriteClicked,
                        onAddToCart = { onCartClick(menu.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantNearby(
    resource: Resource<ImmutableList<RestaurantUiModel>>,
    onNavigateToNearbyRestaurant:() -> Unit,
) {
    val items = resource.data.orEmpty()

    when{
        resource.isLoading ->{
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(20.dp))
                            .height(14.dp)
                            .shimmerLoadingAnimation()
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(20.dp))
                            .height(14.dp)
                            .shimmerLoadingAnimation()
                    )
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LocalCustomColors.current.cardBackground
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
            ErrorListState(title = stringResource(R.string.restos_nearby_you)) { }
        }
        else -> {
            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderListTitleButton(
                    title = stringResource(R.string.restos_nearby_you),
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
    resource : Resource<ImmutableList<RestaurantUiModel>>,
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
                title = stringResource(R.string.recommended_restaurant),
                onRetry = {}
            )
        }

        else ->{
            HorizontalTitleButtonSection(
                title = stringResource(R.string.recommended_restaurant),
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
    resource: Resource<ImmutableList<VoucherUiModel>>,
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
            ErrorListState(stringResource(R.string.today_deals)) { }
        }

        else ->{
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(LocalCustomColors.current.cardBackground)
                .padding(vertical = 24.dp)
            ) {
                HorizontalTitleButtonSection(
                    title= stringResource(R.string.today_deals),
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
    resource: Resource<ImmutableList<MenuUiModel>>,
    onFavoriteClick: (MenuUiModel) -> Unit,
    onCartClick :(String) -> Unit,
) {
    val items = resource.data.orEmpty()
    when {
        resource.isLoading -> {
            ShimmerGridMenuListPlaceholder()
        }

        resource.errorMessage != null || items.isEmpty() -> {
            ErrorListState(
                title = stringResource(R.string.suggested_menu_for_you),
                onRetry = {}
            )
        }

        else -> {
            GridMenuListSection(
                title = stringResource(R.string.suggested_menu_for_you),
                menuItems = items,
                onFavoriteClick = onFavoriteClick,
                onAddToCartClick = onCartClick
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun HomeLightPreview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme(darkTheme = true){
        HomeContent(
            snackHostState = snackHostState,
            uiState = HomeUiState(
                userName = "Atifa",
                addressName = "Guest",
                allCategories = Resource(data = RestaurantPreviewData.categoriesUiModel),
                recommendedRestaurants = Resource(data = RestaurantPreviewData.restaurantUiList),
                nearbyRestaurants = Resource(data = RestaurantPreviewData.restaurantUiList),
                //todayVouchers = Resource(data = VoucherData.voucherUiModel),
                recommendedMenus = Resource(data = MenuPreviewData.menuUiList),
                suggestedMenus = Resource(data = MenuPreviewData.menuUiList),
            ),
            onRefresh = {},
            onNavigateToDetail = {},
            onNavigateToCategory = {_,_,_ ->},
            onNavigateToSearch = {},
            onNavigateToVoucher = {},
            onNavigateToNearbyRestaurant = {},
            onNavigateToRecommended = {},
            onFavoriteClick = {},
            onCartClick = {}
        )
    }
}