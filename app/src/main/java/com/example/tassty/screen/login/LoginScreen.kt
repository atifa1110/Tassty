package com.example.tassty.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.tassty.R
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmailSection
import com.example.tassty.component.FailedIcon
import com.example.tassty.component.HeaderTitleSubtitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.UiText

@Composable
fun LoginRoute(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome :() -> Unit,
    onNavigateToEmailInput: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> {
                    onNavigateToHome()
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLogin,
        onRegisterClick = onNavigateToRegister,
        onForgotClick = onNavigateToEmailInput
    )

    CustomBottomSheet(
        visible = uiState.isBottomSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = stringResource(R.string.login_failed),
            subtitle = uiState.bottomSheetMessage?:"",
            buttonTitle = stringResource(R.string.ok),
            onClick = viewModel::onDismissBottomSheet
        ) {
            FailedIcon()
        }
    }
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged:(String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick:() -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            AuthTopAppBar()
        },
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeaderTitleSubtitleScreen(
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    title = R.string.login_to_your_account,
                    subtitle = R.string.welcome_back_you_ve_been_missed
                )

                EmailSection(
                    email = uiState.email,
                    enabled = !uiState.isLoading,
                    emailError = uiState.emailError,
                    onEmailChanged = onEmailChanged
                )

                PasswordSection(
                    password = uiState.password,
                    enabled = !uiState.isLoading,
                    passwordError = uiState.passwordError,
                    onPasswordChanged = onPasswordChanged
                )

                Text(
                    text = stringResource(R.string.forgot_password),
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onForgotClick,
                            enabled = !uiState.isLoading
                        ),
                    textAlign = TextAlign.Right,
                    color = Orange500
                )
                LoadingButtonComponent(
                    modifier = Modifier.padding(top = 8.dp),
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.login,
                    onClick = onLoginClick,
                    isLoading = uiState.isLoading
                )
            }

            Divider32()

            ButtonLogin()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.don_t_have_an_account),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral60
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.register),
                    color = Orange500,
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    modifier = Modifier.clickable(
                        onClick = onRegisterClick,
                        enabled = !uiState.isLoading
                    )
                )
            }
        }
    }
}


//@Preview(name = "Small Phone", device = "spec:width=320dp,height=480dp")
//@Preview(name = "Normal Phone", device = PIXEL_8)
//@Preview(name = "Foldable/Tablet", device = Devices.FOLDABLE)
@Composable
fun LoginLightPreview() {
    TasstyTheme(darkTheme = false) {
        LoginScreen(
            uiState = LoginUiState(
                email = "atifafiorenza24@gmail.com",
                emailError = UiText.StringResource(R.string.email_is_not_valid),
                password = "1234567",
                passwordError = UiText.StringResource(R.string.password_cannot_be_empty),
            ),
            onEmailChanged = { },
            onPasswordChanged = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotClick = {},
        )
        CustomBottomSheet(
            visible = false,
            dismissOnClickOutside = false,
            onDismiss = {}
        ) {
            ModalStatusContent(
                title = "Login Failed!",
                subtitle = "Your username or password\nis incorrect.",
                buttonTitle = "OK",
                onClick = {}
            ) {
                FailedIcon()
            }
        }
    }
}

//@Preview(name = "Small Phone", device = "spec:width=320dp,height=480dp")
//@Preview(name = "Normal Phone", device = PIXEL_8)
//@Preview(name = "Foldable/Tablet", device = Devices.FOLDABLE)
@Composable
fun LoginDarkPreview() {
    TasstyTheme(darkTheme = true) {
        LoginScreen(
            uiState = LoginUiState(
                email = "atifafiorenza24@gmail.com",
                emailError = UiText.StringResource(R.string.email_is_not_valid),
                password = "1234567",
                passwordError = UiText.StringResource(R.string.password_cannot_be_empty),
            ),
            onEmailChanged = { },
            onPasswordChanged = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotClick = {},
        )

        CustomBottomSheet(
            visible = false,
            dismissOnClickOutside = false,
            onDismiss = {}
        ) {
            ModalStatusContent(
                title = "Login Failed!",
                subtitle = "Your username or password\nis incorrect.",
                buttonTitle = "OK",
                onClick = {}
            ) {
                FailedIcon()
            }
        }
    }
}