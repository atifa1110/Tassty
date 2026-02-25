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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.example.tassty.addresses
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState


@Composable
fun UserAddressBox(
    address: UserAddressUiModel,
    onCardClick:() -> Unit
){
    Card(
        modifier = Modifier
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
            LocationMapBox(address = address)
            Column (
                modifier = Modifier.padding(horizontal=6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = address.formatAddressName,
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
                        text = address.formatFullAddress,
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
                        text = address.formatAddressType,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }
            }
        }
    }
}

@Composable
fun NearbyMapBox(
    restaurant: List<RestaurantUiModel>
) {
    val cameraPositionState = rememberCameraPositionState()

    val mapOptions = remember {
        GoogleMapOptions().liteMode(true)
    }

    LaunchedEffect(restaurant) {
        if (restaurant.isNotEmpty()) {
            val firstLocation = LatLng(
                restaurant.first().locationDetail.latitude,
                restaurant.first().locationDetail.longitude
            )
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(firstLocation, 14f)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp)) // Opsional: agar Map melengkung rapi
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            googleMapOptionsFactory = { mapOptions },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = false,
                zoomGesturesEnabled = false,
                rotationGesturesEnabled = false,
                tiltGesturesEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            // 4. Render markers dengan rememberMarkerState
            restaurant.forEach { resto ->
                val position = remember(resto.id) {
                    LatLng(resto.locationDetail.latitude, resto.locationDetail.longitude)
                }
                Marker(
                    state = rememberMarkerState(position = position),
                    title = resto.name
                )
            }
        }
    }
}


@Composable
fun LocationMapBox(
    address: UserAddressUiModel
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(address.latitude, address.longitude) {
        if (address.latitude!= 0.0 && address.longitude != 0.0) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(address.latitude, address.longitude),
                    15f
                ),
                durationMs = 1000
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

            if (address.latitude != 0.0) {
                Marker(
                    state = MarkerState(position = LatLng(address.latitude, address.longitude)),
                    title = address.fullAddress
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBoxLocation() {
    UserAddressBox(
        address = addresses[0],
        onCardClick = {}
    )
}