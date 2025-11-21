package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.res.painterResource
import com.example.core.domain.model.AddressType
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.R
import com.example.core.ui.model.UserAddressUiModel
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


@Composable
fun LocationBox(
    modifier: Modifier = Modifier,
    resource: UserAddressUiModel,
    onCardClick:() -> Unit
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onCardClick()
            } ,
        colors = CardDefaults.cardColors(
            containerColor = Neutral20,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        )
    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 14.dp, start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            LocationMapBox(
                fullAddress = resource.fullAddress,
                latitude = resource.latitude,
                longitude = resource.longitude
            )
            Column (
                modifier = Modifier.padding(horizontal=6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = resource.formatAddressName,
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "location"
                    )
                    Text(
                        text = resource.formatFullAddress,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.flag),
                        contentDescription = "flag"
                    )
                    Text(
                        text = resource.formatAddressType,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }
            }
        }
    }
}

@Composable
fun ListMapBox(
    restaurant: List<RestaurantUiModel>
) {
    val cameraPositionState = rememberCameraPositionState {
        if (restaurant.isNotEmpty()) {
            position = CameraPosition.fromLatLngZoom(
                LatLng(restaurant.first().lat, restaurant.first().long), 14f
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            restaurant.forEach { resto ->
                Marker(
                    state = MarkerState(position = LatLng(resto.lat, resto.long)),
                    title = resto.restaurant.name,
                    snippet = resto.restaurant.locationDetails.fullAddress // Optional
                )
            }
        }
    }
}


@Composable
fun LocationMapBox(
    latitude : Double,
    longitude: Double,
    fullAddress: String,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(latitude, longitude),
            13f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (fullAddress.isNotEmpty()) {
                Marker(
                    state = MarkerState(position = LatLng(latitude,
                        longitude)),
                    title = fullAddress,
                    snippet = fullAddress
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBoxLocation() {
    LocationBox(
        resource = UserAddressUiModel(
            "","",0.0,0.0,
            "", "", AddressType.NONE
        ),
        onCardClick = {}
    )
}