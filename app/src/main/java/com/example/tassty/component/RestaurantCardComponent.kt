package com.example.tassty.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.OperationalDay
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.restaurantItem
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun RestaurantSmallListCard (
    restaurant: Restaurant,
    status: RestaurantStatus
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral10),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RestaurantImageBox(
                restaurant = restaurantItem,
                status = status,
                modifier = Modifier.size(100.dp,80.dp)
            )

            RestaurantSmallListCardContent(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantLargeListCard(
    restaurant: Restaurant,
    status: RestaurantStatus
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RestaurantImageBox(
                restaurant = restaurantItem,
                status = status,
                modifier = Modifier.size(100.dp,112.dp)
            )

            RestaurantLargeListCardContent(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantTinyGridCard (
    restaurant: Restaurant,
    status: RestaurantStatus,
){
    Card(
        modifier = Modifier.width(140.dp), // Adjust width as needed
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RestaurantImageBox(
                restaurant = restaurant,
                status = status,
                modifier = Modifier.size(124.dp,120.dp)
            )

            RestaurantTinyGridCardContent(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantGridCard (
    restaurant : Restaurant,
    status: RestaurantStatus,
){
    Card(
        modifier = Modifier.width(156.dp), // Adjust width as needed
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RestaurantImageBox(
                restaurant = restaurant,
                status = status,
                modifier = Modifier.size(140.dp,100.dp)
            )

            RestaurantGridCardContent(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantLargeGridCard (
    restaurant : Restaurant,
    status: RestaurantStatus
){
    Card(
        modifier = Modifier.width(196.dp), // Adjust width as needed
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RestaurantImageBox(
                restaurant = restaurant,
                status = status,
                modifier = Modifier.size(180.dp,120.dp)
            )

            RestaurantLargeGridCardContent(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantCloseStatus(
    statusMessage: String
){
    Box(
        modifier = Modifier
            .background(
                Pink500,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.clock),
                    tint = Neutral10,
                    contentDescription = "time open"
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.closed),
                    color = Color.White,
                    style = LocalCustomTypography.current.bodySmallSemiBold
                )
            }

            Text(
                text = statusMessage,
                color = Color.White,
                style = LocalCustomTypography.current.bodySmallMedium
            )
        }
    }
}

@Composable
fun RestaurantInfoCard(
    restaurant: Restaurant,
    onLocationClick:() -> Unit,
    onReviewsClick:() -> Unit,
    onScheduleClick:() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)){
                // Icon Verified
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.shield_check),
                        contentDescription = stringResource(R.string.verified),
                        tint = Green500,
                        modifier = Modifier.size(18.dp)
                    )

                    // Text "Verified"
                    Text(
                        text = stringResource(R.string.verified),
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }
                Text(
                    text = stringResource(R.string.restaurant),
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = Neutral70
                )
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = stringResource(R.string.rating),
                        tint = Orange500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = restaurant.rating.toString(),
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                    Text(
                        text = "(200+)",
                        style = LocalCustomTypography.current.h8Regular,
                        color = Neutral70
                    )
                }

                SeeInfoButton(text = "See reviews", onClick = onReviewsClick)
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "distance",
                        tint = Pink500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${restaurant.distance} km • ${restaurant.deliveryTime} min",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }

                SeeInfoButton(text = "See location", onClick = onLocationClick)
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "operational hour",
                        tint = Pink500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "10.00 - 20.00",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }

                SeeInfoButton(text = "See schedule", onClick = onScheduleClick)
            }
        }
    }
}

@Composable
fun RestaurantShortInfoCard(
    onScheduleClick:() -> Unit,
    onReviewsClick:() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (verticalArrangement = Arrangement.spacedBy(4.dp)){
                // Icon Verified
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.shield_check),
                        contentDescription = "Verified",
                        tint = Green500,
                        modifier = Modifier.size(18.dp)
                    )

                    // Text "Verified"
                    Text(
                        text = "Verified",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }
                Text(
                    text = "Restaurant",
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = Neutral70
                )
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "Rating",
                        tint = Orange500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "4.8",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                    Text(
                        text = "(200+)",
                        style = LocalCustomTypography.current.h8Regular,
                        color = Neutral70
                    )
                }

                SeeInfoButton(text = "See reviews", onClick = onReviewsClick)
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "Distance",
                        tint = Pink500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "10.00 - 20.00",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }

                SeeInfoButton(text = "See schedule", onClick = onScheduleClick)
            }
        }
    }
}

@Composable
fun RestaurantMenuInfoCard(
    onReviewsClick:() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral20
        )
    ) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "Rating",
                        tint = Orange500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "4.8",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                    Text(
                        text = "(200+)",
                        style = LocalCustomTypography.current.h8Regular,
                        color = Neutral70
                    )
                }

                SeeInfoButton(text = "See reviews", onClick = onReviewsClick)
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rating
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = "delivery",
                        tint = Orange500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Free",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }

                Text(
                    text = "Delivery",
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = Neutral70
                )
            }

            VerticalDivider(Modifier.height(24.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = "estimation time",
                        tint = Blue500,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "10-25 min",
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                }

                Text(
                    text = "Estimation time",
                    style = LocalCustomTypography.current.bodyXtraSmallMedium,
                    color = Neutral70
                )
            }
        }
    }
}

@Composable
fun RestaurantOperationalCard(day: OperationalDay) {
    val isToday = day.isToday

    // Warna background dan ikon jika hari ini
    val bgColor = if (isToday) Neutral10 else Color.Transparent
    val iconColor = if (isToday) Color(0xFF7251F8) else Neutral70.copy(0.2f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Kiri: Icon dan Hari
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.calendar), // Placeholder Calendar/Clock
                contentDescription = "Day Icon",
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = day.day,
                style = if (isToday) LocalCustomTypography.current.h6Bold else LocalCustomTypography.current.h6Regular,
                color = if(isToday) Neutral100 else Neutral70
            )
        }

        // Kanan: Jam Operasional
        Text(
            text = day.hours,
            style = if (isToday) LocalCustomTypography.current.bodySmallBold else LocalCustomTypography.current.bodySmallMedium,
            color = if(isToday) Neutral100 else Neutral70
        )
    }
}

@Composable
fun SeeInfoButton(
    text : String,
    onClick:() -> Unit
){
    Row (
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ){
        Text(
            text = text,
            style= LocalCustomTypography.current.bodyXtraSmallMedium,
            color = Orange500
        )
        Icon(
            painter = painterResource(R.drawable.arrow_left_up),
            contentDescription ="",
            tint = Orange500,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Preview
@Composable
fun PreviewRestaurantInfoCard() {
    RestaurantInfoCard(
        restaurant = restaurantItem,
        onReviewsClick = {},
        onLocationClick = {},
        onScheduleClick = {}
    )
}


@Preview(showBackground = true)
@Composable
fun RestaurantListCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RestaurantSmallListCard(restaurant = restaurantItem, status = RestaurantStatus.OPEN)
        RestaurantLargeListCard(restaurant = restaurantItem, status = RestaurantStatus.OPEN)
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantGridCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            RestaurantTinyGridCard(restaurant = restaurantItem,status = RestaurantStatus.OPEN)
            RestaurantGridCard(restaurant = restaurantItem, status = RestaurantStatus.OPEN)
        }
        RestaurantLargeGridCard(restaurant = restaurantItem,status = RestaurantStatus.OPEN)
    }
}