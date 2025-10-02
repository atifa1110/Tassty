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
import com.example.tassty.R
import com.example.tassty.model.Menu
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun FoodImageWithFloating(
    menu : Menu,
    status : RestaurantStatus,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageCircle(menu,status)

        FloatingAddButton(
            iconSize = 12.dp,
            actionSize = 24.dp,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun FoodRoundImageWithOverlays(
    menu: Menu,
    status : RestaurantStatus,
    isFirstItem: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageRound(menu = menu,status = status)

        if (isFirstItem) {
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
    menu: Menu,
    status : RestaurantStatus,
    isFirstItem: Boolean,
    isWishlist: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FoodImageCircle(menu = menu,status = status)

        if (isFirstItem) {
            RankBadge(
                horizontal = 8.dp, vertical = 4.dp,
                modifier = Modifier.width(40.dp)
                    .align(Alignment.TopStart)
            )
        }

        FavoriteButton(
            modifier = Modifier.align(Alignment.TopEnd),
            isWishlist = isWishlist,
            onClick = onFavoriteClick
        )
    }
}

@Composable
fun FoodRatingAndDistanceRow(
    rating: Double,
    distance: Int,
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
            text = "$distance m",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun FoodTinyGridCardContent(
    menu: Menu,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = menu.name,
            style = LocalCustomTypography.current.h7Bold,
            color = Neutral100
        )

        FoodPriceTinyText(
            price = menu.price
        )
    }
}

@Composable
fun FoodGridCardContent(
    menu: Menu,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = menu.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        Spacer(modifier = Modifier.height(4.dp))
        FoodRatingAndDistanceRow(
            rating = menu.rating,
            distance = menu.distance
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodPriceText(price= menu.price, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
            )
        }
    }
}

@Composable
fun FoodListCardContent(
    menu: Menu,
    isWishlist : Boolean,
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
                    text = menu.name,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Text(
                    text = menu.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
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
            FoodPriceText(price = menu.price, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
            )
        }

    }

}

@Composable
fun FoodWideListCardContent(
    menu: Menu,
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
                    text = menu.name,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = menu.description,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(style = LocalCustomTypography.current.h7Bold.toSpanStyle().copy(color = Neutral70)) {
                            append("${menu.sold}x")
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
            FoodPriceText(price = menu.price, color = Orange500)
            FloatingAddButton(
                iconSize = 16.dp,
                actionSize = 30.dp,
            )
        }

    }

}

@Composable
fun FoodOrderListCardContent(
    menu: Menu,
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
                text = menu.name,
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Text(
                text = "154x",
                style = LocalCustomTypography.current.h5Regular,
                color = Neutral100
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                withStyle(
                    style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle()
                        .copy(color = Neutral100)
                ) {
                    append("Notes : ")
                }
                withStyle(
                    style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle()
                        .copy(color = Neutral70)
                ) {
                    append("extra sides")
                }
            },
        )

        GiveRatingBoxButton(onClick = {})
    }
}