package com.example.tassty.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tassty.R
import com.example.tassty.component.CartAddItemButton
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DetailScheduleContent
import com.example.tassty.component.DetailTopAppBar
import com.example.tassty.component.FoodGridCard
import com.example.tassty.component.FoodListCard
import com.example.tassty.component.GridRow
import com.example.tassty.component.HorizontalListOrangeSection
import com.example.tassty.component.HorizontalListSection
import com.example.tassty.component.ItemImage
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.RankBadgeIcon
import com.example.tassty.component.RestaurantCloseStatus
import com.example.tassty.component.RestaurantInfoCard
import com.example.tassty.component.ReviewCard
import com.example.tassty.component.TitleListHeader
import com.example.tassty.component.VoucherCard
import com.example.tassty.menus
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.model.RestaurantStatusResult
import com.example.tassty.restaurants
import com.example.tassty.reviews
import com.example.tassty.toCleanRupiahFormat
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import kotlin.collections.chunked

val menuChunks = menus.chunked(2)

@Composable
fun RestaurantDetailScreen(
    restaurant: Restaurant,
    status : RestaurantStatusResult
) {
    var query by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var showScheduleModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        RestaurantDetailContent(
            restaurant = restaurant,
            status = status,
            onShowSearch = { showSearch = true },
            onShowSchedule = { showScheduleModal = true },
            onFavoriteClick = { isFavorite = true }
        )

        CustomBottomSheet(
            visible = showScheduleModal,
            onDismiss = { showScheduleModal = false }
        ) {
            DetailScheduleContent()
        }

        CustomBottomSheet(
            visible = isFavorite,
            dismissOnClickOutside = false,
            onDismiss = { isFavorite = false }
        ) {
            ModalStatusContent(
                title = "Saved to my favorite restaurants!",
                subtitle = "Lorem ipsum dolor sit amet, consectetur \nadipiscing elit, sed do eiusmod.",
                buttonTitle ="Confirm",
                onClick = { isFavorite = false }
            ){
                Image(
                    painter = painterResource(id = R.drawable.success),
                    contentDescription = "Success Icon",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }

    if (showSearch) {
        DetailSearchScreen(
            query = query,
            onQueryChange = { query = it },
            onClose = { showSearch = false }
        )
    }

}

@Composable
fun RestaurantDetailContent(
    restaurant: Restaurant,
    status : RestaurantStatusResult,
    onFavoriteClick : () -> Unit,
    onShowSchedule : () -> Unit,
    onShowSearch : () -> Unit,
) {

    Scaffold (
        containerColor = Neutral10,
        bottomBar = {
            ShoppingCartBottomBar(
                itemCount = 4,
                totalPrice = 25000,
                onCartClick = onShowSchedule
            )
        }
    ){ padding->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize().background(Neutral10),
        ) {
            // 1. Header Section (Single Item)
            item {
                HeaderDetail(
                    restaurant = restaurant,
                    status = status,
                    onShowSearch = onShowSearch,
                    onShowSchedule = onShowSchedule,
                    onFavoriteClick = onFavoriteClick
                )
            }

            // 2. Divider Section (Single Item)
            item {
                Spacer(Modifier.height(90.dp))
                HorizontalDivider(color = Neutral20) // Tambahkan warna untuk konsistensi
                Spacer(Modifier.height(32.dp))
            }

            // 3. Our Best Seller (Single Item - Horizontal LazyRow inside)
            item {
                HorizontalListSection(
                    headerText = "Our best seller",
                    onSeeAllClick = {}
                ) {
                    items(restaurant.menus) { item ->
                        FoodGridCard(
                            menu = item,
                            status = status.status,
                            isFirstItem = false,
                            isWishlist = false
                        )
                    }
                }
            }

            // 4. Reviews Section (Single Item - Horizontal LazyRow inside)
            item {
                Spacer(Modifier.height(32.dp))
                Box(
                    Modifier.fillMaxWidth().background(Neutral20)
                        .padding(top = 20.dp, bottom = 24.dp)
                ) {
                    HorizontalListSection(
                        headerText = "What people say about us",
                        onSeeAllClick = {}
                    ) {
                        // Gunakan key untuk ReviewCard jika ReviewCard di-render ulang
                        items(reviews, key = { it.userName }) { item ->
                            ReviewCard(review = item)
                        }
                    }
                }
            }

            // 5. Recommended Menu Header (Single Item)
            item {
                Spacer(Modifier.height(32.dp))
                Text(
                    text = "Our recommended menu",
                    color = Neutral100,
                    style = LocalCustomTypography.current.h5Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(12.dp)) // Spacing antara header dan grid
            }

            // 6. Recommended Menu Grid (Multiple Items - Peningkatan Performa di sini)
            // Kita iterasi baris (Row) dari grid, bukan item individual.
            items(menuChunks) { rowItems ->
                GridRow(rowItems,status.status)
                Spacer(Modifier.height(12.dp))
            }

            // 7. List Menu Divider (Single Item)
            item {
                Spacer(Modifier.height(32.dp))
                HorizontalDivider(color = Neutral20)
                Spacer(Modifier.height(32.dp))
            }

            item {
                TitleListHeader(
                    data = menus.size,
                    text = "All menus"
                )
                Spacer(Modifier.height(12.dp))
            }
            // 8. Full Menu List (Multiple Items)
            // Pastikan Anda memiliki key untuk performa optimal di LazyColumn
            items(menus, key = { it.id }) { menu ->
                Column(Modifier.padding(horizontal = 24.dp)) {
                    FoodListCard(menu,status.status, false, false)
                    Spacer(Modifier.height(8.dp))
                }
            }

            // Final Padding
            item {
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun HeaderDetail(
    restaurant: Restaurant,
    status : RestaurantStatusResult,
    onFavoriteClick: () -> Unit,
    onShowSearch : () -> Unit,
    onShowSchedule : () -> Unit
){
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val boxHeight = screenHeight * 0.60f
    val cardFraction = 0.55f
    val cardHeight = boxHeight * cardFraction

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
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
    ) {
        ItemImage(
            imageUrl = restaurant.imageUrl,
            name= "detail header image",
            status = status.status,
            modifier = Modifier
                .fillMaxWidth()
                .height(386.dp)
                .align(Alignment.TopCenter)
        )

        // black background at image
        Spacer(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.05f))
        )

        DetailTopAppBar(
            onShowSearch = onShowSearch,onBackClick = {},
            onFavoriteClick = onFavoriteClick, onShareClick = {}
        )

        Box(modifier = Modifier
            .background(Neutral10.copy(alpha = 0.9f))
                .fillMaxWidth()
                .height(cardHeight)
                .align(Alignment.BottomCenter)
        ) {
            Column(modifier = Modifier.fillMaxSize()
                .padding(top = 36.dp, bottom = 24.dp)
            ) {
                Column (modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
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
                        text = "Bakery, Coffee, Croissant",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }

                Spacer(Modifier.height(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 24.dp, end = 24.dp)
                ) {
                    items(1) {
                        RestaurantInfoCard(
                            restaurant = restaurant,
                            onReviewsClick = {},
                            onLocationClick = {},
                            onScheduleClick = onShowSchedule
                        )
                    }
                }
            }


            if(status.status == RestaurantStatus.CLOSED) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-24).dp)
                        .padding(horizontal = 24.dp)
                ) {
                    RestaurantCloseStatus(statusMessage = status.message)
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(y = 60.dp)
            ) {
                HorizontalListOrangeSection(
                    data = 4,
                    headerText = "vouchers",
                    onSeeAllClick = {}
                ) {
                    items(4) {
                        VoucherCard(status = status.status)
                    }
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
        Modifier.padding(top=24.dp,
        bottom = 36.dp,start=24.dp,end=24.dp)
        .background(Neutral10).clickable(onClick = onCartClick)
    ) {
        CartAddItemButton(
            totalPrice = totalPrice,
            itemCount = itemCount
        )
    }
}


@Preview
@Composable
fun DetailClosePreview() {
    RestaurantDetailScreen(
        restaurant = restaurants[0],
        status = RestaurantStatusResult(RestaurantStatus.OPEN,""),
    )
}

@Preview
@Composable
fun DetailOpenPreview() {
    RestaurantDetailScreen(
        restaurant = restaurants[0],
        status = RestaurantStatusResult(RestaurantStatus.CLOSED,"Open at 08.00 today"),
    )
}

