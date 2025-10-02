package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.EmailComponent
import com.example.tassty.component.PasswordComponent
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70

enum class PasswordResetStage {
    EMAIL,
    PASSWORD
}

@Composable
fun ResetPasswordScreen() {
    var currentStage by remember { mutableStateOf(PasswordResetStage.EMAIL) }

    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    when (currentStage) {
        PasswordResetStage.EMAIL -> {
            ResetEmail(
                email = email,
                emailError = emailError,
                onEmailChange = { email = it },
                onSendOtpClick = {
                    // validasi dulu
                    if (email.isEmpty()) {
                        emailError = "Email cannot be empty"
                    } else {
                        emailError = ""
                        currentStage = PasswordResetStage.PASSWORD
                    }
                },
                onBackClick = { /* misal popBackStack */ }
            )
        }

        PasswordResetStage.PASSWORD -> {
            ResetNewPassword(
                password = password,
                confirm = confirm,
                passwordError = passwordError,
                confirmError = confirmError,
                onPasswordChange = { password = it },
                onConfirmChange = { confirm =it },
                onChangePasswordClick = {
                    if (password != confirm) {
                        passwordError ="Passwords do not match"
                        confirmError = "Passwords do not match"
                    } else {
                        passwordError=""
                        confirmError = ""
                    }
                },
                onBackClick = { currentStage = PasswordResetStage.EMAIL }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetEmail(
    email: String,
    emailError:String,
    onEmailChange: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            BackTopAppBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding() // tombol naik saat keyboard muncul
                    .padding(24.dp)
            ) {
                ButtonComponent(
                    enabled = email.isNotEmpty(),
                    labelResId = R.string.send_otp,
                    onClick = onSendOtpClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize().background(Neutral10)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.reset_password),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )

                Text(
                    text = stringResource(R.string.order_reset_password),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider(
                color = Neutral30
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = "Email address",
                    style = LocalCustomTypography.current.h5Bold,
                )

                EmailComponent(
                    email = email,
                    emailError = emailError,
                    onEmailChanged = onEmailChange
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetNewPassword(
    password: String,
    confirm: String,
    passwordError:String,
    confirmError:String,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onChangePasswordClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            BackTopAppBar(onBackClick = onBackClick)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding() // tombol naik saat keyboard muncul
                    .padding(24.dp)
            ) {
                ButtonComponent(
                    enabled = password.isNotEmpty() && confirm.isNotEmpty(),
                    labelResId = R.string.change_password,
                    onClick = onChangePasswordClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize().background(Neutral10)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.reset_password),
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )

                Text(
                    text = stringResource(R.string.password_contain_combination),
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral70,
                    textAlign = TextAlign.Start
                )
            }

            HorizontalDivider(
                color = Neutral30
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = "New Password",
                    style = LocalCustomTypography.current.h5Bold,
                )

                PasswordComponent(
                    password = password,
                    passwordError = passwordError,
                    onPasswordChanged = onPasswordChange
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    text = "Confirm Password",
                    style = LocalCustomTypography.current.h5Bold,
                )

                PasswordComponent(
                    password = confirm,
                    passwordError = confirmError,
                    onPasswordChanged = onConfirmChange
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordEmailPreview() {
    ResetEmail(
        email = "",
        emailError = "",
        onEmailChange = {},
        onSendOtpClick = {}
    ) { }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    ResetNewPassword(
        password = "",
        confirm = "",
        passwordError = "",
        confirmError = "",
        onPasswordChange = {},
        onConfirmChange = {},
        onChangePasswordClick = {}
    ) { }
}