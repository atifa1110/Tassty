package com.example.tassty.screen.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme

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
            title = "Login Failed!",
            subtitle = uiState.bottomSheetMessage?:"",
            buttonTitle = "OK",
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
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderTitleScreen(title = stringResource(R.string.login_to_your_account),)
                Text(
                    text = stringResource(R.string.welcome_back_you_ve_been_missed),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text

                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmailSection(
                    email = uiState.email,
                    enabled = uiState.isTextEditable,
                    emailError = uiState.emailError?:"",
                    onEmailChanged = onEmailChanged
                )

                PasswordSection(
                    password = uiState.password,
                    enabled = uiState.isTextEditable,
                    passwordError = uiState.passwordError?:"",
                    onPasswordChanged = onPasswordChanged
                )

                Text(
                    text = stringResource(R.string.forgot_password),
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = onForgotClick, enabled = uiState.isTextEditable),
                    textAlign = TextAlign.Right,
                    color = Orange500
                )

            }

            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                LoadingButtonComponent(
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.don_t_have_an_account),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral60
                )
                Text(
                    text = " Register",
                    color = Orange500,
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    modifier = Modifier.clickable(onClick = onRegisterClick,
                        enabled = uiState.isTextEditable)
                )
            }
        }
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun LoginLightPreview() {
//    TasstyTheme(darkTheme = false) {
//        LoginScreen(
//            uiState = LoginUiState(
//                email = "atifafiorenza24@gmail.com",
//                //emailError = "email empty",
//                password = "123456",
//                //passwordError = "password empty",
//                isTextEditable = true,
//                isButtonEnabled = false
//            ),
//            onEmailChanged = { },
//            onPasswordChanged = {},
//            onLoginClick = {},
//            onRegisterClick = {},
//            onForgotClick = {},
//        )
//        CustomBottomSheet(
//            visible = false,
//            dismissOnClickOutside = false,
//            onDismiss = {}
//        ) {
//            ModalStatusContent(
//                title = "Login Failed!",
//                subtitle = "Your username or password\nis incorrect.",
//                buttonTitle = "OK",
//                onClick = {}
//            ) {
//                FailedIcon()
//            }
//        }
//    }
//}
//
//@Preview(
//    showBackground = true,
//    name = "Dark Mode",
//)
//@Composable
//fun LoginDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        LoginScreen(
//            uiState = LoginUiState(
//                email = "atifafiorenza24@gmail.com",
//                //emailError = "email empty",
//                password = "",
//                //passwordError = "password empty",
//                isTextEditable = true,
//                isButtonEnabled = false
//            ),
//            onEmailChanged = { },
//            onPasswordChanged = {},
//            onLoginClick = {},
//            onRegisterClick = {},
//            onForgotClick = {},
//        )
//
//        CustomBottomSheet(
//            visible = false,
//            dismissOnClickOutside = false,
//            onDismiss = {}
//        ) {
//            ModalStatusContent(
//                title = "Login Failed!",
//                subtitle = "Your username or password\nis incorrect.",
//                buttonTitle = "OK",
//                onClick = {}
//            ) {
//                FailedIcon()
//            }
//        }
//    }
//}