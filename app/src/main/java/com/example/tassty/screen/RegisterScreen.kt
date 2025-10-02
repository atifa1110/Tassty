package com.example.tassty.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.EmailComponent
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.PasswordComponent
import com.example.tassty.component.TermsOfServiceCheckbox
import com.example.tassty.component.TextComponent
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun RegisterRoute(){
    var showSuccessModal by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        RegisterScreen(
            text = "atifaforenza",
            textError = "",
            email = "atifafiorenza24@gmail.com",
            emailError = "",
            password = "124567",
            passwordError = "",
            onTextChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )

        CustomBottomSheet(
            visible = showSuccessModal,
            dismissOnClickOutside = false,
            onDismiss = { showSuccessModal = false }
        ) {
            ModalStatusContent(
                title = "Register Success!",
                subtitle = "Congratulation, you're now registered.\nNext, you need to setup your account first.",
                buttonTitle ="Continue setup account",
                onClick = { showSuccessModal = false }
            ){
                Image(
                    painter = painterResource(id = R.drawable.success),
                    contentDescription = "Success Icon",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    text: String,
    textError: String,
    email:String,
    emailError:String,
    password:String,
    passwordError:String,
    onTextChange : (String) -> Unit,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
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
            Spacer(Modifier.height(12.dp))
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

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = stringResource(R.string.full_name),
                        style = LocalCustomTypography.current.h5Bold,
                    )

                    TextComponent(
                        text = text,
                        textError = textError,
                        placeholder = "Enter name",
                        leadingIcon = R.drawable.person,
                        onTextChanged = {
                            onTextChange(it)
                        }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = stringResource(R.string.email_address),
                        style = LocalCustomTypography.current.h5Bold,
                    )

                    EmailComponent(
                        email = email,
                        emailError = emailError,
                        onEmailChanged = {
                            onEmailChange(it)
                        }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = stringResource(R.string.password),
                        style = LocalCustomTypography.current.h5Bold,
                    )

                    PasswordComponent(
                        password = password,
                        passwordError = passwordError,
                        onPasswordChanged = {
                            onPasswordChange(it)
                        }
                    )

                    TermsOfServiceCheckbox(
                        checked = false,
                        onCheckedChange = {}
                    ) { }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                ButtonComponent(
                    enabled = text.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty(),
                    labelResId = R.string.register,
                    onClick = {
                        onRegisterClick()
                    }
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

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterRoute()
}