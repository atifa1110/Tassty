package com.example.tassty.screen.setuplocation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.Divider32
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.LocationSetUpCard
import com.example.tassty.component.SetupTopAppBar
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.addresses

@Composable
fun SetupLocationRoute(
    selectedCuisines: List<String>,
    onBackClick:() -> Unit,
    onNavigateToComplete:() -> Unit,
    viewModel: SetUpLocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event){
                is SetUpLocationEvent.OnNavigateToComplete -> {
                    onNavigateToComplete()
                }
                is SetUpLocationEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SetupLocationScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSkipClick = {},
        onSubmitClick= {viewModel.onSubmitAddress(selectedCuisines)},
        onSetLocationClick = {viewModel.onSetLocationClick(true)}
    )

    SetLocationModal(
        isVisible = uiState.isModalVisible,
        onDismiss =  {viewModel.onSetLocationClick(false)},
        uiState = uiState,
        onAddressNameChanged = viewModel::onAddressNameChange,
        onTypeSelected = viewModel::onTypeSelected,
        onSelectLocation = viewModel::onMapClicked,
        onLandmarkDetailChanged = viewModel::onLandmarkDetailChange,
        onSaveAddress = viewModel::onSaveLocation
    )
}

@Composable
fun SetupLocationScreen(
    uiState: SetUpLocationUiState,
    onBackClick:() -> Unit,
    onSkipClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onSetLocationClick: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            SetupTopAppBar(
                currentStep = 2,
                totalStep = 2,
                onBackClick = onBackClick,
                onSkipClick = onSkipClick
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth() .imePadding()
                    .background(LocalCustomColors.current.modalBackgroundFrame)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ButtonComponent(
                    modifier = Modifier.width(220.dp),
                    enabled = true,
                    labelResId = R.string.submit,
                    onClick = onSubmitClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderTitleScreen(title = stringResource(R.string.setup_your_delivery_location))

                Text(
                    text = stringResource(R.string.primary_delivery_location),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text,
                    textAlign = TextAlign.Start
                )
            }

            Divider32()

            if (uiState.isLoading) {
                LoadingScreen()
            } else if (uiState.errorMessage != null) {
                Text("No location available")
            } else {
                Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.primary_address),
                            style = LocalCustomTypography.current.h5Bold,
                            color = LocalCustomColors.current.headerText
                        )

                        Text(
                            modifier= Modifier.clickable(onClick = onSetLocationClick),
                            text = stringResource(R.string.set_location),
                            style = LocalCustomTypography.current.bodyMediumMedium,
                            color = Orange500
                        )
                    }

                    LocationSetUpCard(
                        selectedLatLng = uiState.selectedLatLng,
                        address = uiState.userAddress
                    )
                }
            }
        }
    }
}



//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun SetupLocationLightPreview() {
//    TasstyTheme(darkTheme = false) {
//        SetupLocationScreen(
//            uiState = SetUpLocationUiState(
//                userAddress = addresses[0]
//            ),
//            onBackClick = {},
//            onSkipClick = {},
//            onSubmitClick = {},
//            onSetLocationClick = {}
//        )
//    }
//}

//@Preview(showBackground = true, name = "Dark mode")
//@Composable
//fun SetupLocationDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        SetupLocationScreen(
//            uiState = SetUpLocationUiState(
//                userAddress = addresses[0]
//            ),
//            onBackClick = {},
//            onSkipClick = {},
//            onSubmitClick = {},
//            onSetLocationClick = {}
//        )
//    }
//}