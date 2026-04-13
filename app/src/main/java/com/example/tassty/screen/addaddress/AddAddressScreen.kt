package com.example.tassty.screen.addaddress

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.model.AddressType
import com.example.tassty.R
import com.example.tassty.component.AddressTypeSection
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.component.MapSearchTopAppBar
import com.example.tassty.component.TextSection
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AddAddressScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddAddressViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event->
            when(event){
                AddAddressUiEffect.NavigateBack ->{
                    onNavigateBack()
                }
                is AddAddressUiEffect.ShowMessage -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AddAddressContent(
        uiState = uiState,
        cameraEvents = viewModel.cameraEvents,
        onBackClick = onNavigateBack,
        onSearchClick = {},
        onAddressNameChange = viewModel::onAddressNameChange,
        onAddressTypeSelected = viewModel::onTypeSelected,
        onLandmarkChange = viewModel::onLandmarkDetailChange,
        onSaveClick = viewModel::onCreateAddress,
        onMapClicked = viewModel::onMapClicked
    )
}

@Composable
fun AddAddressContent(
    uiState: AddAddressUiState,
    cameraEvents: Flow<LatLng>,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAddressNameChange: (String) -> Unit,
    onLandmarkChange: (String) -> Unit,
    onAddressTypeSelected: (AddressType) -> Unit,
    onMapClicked : (LatLng) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-6.4, 106.8), 10f)
    }
    val markerState = rememberMarkerState()
    var isInitialCenteringDone by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.selectedLatLng) {
        uiState.selectedLatLng?.let { newLatLng ->
            markerState.position = newLatLng
            if (!isInitialCenteringDone) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(newLatLng, 16f)
                isInitialCenteringDone = true
            }
        }
    }

    LaunchedEffect(Unit) {
        cameraEvents.collect { targetLatLng ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(targetLatLng, 16f))
        }
    }

    Scaffold(
        topBar = {
            MapSearchTopAppBar(
                onBackClick = onBackClick,
                onSearchClick = onSearchClick
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false
                    ),
                    onMapClick = { latLng ->
                        onMapClicked(latLng)
                    }
                ) {
                    //uiState.selectedLatLng?.let { pos ->
                        Marker(
                            state = markerState,
                            title = "Selected Location"
                        )
                    //}
                }

                AnimatedVisibility(
                    visible = uiState.isMapLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            LocalCustomColors.current.background.copy(0.3f)
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Green500)
                    }
                }
            }

            AddressBottomSheet(
                uiState = uiState,
                onAddressNameChange = onAddressNameChange,
                onLandmarkChange = onLandmarkChange,
                onAddressTypeSelected = onAddressTypeSelected,
                onSaveClick = onSaveClick
            )

            LoadingOverlay(
                isLoading = uiState.isLoading,
                text = stringResource(R.string.load)
            )
        }
    }
}

@Composable
fun AddressBottomSheet(
    uiState: AddAddressUiState,
    onAddressNameChange: (String) -> Unit,
    onLandmarkChange: (String) -> Unit,
    onAddressTypeSelected: (AddressType) -> Unit,
    onSaveClick: () -> Unit
) {
    val scope = rememberCoroutineScope ()
    val offsetAnim = remember { Animatable(2000f) }
    var panelHeightPx by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.fullAddress, panelHeightPx) {
        if (panelHeightPx > 0f && !isDragging) {
            val isReady = uiState.fullAddress.isNotEmpty() &&
                    uiState.fullAddress != "Choose your location"
            val targetOffset = if (isReady) 0f else panelHeightPx * 0.82f

            offsetAnim.animateTo(
                targetValue = targetOffset,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .onGloballyPositioned {
                    if (panelHeightPx == 0f) panelHeightPx = it.size.height.toFloat()
                }
                .graphicsLayer {
                    translationY = offsetAnim.value
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(LocalCustomColors.current.background)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val newValue = offsetAnim.value + delta
                            offsetAnim.snapTo(newValue.coerceIn(0f, panelHeightPx * 0.82f))
                        }
                    },
                    onDragStarted = { isDragging = true },
                    onDragStopped = { isDragging = false }
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Box(
                modifier = Modifier
                    .size(40.dp, 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Neutral70.copy(alpha = 0.5f))
                    .align(Alignment.CenterHorizontally)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.set_location),
                    style = LocalCustomTypography.current.h3Bold,
                    color = LocalCustomColors.current.headerText
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = uiState.fullAddress,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = LocalCustomColors.current.text
                    )
                }
            }

            TextSection(
                label = stringResource(R.string.address_name),
                placeholder = stringResource(R.string.address_name_placeholder),
                text = uiState.addressName,
                onTextChanged = onAddressNameChange
            )

            TextSection(
                label = stringResource(R.string.landmark_detail),
                placeholder = stringResource(R.string.landmark_placeholder),
                text = uiState.landmark,
                leadingIcon = R.drawable.flag,
                onTextChanged = onLandmarkChange
            )

            AddressTypeSection(
                addressType = uiState.addressType,
                onTypeSelected = onAddressTypeSelected
            )

            ButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isButtonEnabled,
                labelResId = R.string.save_address,
                onClick = onSaveClick
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun AddAddressLightPreview() {
    TasstyTheme {
        AddAddressContent(
            uiState = AddAddressUiState(),
            cameraEvents = flowOf(),
            onAddressNameChange = {},
            onAddressTypeSelected = {},
            onBackClick = {},
            onSearchClick = {},
            onSaveClick = {},
            onLandmarkChange = {},
           onMapClicked = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun AddAddressDarkPreview() {
    TasstyTheme(darkTheme = true) {
        AddAddressContent(
            uiState = AddAddressUiState(),
            cameraEvents = flowOf(),
            onAddressNameChange = {},
            onAddressTypeSelected = {},
            onBackClick = {},
            onSearchClick = {},
            onSaveClick = {},
            onLandmarkChange = {},
            onMapClicked = {}
        )
    }
}