package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.tassty.R
import com.example.tassty.hashUrl
import com.example.tassty.model.Category
import com.example.tassty.model.CollectionUiItem
import com.example.tassty.model.Menu
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Pink500

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.15)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(250L * 1024 * 1024)
                    .build()
            }
            .crossfade(true)
            .respectCacheHeaders(false)
            .build()
    }
}

@Composable
fun ItemImage(
    imageUrl: String,
    name: String,
    status: RestaurantStatus,
    placeholder: ColorPainter = ColorPainter(Color.LightGray),
    modifier : Modifier = Modifier
){
    val context = LocalContext.current
    val imageLoader = rememberImageLoader()
    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .diskCacheKey("image_${hashUrl(imageUrl)}")    // cache unik per kategori
        .memoryCacheKey("image_${hashUrl(imageUrl)}")  // supaya gak reload kalau sama
        .diskCachePolicy(CachePolicy.ENABLED)    // simpan ke disk cache
        .memoryCachePolicy(CachePolicy.ENABLED)  // simpan ke mem cache
        .crossfade(true)
        .size(256)// masih bisa override per gambar
        .build()

    // 🔹 ColorMatrix buat grayscale
    val grayscaleFilter = ColorFilter.colorMatrix(
        ColorMatrix().apply { setToSaturation(0f) }
    )

    val filter = when (status) {
        RestaurantStatus.OPEN -> null
        RestaurantStatus.CLOSED -> grayscaleFilter
        RestaurantStatus.OFFDAY -> grayscaleFilter
    }

    AsyncImage(
        model = imageRequest,
        imageLoader = imageLoader,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        colorFilter = filter
    )
}

@Composable
fun CommonImage(
    imageUrl: String,
    name: String,
    placeholder: ColorPainter = ColorPainter(Color.LightGray),
    modifier : Modifier = Modifier
){
    val context = LocalContext.current
    val imageLoader = rememberImageLoader()
    // 🔹 bikin request khusus per gambar
    val imageRequest = ImageRequest.Builder(context)
        .data(imageUrl)
        .diskCacheKey("image_${hashUrl(imageUrl)}")    // cache unik per kategori
        .memoryCacheKey("image_${hashUrl(imageUrl)}")  // supaya gak reload kalau sama
        .diskCachePolicy(CachePolicy.ENABLED)    // simpan ke disk cache
        .memoryCachePolicy(CachePolicy.ENABLED)  // simpan ke mem cache
        .crossfade(true)
        .size(256)// masih bisa override per gambar
        .build()

    AsyncImage(
        model = imageRequest,
        imageLoader = imageLoader,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
    )
}

@Composable
fun CategoryImageCircle(
    category: Category
){
    CommonImage(
        imageUrl = category.imageUrl,
        name = category.name,
        modifier = Modifier.size(24.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FoodImageCircle(
    menu : Menu,
    status: RestaurantStatus,
    modifier: Modifier = Modifier
){
    ItemImage(
        imageUrl = menu.imageUrl,
        name = menu.name,
        status = status,
        modifier = modifier.fillMaxSize()
            .clip(CircleShape)
    )
}

@Composable
fun FoodImageRound(
    menu : Menu,
    status: RestaurantStatus,
    modifier: Modifier = Modifier
){
    ItemImage(
        imageUrl = menu.imageUrl,
        name = menu.name,
        status = status,
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(10))
    )
}

@Composable
fun CollectionImageRound(
    collection: CollectionUiItem,
    modifier: Modifier = Modifier
){
    CommonImage(
        imageUrl = collection.thumbnailUrl,
        name = collection.name,
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(10))
    )
}

@Composable
fun RestaurantImageRound(
    restaurant: Restaurant,
    status: RestaurantStatus,
    modifier: Modifier = Modifier
){
    ItemImage(
        imageUrl = restaurant.imageUrl,
        name = restaurant.name,
        status = status,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun ImageIcon(
    image : Int,
    contentDescription : String,
    modifier: Modifier
){
    Image(
        painter = painterResource(id = image),
        contentDescription = contentDescription,
        modifier = modifier
    )
}

@Composable
fun BestSellerIcon(){
    ImageIcon(
        image = R.drawable.best_seller,
        contentDescription = "best seller icon",
        modifier = Modifier.size(44.dp)
    )
}

@Composable
fun LogoImage(){
    ImageIcon(image = R.drawable.logo,
        contentDescription = "App Logo",
        modifier = Modifier.size(62.dp))
}

@Composable
fun SuccessImage(){
    ImageIcon(
        image = R.drawable.success,
        contentDescription = "Success Icon",
        modifier = Modifier.size(64.dp)
    )
}

@Composable
fun PromoIcon(
    status : RestaurantStatus
){
    Box(modifier = Modifier
        .size(44.dp)
        .background(
            brush = Brush.verticalGradient(
                colors = when (status) {
                    RestaurantStatus.OPEN -> listOf(
                        Color(0xFFEE8E1E), // Orange
                        Color(0xFFF03F94)  // Pink
                    )
                    RestaurantStatus.CLOSED -> listOf(
                        Color(0xFFCBCBCB),
                        Color(0xFF908F8F)
                    )
                    RestaurantStatus.OFFDAY ->  listOf(
                        Color(0xFFCBCBCB),
                        Color(0xFF908F8F)
                    )
                }

            ),
            shape = RoundedCornerShape(12.dp)
        ).
        clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier.size(26.dp),
            painter = painterResource(R.drawable.promo),
            contentDescription = null,
            tint = Neutral10
        )
    }

}

@Composable
fun CircleImageIcon(
    boxColor: Color,
    contentDescription: String,
    icon: Any,
    iconSize : Dp,
    iconColor: Color,
    modifier: Modifier
){
    Box(
        modifier = modifier.clip(CircleShape)
            .background(boxColor),
        contentAlignment = Alignment.Center
    ) {
        val painter = when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> rememberVectorPainter(icon)
            is Int -> painterResource(icon)
            else -> return // Handle case not supported
        }
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun OverlapImage(
    imageUrl: String
){

    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier.offset(10.dp)
                .zIndex(1f)
                .size(64.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFFEE8E1E).copy(0.24f))
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE1E1E1))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                CommonImage(
                    imageUrl =imageUrl,
                    name ="",
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                )
            }
        }

        Box(
            modifier = Modifier.offset(-(10).dp)
                .zIndex(2f)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFF03F94).copy(0.24f))
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Pink500)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.exclamation),
                    contentDescription = "",
                    tint = Neutral10,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}