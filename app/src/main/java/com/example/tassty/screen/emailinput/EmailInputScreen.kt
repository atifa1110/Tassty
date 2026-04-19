package com.example.tassty.screen.emailinput

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.VerificationType
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmailSection
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.UiText

@Composable
fun EmailInputScreen(
    onNavigateBack:()-> Unit,
    onNavigateToVerify: (VerificationType, Int, Int) -> Unit,
    viewModel: EmailInputViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event->
            when(event){
                is EmailInputEvent.NavigateToVerifyReset -> {
                    onNavigateToVerify(VerificationType.FORGOT_PASSWORD,event.expireIn,event.resendAvailableIn)
                }
                is EmailInputEvent.ShowMessage ->{
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    EmailInputContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onSendOtpClick = viewModel::onSendOtpToEmail,
        onBackClick = onNavigateBack
    )
}

@Composable
fun EmailInputContent(
    uiState: EmailInputUiState,
    onEmailChange: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Column(modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(24.dp)
            ) {
                LoadingButtonComponent(
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.send_otp,
                    onClick = onSendOtpClick,
                    isLoading = uiState.isLoading
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderTitleScreen(title = stringResource(R.string.reset_password))

                Text(
                    text = stringResource(R.string.order_reset_password),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text,
                    textAlign = TextAlign.Start
                )
            }

            Divider32()

            Column (modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)){
                EmailSection(
                    email = uiState.email,
                    emailError = uiState.emailError,
                    onEmailChanged = onEmailChange,
                    enabled = !uiState.isLoading
                )
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun EmailInputLightPreview() {
    TasstyTheme {
        EmailInputContent(
            uiState = EmailInputUiState(
                email = "user@gmail.com",
                isLoading = false,
                isButtonEnabled = false
            ),
            onEmailChange = {},
            onSendOtpClick = {},
            onBackClick = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun EmailInputDarkPreview() {
    TasstyTheme(darkTheme = true){
        EmailInputContent(
            uiState = EmailInputUiState(
                email = "user@gmail.com",
                isButtonEnabled = true
            ),
            onEmailChange = {},
            onSendOtpClick = {},
            onBackClick = {}
        )
    }
}