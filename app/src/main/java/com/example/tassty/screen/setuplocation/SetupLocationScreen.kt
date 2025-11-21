package com.example.tassty.screen.setuplocation

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.model.AddressType
import com.example.core.ui.model.UserAddressUiModel
import com.example.tassty.R
import com.example.tassty.component.ButtonSmallComponent
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.LocationBox
import com.example.tassty.component.SetupTopAppBar
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun SetupLocationRoute(
    selectedCuisines: List<String>,
    onBackClick:() -> Unit,
    onNavigateToComplete:() -> Unit,
    viewModel: SetUpLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event){
                is SetUpLocationEvent.OnNavigateToComplete -> {onNavigateToComplete()}
                is SetUpLocationEvent.ShowToast -> {

                }
            }
        }
    }

    SetupLocationScreen(
        uiState = uiState,
        onBackClick= onBackClick,
        onSkipClick={},
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
        containerColor = Neutral10,
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
                    .fillMaxWidth()
                    .imePadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ButtonSmallComponent(
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
                .background(Neutral10)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.setup_your_delivery_location),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )

                Text(
                    text = stringResource(R.string.primary_delivery_location),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider(color = Neutral30)

            if (uiState.isLoading) {
                LoadingScreen()
            } else if (uiState.errorMessage != null) {
                Text("No location available")
            } else {
                Column(
                    modifier = Modifier
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
                            color = Neutral100
                        )

                        Text(
                            modifier= Modifier.clickable(onClick = onSetLocationClick),
                            text = stringResource(R.string.set_location),
                            style = LocalCustomTypography.current.bodyMediumMedium,
                            color = Orange500
                        )
                    }
                    LocationBox(
                        resource = uiState.userAddress,
                        onCardClick = {}
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewSetupAccountScreen() {
    SetupLocationScreen (
        uiState = SetUpLocationUiState(
            UserAddressUiModel("","",0.0,0.0,
            "","", AddressType.NONE)
        ),
        onBackClick = {},
        onSkipClick={},
        onSubmitClick = {},
        onSetLocationClick = {}
    )
}