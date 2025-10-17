package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.R
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
@Composable
fun BoxLocation(
    modifier: Modifier = Modifier,
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
            EmptyMapBox()
            Column (
                modifier = Modifier.padding(horizontal=6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Address title",
                    style = LocalCustomTypography.current.h5Bold,
                    color = Neutral100
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "location"
                    )
                    Text(
                        text = "-",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.flag),
                        contentDescription = "flag"
                    )
                    Text(
                        text = "Type",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Neutral70
                    )
                }
            }
        }
    }
}

@Composable
fun MarkerView(imageUrl: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(30.dp).clip(RoundedCornerShape(12.dp))
        )
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
fun EmptyMapBox() {
    val isPreview = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        if (isPreview) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.LightGray)
            )
        } else {
            // Kode GoogleMap untuk perangkat nyata
            val jakarta = LatLng(-6.2088, 106.8456)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(jakarta, 12f)
            }
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                ),
                uiSettings = MapUiSettings(
                    compassEnabled = false,
                    mapToolbarEnabled = false,
                    myLocationButtonEnabled = false,
                )
            ) {
                // Tidak ada Marker atau Composable lain
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoxLocation() {
    BoxLocation {  }
}