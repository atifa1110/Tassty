package com.example.tassty.screen.setuplocation

import android.content.Context
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.core.domain.model.AddressType
import com.example.tassty.R
import com.example.tassty.component.AddressTypeSection
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.Divider32
import com.example.tassty.component.MapSearchTopAppBar
import com.example.tassty.component.TextSection
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.roundToInt

@Composable
fun SetLocationModal(
    isVisible: Boolean,
    uiState: SetUpLocationUiState,
    onAddressNameChanged: (String) -> Unit,
    onLandmarkDetailChanged: (String) -> Unit,
    onTypeSelected: (AddressType) -> Unit,
    onSelectLocation:(Context, LatLng) -> Unit,
    onDismiss: () -> Unit,
    onSaveAddress: () -> Unit,
) {
    val density = LocalDensity.current
    val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    var offsetY by remember { mutableStateOf(0f) }
    var initialPanelHeight by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        val newOffset = offsetY + delta
        offsetY = newOffset.coerceIn(0f, screenHeight - initialPanelHeight)
    }
    val context = LocalContext.current
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val cameraPositionState = rememberCameraPositionState {
                    val lat = uiState.userAddress.latitude
                    val lng = uiState.userAddress.longitude

                    position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 16f)
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = true
                    ),
                    onMapClick = { latLng ->
                        onSelectLocation(context,latLng)
                    }
                ) {
                   uiState.selectedLatLng?.let { pos ->
                       Marker(
                           state = MarkerState(position = pos),
                           title = "Selected Location"
                       )
                    }
                }

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
                            .background(Neutral10)
                            .padding(top = 24.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header (Set location and dash)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Set location",
                                style = LocalCustomTypography.current.h3Bold,
                                color = Neutral100
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.location),
                                    contentDescription = "location"
                                )
                                Text(
                                    text = uiState.tempFullAddress,
                                    style = LocalCustomTypography.current.bodySmallMedium,
                                    color = Neutral70
                                )
                            }
                        }

                        Divider32()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            TextSection(
                                label = stringResource(R.string.address_name),
                                placeholder = "Enter name",
                                text = uiState.tempAddressName,
                                textError = "",
                                onTextChanged = onAddressNameChanged
                            )

                            TextSection(
                                label = stringResource(R.string.landmark_detail),
                                placeholder = stringResource(R.string.enter_landmark),
                                text = uiState.tempLandmark,
                                textError = "",
                                leadingIcon = R.drawable.flag,
                                onTextChanged = onLandmarkDetailChanged
                            )

                            AddressTypeSection(
                                addressType = uiState.tempAddressType,
                                onTypeSelected = onTypeSelected
                            )

                            ButtonComponent(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.buttonEnable,
                                labelResId = R.string.save_address,
                                onClick = onSaveAddress
                            )
                        }
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

                // Top bar
                MapSearchTopAppBar(onBackClick = onDismiss, onSearchClick = {})
            }
        }

}

@Composable
fun AddressTypeRow(
    selectedType: AddressType,
    onTypeSelected: (AddressType) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AddressType.entries
            .filter { it != AddressType.NONE }
            .forEach { type ->
                AddressTypeChip(
                    text = type.displayName,
                    icon = R.drawable.person,
                    isSelected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )
            }
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
            .clickable {
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
fun PreviewLocationModal(){
    SetLocationModal(
        isVisible = true,
        onDismiss = {},
        uiState = SetUpLocationUiState(
            tempFullAddress = "Margonda No.5",
            tempLandmark = "Deket Margo",
            tempAddressType = AddressType.PERSONAL,
            tempAddressName = "My Home",
            buttonEnable = true
        ),
        onAddressNameChanged = {},
        onTypeSelected = {},
        onLandmarkDetailChanged = {},
        onSelectLocation = {_,_->},
        onSaveAddress = {}
    )
}