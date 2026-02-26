package com.example.tassty.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.EmailSection
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun LoginRoute(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome :() -> Unit,
    onNavigateToResetPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> {
                    onNavigateToHome()
                }
                is LoginEvent.ShowBottomSheet -> {
                    viewModel.setBottomSheetVisible(true)
                }
            }
        }
    }

    LoginScreen(
        isLoading = uiState.isLoading,
        email= uiState.email,
        emailError = uiState.emailError?:"",
        onEmailChanged = { viewModel.onEmailChange(it)},
        password = uiState.password,
        passwordError = uiState.passwordError?:"",
        onPasswordChanged = {viewModel.onPasswordChange(it)},
        onLoginClick = {viewModel.login()},
        onRegisterClick = onNavigateToRegister,
        onForgotClick = onNavigateToResetPassword,
        isButtonEnable = viewModel.isLoginEnabled.collectAsState().value
    )

    CustomBottomSheet(
        visible = uiState.isBottomSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = "Login failed!",
            subtitle = uiState.bottomSheetMessage?:"",
            buttonTitle = "OK",
            onClick = { viewModel.resetLoginInput() }
        ) {
            Image(
                painter = painterResource(id = R.drawable.success),
                contentDescription = "Success Icon",
                modifier = Modifier.size(64.dp)
            )
        }
    }

}
@Composable
fun LoginScreen(
    isLoading: Boolean,
    email:String,
    emailError:String,
    onEmailChanged: (String) -> Unit,
    password:String,
    passwordError:String,
    onPasswordChanged:(String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotClick:() -> Unit,
    isButtonEnable: Boolean,
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            AuthTopAppBar()
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()
            .background(Neutral10),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_to_your_account),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Text(
                    text = stringResource(R.string.welcome_back_you_ve_been_missed),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70

                )
            }


            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmailSection(
                    email = email,
                    emailError = emailError,
                    onEmailChanged = onEmailChanged
                )

                PasswordSection(
                    password = password,
                    passwordError = passwordError,
                    onPasswordChanged = onPasswordChanged
                )

                Text(
                    text = stringResource(R.string.forgot_password),
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = onForgotClick),
                    textAlign = TextAlign.Right,
                    color = Orange500
                )

            }

            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                LoadingButtonComponent(
                    enabled = isButtonEnable,
                    labelResId = R.string.login,
                    onClick = onLoginClick,
                    isLoading = isLoading
                )
            }

            HorizontalDivider(color = Neutral30)

            ButtonLogin()

            // Teks Register
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
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun LoginPreview() {
//    LoginScreen(
//        email= "atifafiorenza24@gmail.com",
//        emailError ="email is empty",
//        onEmailChanged = { },
//        password = "123456",
//        passwordError = "password is empty",
//        onPasswordChanged = {},
//        onLoginClick = {},
//        onRegisterClick = {},
//        onForgotClick = {},
//        isButtonEnable = false,
//        isLoading = false
//    )
//}