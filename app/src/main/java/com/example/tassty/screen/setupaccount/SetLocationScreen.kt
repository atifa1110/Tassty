package com.example.tassty.screen.setupaccount

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.MapSearchTopAppBar
import com.example.tassty.component.TextComponent
import com.example.tassty.component.TextComponentNoIcon
import com.example.tassty.getCurrentLocation
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SetLocationScreen(
    onLocationSelected: (String, Double, Double) -> Unit
) {
    // UI state
    var location by remember { mutableStateOf("") }
    var addressName by remember { mutableStateOf("") }
    var landmarkDetail by remember { mutableStateOf("") }
    val isPreview = LocalInspectionMode.current
    var selectedType by remember { mutableStateOf("Personal") }

    // Map and Location State
    var currentLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var locationAddress by remember { mutableStateOf("Getting your location...") }
    val markerState = rememberMarkerState(position = currentLocation)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 15f)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Dragging state
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    var offsetY by remember { mutableStateOf(0f) }
    var initialPanelHeight by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        val newOffset = offsetY + delta
        offsetY = newOffset.coerceIn(0f, screenHeight - initialPanelHeight)
    }

    // Panggil lokasi saat pertama kali Composable diluncurkan
    LaunchedEffect(Unit) {
        getCurrentLocation(context) { latLng ->
            currentLocation = latLng
            locationAddress = "Your Current Location"
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Lapisan 1: Peta (Latar Belakang)
        if (isPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = true
                ),
                onMapClick = { latLng ->
                    currentLocation = latLng
                    markerState.position = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)

                    coroutineScope.launch {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        try {
                            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                            val addressText = addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
                            location = addressText
                            onLocationSelected(addressText, latLng.latitude, latLng.longitude)
                        } catch (e: Exception) {
                            Log.e("Geocoding", "Error fetching address", e)
                            locationAddress = "Address not found"
                        }
                    }
                }
            ) {
                Marker(
                    state = markerState,
                    title = "Selected Location"
                )
            }
        }

        // Lapisan 3: Panel yang dapat digerakkan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, offsetY.roundToInt()) }
                .padding(top = 16.dp)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Vertical,
                )
                .onGloballyPositioned { coordinates ->
                    if (initialPanelHeight == 0f) {
                        initialPanelHeight = coordinates.size.height.toFloat()
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Neutral20)
                    .padding(top = 24.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Header (Set location and dash)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Set location",
                        style = LocalCustomTypography.current.h3Bold,
                        color = Neutral100
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.location),
                            contentDescription = "location"
                        )
                        Text(
                            text = location,
                            style = LocalCustomTypography.current.bodySmallMedium,
                            color = Neutral70
                        )
                    }
                }

                HorizontalDivider(color = Neutral30)

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            text = stringResource(R.string.address_name),
                            style = LocalCustomTypography.current.h5Bold,
                        )

                        TextComponentNoIcon(
                            text = addressName,
                            textError = "",
                            placeholder = "Enter name",
                            onTextChanged = {
                                addressName=it
                            }
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            text = stringResource(R.string.landmark_detail),
                            style = LocalCustomTypography.current.h5Bold,
                        )

                        TextComponent(
                            text = landmarkDetail,
                            textError = "",
                            placeholder = stringResource(R.string.enter_landmark),
                            leadingIcon = R.drawable.flag,
                            onTextChanged = {
                                landmarkDetail=it
                            }
                        )
                    }


                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            text = "Address type",
                            style = LocalCustomTypography.current.h5Bold,
                        )

                        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
                            AddressTypeChip(
                                text = "Personal",
                                icon = R.drawable.person,
                                isSelected = selectedType == "Personal",
                                onClick = { selectedType = "Personal" }
                            )

                            AddressTypeChip(
                                text = "Business",
                                icon = R.drawable.person,
                                isSelected = selectedType == "Business",
                                onClick = { selectedType = "Business" }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    ButtonComponent(
                        enabled = true,
                        labelResId = R.string.save_address,
                        onClick = {}
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(y = (-5).dp)
                    .size(width = 60.dp, height = 9.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(Neutral10)
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 25.dp, height = 1.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(Neutral70)
                )
            }
        }

        // Lapisan 4: Panel yang dapat digerakkan
        MapSearchTopAppBar(onBackClick = {}, onSearchClick = {})

    }
}

@Composable
fun AddressTypeChip(
    text: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) Orange50 else Neutral10
    val contentColor = if (isSelected) Orange500 else Neutral70
    val borderColor = if (isSelected) Orange500 else Neutral30

    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable{
                onClick()
            } ,
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
    ) {
        Row(modifier = Modifier
            .padding(vertical = 11.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),// or other icon
                contentDescription = null,
                tint = contentColor
            )
            Spacer(Modifier.width(8.dp))
            Text(text,
                style = LocalCustomTypography.current.bodyMediumMedium,
                color = contentColor)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLocationScreen(){
    SetLocationScreen(
        onLocationSelected = {_,_,_->}
    )
}