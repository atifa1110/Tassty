package com.example.tassty.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tassty.R
import com.example.tassty.categories
import com.example.tassty.component.CategoryCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyMapBox
import com.example.tassty.component.FoodGridCard
import com.example.tassty.component.GridMenuListSection
import com.example.tassty.component.HeaderListTitleButton
import com.example.tassty.component.HorizontalTitleButtonSection
import com.example.tassty.component.RestaurantGridCard
import com.example.tassty.component.SearchBarHomeSection
import com.example.tassty.component.VoucherLargeCard
import com.example.tassty.menus
import com.example.tassty.model.Category
import com.example.tassty.model.Menu
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange900
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.ui.model.RestaurantStatus
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.LoadingRowState
import com.example.tassty.screen.search.Resource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeState.collectAsStateWithLifecycle()

    HomeContent(uiState = uiState)
}

@Composable
fun HomeContent(
    uiState: HomeUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral10),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(375.dp)
                        .drawBehind {
                            drawRect(
                                brush = Brush.radialGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color(0xFF737373),
                                        0.57f to Color(0xFF3E3E3E),
                                        0.78f to Color(0xFF1F1E1E)
                                    ),
                                    center = center, // Gunakan center yang disediakan oleh DrawScope
                                    radius = 1900f,
                                )
                            )
                        }
                )
                Column {
                    TopAppBarSection()
                    Spacer(modifier = Modifier.height(16.dp))
                    HeaderSection()
                    Spacer(modifier = Modifier.height(24.dp))
                    BannerSection()
                }
            }
        }
        // konten bawah
        item {
            CategorySection(categoryItems = categories)
            Divider32()
        }

        item {
            RecommendationSection(menuItems = menus, status = RestaurantStatus.OPEN)
            Divider32()
        }
//        item {RestaurantNearby()}
        item {RecommendationRestaurant(resource = uiState.recommendedRestaurants)}
        item {TodayDeal()}
        item {SuggestedMenu(menuItems = menus,status = RestaurantStatus.OPEN) }
    }
}

@Composable
fun TopAppBarSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        // Kiri (profile + home)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }

        // Title (center)
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = Neutral10.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
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

        // Kanan (notif button)
        IconButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.CenterEnd)
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

// --- Komponen Header ---
@Composable
fun HeaderSection() {
    Column(modifier = Modifier.fillMaxWidth()
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
            value= "",
            onValueChange = {}
        )
    }
}

// --- Komponen Banner ---
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
    categoryItems : List<Category>
){
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items (items = categoryItems, key = { it.id }) { category ->
            CategoryCard(category = category)
        }
    }
}

@Composable
fun RecommendationSection(
    menuItems : List<Menu>,
    status: RestaurantStatus
) {
    HorizontalTitleButtonSection(
        title = "Recommended for you",
        onClick = {}
    ) {
        itemsIndexed(
            items = menuItems,
            key = { index, menu -> menu.id }
        ) { index, menu ->
            FoodGridCard(
                menu = menu,
                status = status,
                isFirstItem = index == 0,
                onFavoriteClick = {},
                onAddToCart = {}
            )
        }
    }
}

@Composable
fun RestaurantNearby() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListTitleButton(
            title = "Restos Nearby you",
            titleColor = Neutral100,
            onClick = {},
            modifier = Modifier.padding(24.dp)
        )
        EmptyMapBox()
    }
}

@Composable
fun RecommendationRestaurant(
    resource : Resource<List<RestaurantUiModel>>,
) {
    val items = resource.data.orEmpty()

    when{
        resource.isLoading -> {
            LoadingRowState()
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
fun TodayDeal(){
    Column(modifier = Modifier.padding(vertical = 24.dp)
        .fillMaxWidth().background(Neutral20),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalTitleButtonSection(
            title= "Today's deals",
            onClick = {}
        ) {
            items(count= 4) {
                VoucherLargeCard()
            }
        }
    }
}

@Composable
fun SuggestedMenu(
    menuItems: List<Menu>,
    status : RestaurantStatus
) {
    GridMenuListSection(
        title = "Suggested menu for you!",
        menuItems = menuItems,
        status = status,
        onFavoriteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}