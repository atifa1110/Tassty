package com.example.tassty.screen.register

import android.content.res.Configuration
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.tassty.VerificationType
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmailSection
import com.example.tassty.component.FailedIcon
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.component.SuccessIcon
import com.example.tassty.component.TermsOfServiceCheckbox
import com.example.tassty.component.TextSection
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
fun RegisterRoute(
    onNavigateToLogin:()  -> Unit,
    onNavigateToVerify: (VerificationType, Int, Int) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.NavigateToVerify -> {
                    onNavigateToVerify(VerificationType.REGISTRATION,event.expireIn,event.resendAvailableIn)
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreen(
        uiState = uiState,
        onTextChange = viewModel::onFullNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTermCheckChange = viewModel::onTermCheckChanged,
        onLoginClick = onNavigateToLogin,
        onRegisterClick = viewModel::onRegister
    )

    CustomBottomSheet(
        visible = uiState.isBottomSuccessVisible,
        dismissOnClickOutside = false,
        onDismiss = { }
    ) {
        ModalStatusContent(
            title = "Register Success!",
            subtitle = "Congratulation, you're now registered.\nNext, you need to setup your account first.",
            buttonTitle ="Continue setup account",
            onClick = viewModel::onConfirmVerification
        ){
            SuccessIcon()
        }
    }

    CustomBottomSheet(
        visible = uiState.isBottomFailedVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = "Register Failed!",
            subtitle = uiState.bottomSheetMessage?:"",
            buttonTitle = "OK",
            onClick = viewModel::onDismissBottomSheet
        ) {
            FailedIcon()
        }
    }
}

@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onTextChange : (String) -> Unit,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onTermCheckChange:(Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            AuthTopAppBar()
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderTitleScreen(
                    title = stringResource(R.string.create_your_new_account),
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_an_account_to_start_looking_for_the),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text
                )
            }

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextSection(
                    label = stringResource(R.string.full_name),
                    text = uiState.fullName,
                    textError = uiState.fullNameError?:"",
                    leadingIcon = R.drawable.person,
                    onTextChanged = onTextChange,
                    enabled = uiState.isTextEditable
                )

                EmailSection (
                    email = uiState.email,
                    emailError = uiState.emailError?:"",
                    onEmailChanged = onEmailChange,
                    enabled = uiState.isTextEditable
                )

                PasswordSection(
                    password = uiState.password,
                    passwordError = uiState.passwordError?:"",
                    onPasswordChanged = onPasswordChange,
                    enabled = uiState.isTextEditable
                )

                TermsOfServiceCheckbox(
                    checked = uiState.isTermSelected,
                    onCheckedChange = onTermCheckChange,
                    onTermsClick = {}
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                LoadingButtonComponent (
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.register,
                    onClick = onRegisterClick,
                    isLoading = uiState.isLoading
                )
            }

            Divider32()

            ButtonLogin()

            CustomTwoColorText(
                fullText = "Already have an account? Login",
                highlightText = "Login",
                highlightColor = Orange500,
                textColor = Neutral60,
                normalStyle = LocalCustomTypography.current.bodyMediumRegular,
                onHighlightClick = onLoginClick
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun RegisterLightPreview() {
//    TasstyTheme(darkTheme = false) {
//        RegisterScreen(
//            uiState = RegisterUiState(
//                isLoading = false,
//                email = "",
//                password = "123456",
//                fullName = "",
//                fullNameError = "",
//                isTermSelected = true
//            ),
//            onTermCheckChange = {},
//            onRegisterClick = {},
//            onLoginClick = {},
//            onTextChange = {},
//            onEmailChange = {},
//            onPasswordChange = {}
//        )
//
//        CustomBottomSheet(
//            visible = false,
//            dismissOnClickOutside = false,
//            onDismiss = { }
//        ) {
//            ModalStatusContent(
//                title = "Register Success!",
//                subtitle = "Congratulation, you're now registered.\nNext, you need to setup your account first.",
//                buttonTitle ="Continue setup account",
//                onClick = {}
//            ){
//                SuccessIcon()
//            }
//        }
//    }
//}
//
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "Dark Mode",
//)
//@Composable
//fun RegisterDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        RegisterScreen(
//            uiState = RegisterUiState(
//                isLoading = true,
//                email = "atifafiorenza24@gmail.com",
//                password = "123456",
//                fullName = "",
//                isTermSelected = true,
//                isButtonEnabled = false,
//            ),
//            onTermCheckChange = {},
//            onRegisterClick = {},
//            onLoginClick = {},
//            onTextChange = {},
//            onEmailChange = {},
//            onPasswordChange = {}
//        )
//
//        CustomBottomSheet(
//            visible = false,
//            dismissOnClickOutside = false,
//            onDismiss = { }
//        ) {
//            ModalStatusContent(
//                title = "Register Success!",
//                subtitle = "Congratulation, you're now registered.\nNext, you need to setup your account first.",
//                buttonTitle ="Continue setup account",
//                onClick = {}
//            ){
//                SuccessIcon()
//            }
//        }
//    }
//}