package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tassty.model.Menu
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.screen.RestaurantContentSection
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70

@Composable
fun HorizontalListOrangeSection(
    modifier: Modifier = Modifier,
    data: Int,
    headerText: String,
    onSeeAllClick: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TitleListHeader(
            data = data,
            text = headerText
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Kita panggil konten yang diserahkan dari luar
            content()
        }
    }
}

@Composable
fun HorizontalListSection(
    modifier: Modifier = Modifier,
    headerText: String,
    onSeeAllClick: () -> Unit,
    // Parameter ini memungkinkan kita untuk memasukkan konten LazyRow yang berbeda
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderText(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = headerText,
            textColor = Neutral100, // Asumsi ini statis atau bisa dijadikan parameter
            textButton = "See All",
            onButtonClick = onSeeAllClick
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Kita panggil konten yang diserahkan dari luar
            content()
        }
    }
}

@Composable
fun HorizontalSubtitleListSection(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onSeeAllClick: () -> Unit,
    // Parameter ini memungkinkan kita untuk memasukkan konten LazyRow yang berbeda
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderSubtitleText(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
            subtitle = subtitle,
            titleColor = Neutral100,
            subtitleColor = Neutral70,
            textButton = "See All",
            onButtonClick = onSeeAllClick
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Kita panggil konten yang diserahkan dari luar
            content()
        }
    }
}

// Menggunakan extension function pada LazyListScope agar bisa memanggil item{} dan items{}
fun LazyListScope.restaurantMenuListBlock(
    headerText: String,
    restaurantItems: List<Restaurant>
) {
    item {
        // Terapkan padding horizontal 24.dp pada judul jika Anda membutuhkannya
        // atau pastikan TitleListHeader menanganinya secara internal
        TitleListHeader(
            data = restaurantItems.size, // Mengambil jumlah resto secara dinamis
            text = headerText,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.id }
    ) { restaurant ->
        RestaurantContentSection(
            restaurant = restaurant,
            status = RestaurantStatus.OPEN
        )
        // Jarak vertikal 12 dp antar setiap blok restoran (sesuai permintaan Anda)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

fun LazyListScope.restaurantVerticalListBlock(
    headerText: String,
    restaurantItems: List<Restaurant>,
    status: RestaurantStatus
) {
    item {
        // Padding horizontal 24.dp diterapkan pada Header
        TitleListHeader(
            data = restaurantItems.size,
            text = headerText,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    items(
        items = restaurantItems,
        key = { it.id }
    ) { restaurant ->

        // Kotak wadah untuk menerapkan padding horizontal 24.dp
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            RestaurantLargeListCard(restaurant = restaurant, status = status)
        }

        // Jarak vertikal 8 dp antar setiap item (sesuai permintaan)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun GridRow(
    rowItems: List<Menu>,
    status: RestaurantStatus
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rowItems.forEachIndexed { itemIndexInRow, item ->
            FoodLargeGridCard(
                menu = item,
                status = status,
                isFirstItem = item.id == 1,
                isWishlist = false,
                modifier = Modifier.weight(1f) // Weight memastikan lebar sama rata
            )
        }

        // Jika hanya 1 item di baris terakhir, tambahkan Spacer kosong
        if (rowItems.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}