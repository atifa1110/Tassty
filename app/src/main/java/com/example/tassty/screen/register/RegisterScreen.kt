package com.example.tassty.screen.register

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
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.EmailSection
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.component.TermsOfServiceCheckbox
import com.example.tassty.component.TextSection
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun RegisterRoute(
    onNavigateToLogin:()  -> Unit,
    onNavigateToVerify: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.ShowBottomSheet -> {
                    viewModel.setBottomSheetVisible(true)
                }

                RegisterEvent.NavigateToVerify -> {
                    onNavigateToVerify()
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RegisterScreen(
        uiState = uiState,
        onTextChange = {viewModel.onFullNameChange(it)},
        onEmailChange = {viewModel.onEmailChange(it)},
        onPasswordChange = {viewModel.onPasswordChange(it)},
        onTermCheckChange = {viewModel.onTermCheckChanged(it)},
        onLoginClick = onNavigateToLogin,
        onRegisterClick = { viewModel.register()}
    )

    CustomBottomSheet(
        visible = uiState.isBottomSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { }
    ) {
        ModalStatusContent(
            title = "Register Success!",
            subtitle = "Congratulation, you're now registered.\nNext, you need to setup your account first.",
            buttonTitle ="Continue setup account",
            onClick = {viewModel.onUserConfirmVerification()}
        ){
            Image(
                painter = painterResource(id = R.drawable.success),
                contentDescription = "Success Icon",
                modifier = Modifier.size(64.dp)
            )
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
        containerColor = Neutral10,
        topBar = {
            AuthTopAppBar()
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize().background(Neutral10)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_your_new_account),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_an_account_to_start_looking_for_the),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70
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
                    onTextChanged = onTextChange
                )

                EmailSection (
                    email = uiState.email,
                    emailError = uiState.emailError?:"",
                    onEmailChanged = onEmailChange
                )

                PasswordSection(
                    password = uiState.password,
                    passwordError = uiState.passwordError?:"",
                    onPasswordChanged = onPasswordChange
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
                    enabled = uiState.isRegisterButtonEnabled,
                    labelResId = R.string.register,
                    onClick = onRegisterClick,
                    isLoading = uiState.isLoading
                )
            }

            HorizontalDivider(color = Neutral30)

            ButtonLogin()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral60
                )
                Text(
                    text = stringResource(R.string.login),
                    style = LocalCustomTypography.current.bodyMediumMedium,
                    color = Orange500,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegisterPreview() {
//    RegisterScreen(
//        uiState = RegisterUiState(
//            isLoading = false,
//            email = "",
//            password = "123456",
//            fullName = "",
//            fullNameError = "",
//            isTermSelected = true
//        ),
//        onTermCheckChange = {},
//        onRegisterClick = {},
//        onLoginClick = {},
//        onTextChange = {},
//        onEmailChange = {},
//        onPasswordChange = {}
//    )
//}