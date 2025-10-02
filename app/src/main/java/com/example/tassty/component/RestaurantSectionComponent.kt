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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun RestaurantImageBox(
    restaurant: Restaurant,
    status: RestaurantStatus,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        RestaurantImageRound(
            restaurant = restaurant,
            status = status,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun RestaurantCityAndDistanceText(
    city: String,
    distance: Int
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CityText(city = city)

        Text(
            text = " • ",
            style = LocalCustomTypography.current.bodySmallRegular,
            color = Neutral40
        )

        Text(
            text = "${distance}m",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun RestaurantCityDistanceDeliveryText(
    city: String,
    distance: Int,
    deliveryTime: Int
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CityText(city = city)

        Text(
            text = " • ",
            style = LocalCustomTypography.current.bodySmallRegular,
            color = Neutral40
        )

        Text(
            text = "${distance}m",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )

        Text(
            text = " • ",
            style = LocalCustomTypography.current.bodySmallRegular,
            color = Neutral40
        )

        Text(
            text = "${deliveryTime}min",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun CityText(
    city: String
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(R.drawable.location),
            contentDescription = "Distance",
            tint = Pink500
        )

        Text(
            text = city,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun RatingText(
    rating : Double
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.star),
            contentDescription = "Rating",
            tint = Orange500,
        )
        Text(
            text = rating.toString(),
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun RestaurantCategoryText(
    category: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.category),
            contentDescription = "Rating",
            tint = Green500
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = category,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun RestaurantSmallListCardContent(
    restaurant: Restaurant
){
    Column(modifier = Modifier.height(80.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = restaurant.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        RestaurantCityAndDistanceText(
            city = restaurant.city,
            distance = restaurant.distance
        )

        RatingText(rating = restaurant.rating)
    }
}

@Composable
fun RestaurantLargeListCardContent(
    restaurant: Restaurant
){
    Column(modifier = Modifier.height(112.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = restaurant.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        RestaurantCategoryText(
            category = "Bakery, Coffee, Croissant"
        )

        RestaurantCityAndDistanceText(
            city = restaurant.city,
            distance = restaurant.distance
        )

        RatingText(
            rating = restaurant.rating
        )
    }
}

@Composable
fun RestaurantTinyGridCardContent(
    restaurant: Restaurant
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = restaurant.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RatingText(rating = restaurant.rating)
            CityText(city = restaurant.city)
        }
    }
}

@Composable
fun RestaurantGridCardContent(
    restaurant: Restaurant
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = restaurant.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        RestaurantCityAndDistanceText(
            city = restaurant.city,
            distance = restaurant.distance
        )

        RatingText(rating = restaurant.rating)
    }
}

@Composable
fun RestaurantLargeGridCardContent(
    restaurant: Restaurant
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = restaurant.name,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        RestaurantCityDistanceDeliveryText(
            city = restaurant.city,
            distance = restaurant.distance,
            deliveryTime = restaurant.distance
        )

        RatingText(rating = restaurant.rating)
    }
}