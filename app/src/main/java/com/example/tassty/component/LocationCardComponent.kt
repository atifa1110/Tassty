package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.tassty.R
import com.example.tassty.util.addresses
import com.example.tassty.getStaticMapUrl
import com.example.tassty.screen.rating.HeaderIconText
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.Pink600
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.collections.forEach

@Composable
fun NearbyMapBox(
    restaurant: List<RestaurantUiModel>,
){
    val mapOptions = remember {
        GoogleMapOptions().liteMode(true)
    }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(restaurant) {
        if (restaurant.isNotEmpty()) {
            val firstLocation = LatLng(
                restaurant.first().locationDetail.latitude,
                restaurant.first().locationDetail.longitude
            )
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(firstLocation, 15f)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth().background(LocalCustomColors.current.cardBackground)
            .height(200.dp)
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
            restaurant.forEach { resto ->
                key (resto.id) {
                    val position = LatLng(
                        resto.locationDetail.latitude,
                        resto.locationDetail.longitude
                    )

                    val markerState = rememberMarkerState(position = position)

                    Marker(
                        state = markerState,
                        title = resto.name,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                }
            }
        }
    }
}

@Composable
fun LocationSetUpCard(
    selectedLatLng: LatLng?,
    address: UserAddressUiModel?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LocationImage(
                imageUrl = getStaticMapUrl(
                    centerLat = selectedLatLng?.latitude?:0.0,
                    centerLng = selectedLatLng?.longitude?:0.0,
                    showMarker = address != null
                ),
                name = "location address",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
            )

            Column (
                modifier = Modifier.padding(horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = address?.addressName?.ifEmpty { "Address title" } ?: "Address title",
                    style = LocalCustomTypography.current.h5Bold,
                    color = LocalCustomColors.current.headerText
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "location icon"
                    )
                    Text(
                        text = address?.fullAddress?.ifEmpty { "-" } ?: "-",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = LocalCustomColors.current.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.flag),
                        contentDescription = "flag icon"
                    )
                    Text(
                        text = address?.addressType?.displayName?.ifEmpty { "Type" }?:"Type",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = LocalCustomColors.current.text
                    )
                }
            }
        }
    }
}

@Composable
fun LocationCard(
    address: UserAddressUiModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

        CircleImageIcon(
            boxColor = Pink100,
            icon = R.drawable.location,
            iconColor = Pink600,
            iconSize = 16.dp,
            contentDescription = "location icon",
            modifier = Modifier.size(32.dp)
        )


        Column(modifier = Modifier.fillMaxWidth()){
                Text(
                    text = address.addressName,
                    style = LocalCustomTypography.current.h6Bold,
                    color = LocalCustomColors.current.headerText
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = address.fullAddress,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = LocalCustomColors.current.text
                )
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    NotesText("Notes: -")
                    EditButton(
                        title = "Notes",
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun LocationSelectorCard(
    address: UserAddressUiModel,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable{onCheckedChange(address.isSelected)},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width= 1.dp, color = if(address.isSelected) Orange500 else Neutral40),
        colors = CardDefaults.cardColors(
            containerColor = if(address.isSelected) LocalCustomColors.current.selectedOrangeBackground else LocalCustomColors.current.background
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            LocationImage(
                imageUrl = getStaticMapUrl(address.latitude,address.longitude),
                name = address.addressName,
                modifier = Modifier.size(94.dp,104.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f).height(104.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = address.addressName,
                    style = LocalCustomTypography.current.h5Bold,
                    color = LocalCustomColors.current.headerText
                )
                Spacer(Modifier.height(4.dp))
                LocationContent(
                    verticalAlignment = Alignment.Top,
                    icon = R.drawable.location,
                    iconColor = Pink600,
                    value = address.fullAddress
                )
                Spacer(Modifier.height(12.dp))
                LocationContent(
                    icon = R.drawable.person,
                    iconColor = Blue500,
                    value = address.addressType.displayName
                )
            }
            Spacer(Modifier.width(8.dp))
            Checkbox(
                checked = address.isSelected,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(0.dp).size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange500,
                    uncheckedColor = Neutral40
                )
            )

        }
    }
}

@Composable
fun LocationContent(
    icon: Int,
    iconColor: Color,
    value: String,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
){
    Row (verticalAlignment = verticalAlignment){
        Icon(
            painter = painterResource(icon),
            tint = iconColor,
            contentDescription = "location icon",
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = value,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = LocalCustomColors.current.text
        )
    }
}

@Composable
fun LocationLardCard(
    address: UserAddressUiModel,
){
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = {}),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground
        )
    ) {
        Column(modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 12.dp)) {

            LocationImage(
                imageUrl = getStaticMapUrl(address.latitude,address.longitude),
                name = "location address",
                modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(10.dp)),
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircleImageIcon(
                    boxColor = Pink100,
                    icon = R.drawable.location,
                    iconColor = Pink600,
                    iconSize = 16.dp,
                    contentDescription = "location icon",
                    modifier = Modifier.size(32.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = address.addressName,
                        style = LocalCustomTypography.current.h5Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = address.fullAddress,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = LocalCustomColors.current.text
                    )
                    Spacer(Modifier.height(12.dp))
                    LocationContent(
                        icon = R.drawable.person,
                        iconColor = Blue500,
                        value = address.addressType.displayName
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationCard() {
    Column(modifier = Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationSetUpCard(
            selectedLatLng = LatLng(-6.2000, 106.8166),
            address = addresses[0]
        )
        LocationCard(address = addresses[0])
        LocationSelectorCard(
            address = addresses[0],
            onCheckedChange = {}
        )
        //LocationLardCard(addresses[0])
    }
}