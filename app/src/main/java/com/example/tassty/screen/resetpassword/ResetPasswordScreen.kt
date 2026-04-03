package com.example.tassty.screen.resetpassword

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordSection
import com.example.tassty.component.SuccessIcon
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun ResetPasswordScreen(
    onNavigateBack: () -> Unit,
    onNavigateLogin: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when(event){
                is ResetPasswordEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ResetNewPasswordContent(
        uiState = uiState,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onResetPassword = viewModel::onValidateReset,
        onNavigateBack = onNavigateBack
    )

    CustomBottomSheet(
        visible = uiState.isBottomSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = stringResource(R.string.password_changed),
            subtitle = stringResource(R.string.success_reset_password),
            buttonTitle = stringResource( R.string.back_to_login),
            onClick = onNavigateLogin
        ){
            SuccessIcon()
        }
    }
}


@Composable
fun ResetNewPasswordContent(
    uiState: ResetPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onResetPassword: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = onNavigateBack)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(24.dp)
            ) {
                LoadingButtonComponent (
                    isLoading = uiState.isLoading,
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.change_password,
                    onClick = onResetPassword
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
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
                    text = stringResource(R.string.password_contain_combination),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = LocalCustomColors.current.text,
                    textAlign = TextAlign.Start
                )
            }

            Divider32()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PasswordSection(
                    placeholder = stringResource(R.string.new_password),
                    label = stringResource(R.string.new_password),
                    password = uiState.password,
                    passwordError = uiState.passwordError?:"",
                    onPasswordChanged = onPasswordChange,
                    enabled = uiState.isTextEditable
                )

                PasswordSection(
                    label = stringResource(R.string.confirm_password),
                    placeholder = stringResource(R.string.confirm_password),
                    password = uiState.confirmPassword,
                    passwordError = uiState.confirmPasswordError?:"",
                    onPasswordChanged = onConfirmChange,
                    enabled = uiState.isTextEditable
                )
            }
        }
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun ResetLightPreview() {
//    TasstyTheme(darkTheme = false) {
//        ResetNewPasswordContent(
//            uiState = ResetPasswordUiState(
//                password = "12345678",
//                confirmPassword = "12345678",
//                isTextEditable = true
//            ),
//            onPasswordChange = {},
//            onConfirmChange = {},
//            onResetPassword = {},
//            onNavigateBack = {}
//        )
//    }
//}

//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun ResetDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        ResetNewPasswordContent(
//            uiState = ResetPasswordUiState(
//                password = "12345678",
//                confirmPassword = "12345678",
//                isTextEditable = true
//            ),
//            onPasswordChange = {},
//            onConfirmChange = {},
//            onResetPassword = {},
//            onNavigateBack = {}
//        )
//    }
//}