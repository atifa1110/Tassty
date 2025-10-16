package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun FoodImageWithFloating(
    menu : MenuUiModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageCircle(menu)

        FloatingAddButton(
            iconSize = 12.dp,
            actionSize = 24.dp,
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {}
        )
    }
}

@Composable
fun FoodRoundImageWithOverlays(
    menu: MenuUiModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageRound(menu = menu)

        if (menu.menu.rank!=null) {
            RankBadge(
                horizontal = 8.dp, vertical = 4.dp,
                modifier = Modifier.width(40.dp)
                    .padding(start = 3.dp, top = 3.dp)
                    .align(Alignment.TopStart)
            )
        }
    }
}

@Composable
fun FoodCircleImageWithOverlays(
    menu: MenuUiModel,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageCircle(menu = menu)

        if (menu.menu.rank!=null) {
            RankBadge(
                horizontal = 8.dp, vertical = 4.dp,
                modifier = Modifier.width(40.dp)
                    .align(Alignment.TopStart)
            )
        }

        FavoriteButton(
            modifier = Modifier.align(Alignment.TopEnd),
            isWishlist = menu.isWishlist,
            onClick = onFavoriteClick
        )
    }
}

@Composable
fun FoodRatingAndDistanceRow(
    rating: Double,
    distance: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.star),
            contentDescription = "Rating",
            tint = Orange500
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = rating.toString(),
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            painter = painterResource(R.drawable.location),
            contentDescription = "Distance",
            tint = Pink500
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = distance,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun FoodRatingAndCityRow(
    city: String,
    rating: Double,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.star),
            contentDescription = "Rating",
            tint = Orange500
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = rating.toString(),
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Neutral70
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(R.drawable.location),
            contentDescription = "Distance",
            tint = Pink500
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = city,
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Neutral70
        )
    }
}

@Composable
fun FoodTinyGridCardContent(
    menu: MenuUiModel,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = menu.menu.name,
            style = LocalCustomTypography.current.h7Bold,
            color = Neutral100
        )

        FoodPriceTinyText(
            price = menu.formatPrice
        )
    }
}

@Composable
fun FoodNameGridCardContent(
    menu: MenuUiModel,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = menu.menu.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
        FloatingAddButton(
            iconSize = 16.dp,
            actionSize = 30.dp,
            onClick = {}
        )
    }
}

@Composable
fun FoodGridCardContent(
    menu: MenuUiModel,
    onAddToCart:() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = menu.menu.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        Spacer(modifier = Modifier.height(4.dp))
        FoodRatingAndDistanceRow(
            rating = menu.formatRating,
            distance = menu.formattedDistance
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodPriceText(price= menu.formatPrice, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
                onClick = onAddToCart
            )
        }
    }
}

@Composable
fun FoodListCardContent(
    menu: MenuUiModel,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(84.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = menu.menu.name,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Text(
                    text = menu.menu.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
            }
            FavoriteButton(
                isWishlist = menu.isWishlist,
                onClick = onFavoriteClick
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FoodPriceText(price = menu.formatPrice, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
                onClick = {}
            )
        }

    }

}

@Composable
fun FoodWideListCardContent(
    menu: MenuUiModel,
    isWishlist : Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Text(
                    text = menu.menu.name,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = menu.menu.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = LocalCustomTypography.current.h7Bold.toSpanStyle().copy(color = Neutral70)) {
                            append("${menu.menu.soldCount}")
                        }
                        withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = Neutral70)) {
                            append("sold")
                        }
                    },
                )
            }
            FavoriteButton(
                isWishlist = isWishlist,
                onClick = onFavoriteClick
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FoodPriceText(price = menu.formatPrice, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
                onClick = {}
            )
        }

    }

}

@Composable
fun FoodOrderListCardContent(
    menu: MenuUiModel,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = menu.menu.name,
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Text(
                text = menu.formatSoldCount,
                style = LocalCustomTypography.current.h5Regular,
                color = Neutral100
            )
        }

        NotesText("extra sides")

        NotesBoxButton(title ="Give Rating",onClick = {})
    }
}