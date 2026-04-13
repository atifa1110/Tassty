package com.example.tassty.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.example.core.domain.model.DisplayStatus
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue100
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun <T : DisplayStatus> StatusItemImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    status: T,
    placeholder: ColorPainter = ColorPainter(Color.LightGray)
) {
    val context = LocalContext.current
    val safeImageUrl = remember(imageUrl) { imageUrl.ifBlank { null } }

    val imageRequest = remember(safeImageUrl, name) {
        ImageRequest.Builder(context)
            .data(safeImageUrl)
            .crossfade(true)
            .size(256)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    val filter = remember(status.isEnabled) {
        if (status.isEnabled) null
        else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        colorFilter = filter
    )
}

//@Composable
//fun CommonImage(
//    modifier : Modifier = Modifier,
//    imageUrl: String,
//    name: String,
//    placeholder: ColorPainter = ColorPainter(Color.LightGray)
//){
//    val context = LocalContext.current
//    val safeImageUrl = remember(imageUrl) { imageUrl.ifBlank { null } }
//
//    val imageRequest = remember(safeImageUrl, name) {
//        ImageRequest.Builder(context)
//            .data(safeImageUrl)
//            .crossfade(true)
//            .size(256)
//            .diskCachePolicy(CachePolicy.ENABLED)
//            .memoryCachePolicy(CachePolicy.ENABLED)
//            .build()
//    }
//
//    AsyncImage(
//        model = imageRequest,
//        contentDescription = name,
//        contentScale = ContentScale.Crop,
//        modifier = modifier,
//        placeholder = placeholder,
//        error = placeholder,
//    )
//}

@Composable
fun CommonImage(
    modifier: Modifier = Modifier,
    imageUrl: Any?,
    name: String,
    placeholder: ColorPainter = ColorPainter(Color.LightGray)
) {
    val context = LocalContext.current
    val safeData = remember(imageUrl) {
        if (imageUrl is String && imageUrl.isBlank()) null else imageUrl
    }

    val imageRequest = remember(safeData) {
        ImageRequest.Builder(context)
            .data(safeData)
            .crossfade(true)
            .size(256)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = name,
        contentScale = ContentScale.Crop,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
    )
}


@Composable
fun HomeProfile(
    imageUrl: String,
    name: String
){
    ProfileImage(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape),
        imageUrl = imageUrl,
        name = name,
        style = LocalCustomTypography.current.bodyMediumBold,
        background= Neutral10,
        textColor = Neutral70
    )
}

@Composable
fun MessageProfile(
    imageUrl: String?,
    name: String,
){
    ProfileImage(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
        imageUrl = imageUrl,
        name = name,
        style = LocalCustomTypography.current.bodySmallBold,
        background = Orange500,
        textColor = Neutral10
    )
}

@Composable
fun ChatProfile(
    imageUrl: String?,
    name: String,
){
    ProfileImage(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape),
        imageUrl = imageUrl,
        name = name,
        style = LocalCustomTypography.current.bodyMediumBold,
        background = Orange500,
        textColor = Neutral10
    )
}


@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String,
    style: TextStyle,
    background: Color = Orange500,
    textColor : Color = Neutral10
) {
    if (imageUrl.isNullOrEmpty()) {
        Box(
            modifier = modifier
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.take(1).uppercase(),
                color = textColor,
                style = style
            )
        }
    }else{
        CommonImage(
            imageUrl = imageUrl,
            name = name,
            modifier = modifier
        )
    }
}

@Composable
fun LocationImage(
    modifier : Modifier = Modifier,
    imageUrl: String,
    name: String
){
    CommonImage(
        modifier = modifier,
        imageUrl = imageUrl,
        name = name,
    )
}

@Composable
fun CategoryImageCircle(
    categoryName: String,
    imageUrl: String
){
    CommonImage(
        imageUrl = imageUrl,
        name = categoryName,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FoodImageCircle(
    menu : MenuUiModel,
    modifier: Modifier = Modifier
){
    StatusItemImage(
        imageUrl = menu.imageUrl,
        name = menu.name,
        status = menu.menuStatus,
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
    )
}

@Composable
fun FoodImageRound(
    menu : MenuUiModel,
    modifier: Modifier = Modifier
){
    StatusItemImage(
        imageUrl = menu.imageUrl,
        name = menu.name,
        status = menu.menuStatus,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10))
    )
}

@Composable
fun FoodImageRound(
    collection: CollectionMenuUiModel,
    modifier: Modifier = Modifier
){
    StatusItemImage(
        imageUrl = collection.imageUrl,
        name = collection.name,
        status = MenuStatus.AVAILABLE,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10))
    )
}

@Composable
fun CollectionImageRound(
    collection: CollectionUiModel,
    modifier: Modifier = Modifier
){
    CommonImage(
        imageUrl = collection.imageUrl,
        name = collection.title,
        modifier = modifier.clip(RoundedCornerShape(10))
    )
}

@Composable
fun RestaurantImageRound(
    restaurant: RestaurantUiModel,
    modifier: Modifier = Modifier
){
    StatusItemImage(
        imageUrl = restaurant.imageUrl,
        name = restaurant.name,
        status = restaurant.statusResult.status,
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
fun LogoImage() {
    val configuration = LocalConfiguration.current
    val logoSize = if (configuration.screenHeightDp < 600) 48.dp else 62.dp

    ImageIcon(
        image = R.drawable.logo,
        contentDescription = "App Logo",
        modifier = Modifier.size(logoSize)
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
                        Color(0xFFEE8E1E),
                        Color(0xFFF03F94)
                    )

                    RestaurantStatus.CLOSED -> listOf(
                        Color(0xFFCBCBCB),
                        Color(0xFF908F8F)
                    )

                    RestaurantStatus.OFFDAY -> listOf(
                        Color(0xFFCBCBCB),
                        Color(0xFF908F8F)
                    )
                }

            ),
            shape = RoundedCornerShape(12.dp)
        )
        .clip(RoundedCornerShape(12.dp)),
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
    modifier: Modifier = Modifier,
    boxColor: Color,
    contentDescription: String,
    icon: Any,
    iconSize : Dp,
    iconColor: Color = Color.Unspecified,
){
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(boxColor),
        contentAlignment = Alignment.Center
    ) {
        val painter = when (icon) {
            is androidx.compose.ui.graphics.vector.ImageVector -> rememberVectorPainter(icon)
            is Int -> painterResource(icon)
            else -> return
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
            modifier = Modifier
                .offset(10.dp)
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
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
        }

        DangerIcon(
            modifier = Modifier
                .offset(-(10).dp)
                .zIndex(2f)
        )
    }
}

@Composable
fun NotificationIcon(){
    CircleModalIcon()
}

@Composable
fun SuccessIcon(){
    CircleModalIcon(
        icon = R.drawable.check,
        innerColor = Orange500,
        outerColor = Color(0xFFEE8E1E).copy(0.24f)
    )
}

@Composable
fun FailedIcon(){
    CircleModalIcon(
        icon = R.drawable.x,
        innerColor = Pink500,
        outerColor = Pink100
    )
}

@Composable
fun DangerIcon(
    modifier: Modifier = Modifier
){
    CircleModalIcon(
        modifier = modifier,
        icon = R.drawable.exclamation,
        innerColor = Pink500,
        outerColor = Pink100
    )
}

@Composable
fun CircleModalIcon(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.bell,
    innerColor: Color = Blue500,
    outerColor: Color = Blue100,
    iconTint: Color = Neutral10,
    innerSize: Dp = 48.dp,
    outerSize: Dp = 64.dp
) {
    Box(
        modifier = modifier.size(outerSize),
        contentAlignment = Alignment.Center
    ) {
        Surface (
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = outerColor.copy(0.8f),
            border = null,
            tonalElevation = 2.dp
        ) {}

        Surface(
            modifier = Modifier.size(innerSize),
            shape = CircleShape,
            color = innerColor,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "notification",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CustomMarkerDesign(
    @DrawableRes iconRes: Int,
    borderColor: Color = Pink500
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(borderColor, CircleShape)
                .padding(3.dp)
                .background(LocalCustomColors.current.background, CircleShape)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            CircleImageIcon(
                boxColor = Color.Transparent,
                iconSize = 24.dp,
                icon = iconRes,
                iconColor = borderColor,
                contentDescription = ""
            )
        }

        Canvas(
            modifier = Modifier
                .size(16.dp, 12.dp)
                .offset(y = (-2).dp)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(path, color = borderColor)
        }
    }
}

@Preview
@Composable
fun Preview(){
    TasstyTheme (darkTheme = true){
        CustomMarkerDesign(
            R.drawable.store
        )
    }
}
