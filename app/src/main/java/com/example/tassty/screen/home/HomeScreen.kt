package com.example.tassty.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.tassty.component.SearchBarHomeSection
import com.example.tassty.component.VoucherLargeCard
import com.example.tassty.menus
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
import com.example.tassty.component.ListMapBox
import com.example.tassty.restaurantUiModel
import com.example.tassty.screen.search.Resource
import kotlinx.coroutines.launch
import kotlin.collections.orEmpty
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.component.CommonImage
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerGridMenuListPlaceholder
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.component.ShimmerRestaurantGridCard
import com.example.tassty.component.ShimmerVoucherLargeCard
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.Orange500

private val TOP_APP_BAR_HEIGHT = 70.dp

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(uiState = uiState, onRefresh = {viewModel.onPullToRefresh()})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()

    val listState = rememberLazyListState()
    val colorChangeThreshold = 80.dp
    val thresholdPx = with(LocalDensity.current) { colorChangeThreshold.toPx() }

    val scrolledPastThreshold by remember {
        derivedStateOf {
            val currentScrollOffset = if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                thresholdPx + 1f
            }
            currentScrollOffset > thresholdPx
        }
    }
    val topBarColor = if (scrolledPastThreshold) Orange500 else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize().background(Color.Transparent)

    ) {
        TopAppBarSection(
            modifier = Modifier.background(topBarColor).zIndex(2f)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Neutral10)
                .pullToRefresh(
                    state = refreshState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        scope.launch { onRefresh() }
                    }
                )
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(460.dp)
                            .offset(y = -TOP_APP_BAR_HEIGHT)
                            .drawBehind {
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colorStops = arrayOf(
                                            0.0f to Color(0xFF737373),
                                            0.57f to Color(0xFF3E3E3E),
                                            0.78f to Color(0xFF1F1E1E)
                                        ),
                                        center = center,
                                        radius = 1900f,
                                    )
                                )
                            }
                    )
                    Column (modifier = Modifier.offset(y = TOP_APP_BAR_HEIGHT)){
                        Spacer(modifier = Modifier.height(16.dp))
                        HeaderSection(onClick = {})
                        Spacer(modifier = Modifier.height(24.dp))
                        BannerSection()
                    }
                }
            }
            item {
                Spacer(Modifier.height(32.dp))
                CategorySection(resource = uiState.allCategories)
                Divider32()
            }
            item {
                RecommendationSection(resource = uiState.recommendedMenus)
                Divider32()
            }
            item {
                RestaurantNearby(resource = uiState.nearbyRestaurants)
                Spacer(Modifier.height(32.dp))
            }
            item {
                RecommendationRestaurant(resource = uiState.recommendedRestaurants)
                Spacer(Modifier.height(32.dp))
            }
            item {
                TodayDeal(resource = uiState.todayVouchers)
                Spacer(Modifier.height(32.dp))
            }
            item {
                SuggestedMenu(resource = uiState.suggestedMenus)
                Spacer(Modifier.height(32.dp))
            }
        }

        PullToRefreshDefaults.Indicator(
            state = refreshState,
            isRefreshing = uiState.isRefreshing,
            modifier = Modifier.align(Alignment.TopCenter)
                .padding(top = TOP_APP_BAR_HEIGHT)
        )
    }
}

@Composable
fun TopAppBarSection(
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
            CommonImage(
                imageUrl = "https://avatar.iran.liara.run/public",
                name = "",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
            )

            Row (modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center){
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
                        text = "Rafiq's Home",
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

            // Kanan (notif button)
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

// --- Header Section ---
@Composable
fun HeaderSection(
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)) {
        Text(
            text = "Hi Rafiq Daniel,",
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
                text = "👋", // Emoji
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SearchBarHomeSection(
            modifier = Modifier.clickable(onClick = onClick),
            value= "",
            onValueChange = {}
        )
    }
}

// ---- Banner Section ---
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
                        onClick = { /* Handle button click */ },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange900
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Get Now", color = Color.White, style = LocalCustomTypography.current.bodySmallMedium)
                            Icon(
                                painter = painterResource(R.drawable.arrow_left_up),
                                contentDescription = "arrow left up"
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
    resource: Resource<List<CategoryUiModel>>
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
                items (items = items, key = { it.category.id }) { category ->
                    CategoryCard(category = category)
                }
            }
        }
    }
}

@Composable
fun RecommendationSection(
    resource : Resource<List<MenuUiModel>>
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
                    key = { index, menu -> menu.menu.id }
                ) { index, menu ->
                    FoodGridCard(
                        menu = menu,
                        onFavoriteClick = {},
                        onAddToCart = {}
                    )
                }
            }
        }
    }
}

@Composable
fun RestaurantNearby(
    resource: Resource<List<RestaurantUiModel>>
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
                    titleColor = Neutral100,
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                ListMapBox(restaurant = items)
            }
        }
    }
}

@Composable
fun RecommendationRestaurant(
    resource : Resource<List<RestaurantUiModel>>,
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
                onClick = {}
            ) {
                itemsIndexed(
                    items = items,
                    key = { index, item -> item.restaurant.id }
                ) { index, restaurant ->
                    RestaurantGridCard(
                        restaurant = restaurant
                    )
                }
            }
        }
    }
}

@Composable
fun TodayDeal(
    resource: Resource<List<VoucherUiModel>>
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
                .background(Neutral20)
                .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalTitleButtonSection(
                    title= "Today's deals",
                    onClick = {}
                ) {
                    items(items = items, key = {it.voucher.id}) {
                        VoucherLargeCard(voucher = it)
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestedMenu(
    resource: Resource<List<MenuUiModel>>
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
                onFavoriteClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeContent(
        uiState = HomeUiState(
            allCategories = Resource(data = emptyList()),
            recommendedRestaurants = Resource(data = restaurantUiModel),
            nearbyRestaurants = Resource(data= restaurantUiModel),
            recommendedMenus = Resource(data = menus),
            suggestedMenus = Resource(data = menus),
            todayVouchers = Resource(data = emptyList())
        ),
        onRefresh = {}
    )
}