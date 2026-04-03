package com.example.tassty.screen.verification

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.component.LoadingButtonComponent
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun VerificationRoute(
    onNavigateToSetUp:() -> Unit,
    onNavigateToNewPassword:() -> Unit,
    viewModel: VerificationViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is VerificationEvent.NavigateToSetUp -> {
                    onNavigateToSetUp()
                }

                is VerificationEvent.ShowMessage -> {
                    snackHostState.showSnackbar(event.message)
                }

                is VerificationEvent.NavigateToNewPassword -> {
                    onNavigateToNewPassword()
                }
            }
        }
    }

    VerificationScreen(
        snackHostState = snackHostState,
        uiState = uiState,
        onOtpChange = viewModel::onOtpChange,
        onVerifyClick = viewModel::onVerificationCode,
        onDismissError = viewModel::onErrorDismiss,
        onResendCodeClick = viewModel::onResendVerification
    )
}

@Composable
fun VerificationScreen(
    snackHostState: SnackbarHostState,
    uiState: VerificationUiState,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onDismissError: () -> Unit,
    onResendCodeClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = {SnackbarHost(snackHostState)},
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = {})
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = onResendCodeClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.isResendEnabled
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.didnt_get_the_email),
                            style = LocalCustomTypography.current.bodyMediumRegular,
                            color = LocalCustomColors.current.text,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (uiState.isResendEnabled) "Resend code"
                            else "Resend code in ${uiState.timerSeconds}s",
                            style = LocalCustomTypography.current.bodyMediumMedium,
                            color = if(uiState.isResendEnabled) Orange500 else Neutral60,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                LoadingButtonComponent(
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.verify,
                    onClick = onVerifyClick,
                    isLoading = uiState.isLoading
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeaderTitleScreen(title = uiState.title)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = LocalCustomTypography.current.bodyMediumRegular.toSpanStyle().copy(color = LocalCustomColors.current.text)) {
                                append(uiState.instruction)
                                append(" ")
                            }

                            withStyle(style = LocalCustomTypography.current.bodyMediumMedium.toSpanStyle().copy(color = LocalCustomColors.current.headerText)) {
                                append(uiState.email)
                                append(" ")
                            }

                            withStyle(style = LocalCustomTypography.current.bodyMediumRegular.toSpanStyle().copy(color = LocalCustomColors.current.text)) {
                                append(uiState.recoveryInfo)
                            }
                        },
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = Neutral70,
                        textAlign = TextAlign.Start
                    )

                }

                Divider32()

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.isError) {
                        ErrorBanner(message = uiState.errorMessage, onClick = onDismissError)
                    }

                    BasicTextField(
                        enabled = uiState.isTextEditable,
                        value = uiState.otp,
                        onValueChange = {
                            onOtpChange(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                if (uiState.otp.length == 6) {
                                    onVerifyClick()
                                }
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(6) { index ->
                                    val char = when {
                                        index < uiState.otp.length -> uiState.otp[index]
                                        else -> ' '
                                    }

                                    val boxBackgroundColor =
                                        if (uiState.isError) LocalCustomColors.current.errorBackground else Color.Transparent
                                    val boxBorderColor =
                                        if (uiState.isError) Pink500 else Neutral40
                                    val boxActiveBorderColor =
                                        if (uiState.isError) Pink500 else Neutral40
                                    val boxFilledBorderColor =
                                        if (uiState.isError) Pink500 else Neutral40

                                    val borderColor = when {
                                        uiState.isError -> LocalCustomColors.current.errorBorder
                                        index < uiState.otp.length -> boxFilledBorderColor
                                        index == uiState.otp.length -> boxActiveBorderColor
                                        else -> boxBorderColor
                                    }

                                    if (index == 3) {
                                        Spacer(
                                            modifier = Modifier
                                                .width(8.dp)
                                                .height(1.dp)
                                                .background(Neutral70)
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(boxBackgroundColor)
                                            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = char.toString(),
                                            style = LocalCustomTypography.current.bodyMediumRegular,
                                            color = if (uiState.isError) Pink500 else LocalCustomColors.current.headerText,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ErrorBanner(
    message: String,
    onClick:()-> Unit ={}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Pink500)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.information_circle),
            contentDescription = "Error",
            modifier = Modifier.size(18.dp),
            colorFilter = ColorFilter.tint(Neutral10)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = message,
            style = LocalCustomTypography.current.bodySmallSemiBold,
            color = Neutral10
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            modifier = Modifier.size(18.dp).clickable(onClick = onClick),
            tint = Neutral10,
        )
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun VerificationLightPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = false) {
//        VerificationScreen(
//            snackHostState = snackHostState,
//            uiState = VerificationUiState(
//                otp = "123456",
//                email = "rafiq@gmail.com",
//                isError = false,
//                isLoading = false,
//                isButtonEnabled = true,
//                isResendEnabled = false,
//                title = "Verify Your Email.",
//                instruction = "Please enter the 6-digit activation code sent to : ",
//                recoveryInfo = "to activate your account.",
//            ),
//            onOtpChange = {},
//            onVerifyClick = {},
//            onResendCodeClick = {},
//            onDismissError = {}
//        )
//    }
//}
//
//@Preview(
//    showBackground = true,
//    name = "Dark Mode",
//)
//@Composable
//fun VerificationDarkPreview() {
//    val snackHostState = remember { SnackbarHostState() }
//    TasstyTheme(darkTheme = true) {
//        VerificationScreen(
//            snackHostState = snackHostState,
//            uiState = VerificationUiState(
//                otp = "123456",
//                email = "rafiq@gmail.com",
//                isError = false,
//                isLoading = false,
//                isButtonEnabled = true,
//                isResendEnabled = false,
//                title = "Verify Your Email.",
//                instruction = "Please enter the 6-digit activation code sent to : ",
//                recoveryInfo = "to activate your account.",
//            ),
//            onOtpChange = {},
//            onVerifyClick = {},
//            onResendCodeClick = {},
//            onDismissError = {}
//        )
//    }
//}