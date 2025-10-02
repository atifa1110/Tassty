package com.example.tassty.screen

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.AuthTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.ButtonLogin
import com.example.tassty.component.EmailComponent
import com.example.tassty.component.PasswordComponent
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun LoginScreen(
    email:String,
    emailError:String,
    password:String,
    passwordError:String,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = "Username/Email",
                        style = LocalCustomTypography.current.h5Bold,
                    )

                    EmailComponent(
                        email = email,
                        emailError = emailError,
                        onEmailChanged = {}
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        text = "Password",
                        style = LocalCustomTypography.current.h5Bold,
                    )

                    PasswordComponent(
                        password = password,
                        passwordError = passwordError,
                        onPasswordChanged = {}
                    )

                    // Teks Forgot password
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = LocalCustomTypography.current.bodyMediumMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { /* Handle forgot password click */ }),
                        textAlign = TextAlign.Right,
                        color = Orange500
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                ButtonComponent(
                    enabled = email.isNotEmpty() && password.isNotEmpty(),
                    labelResId = R.string.login,
                    onClick = onLoginClick
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


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(
        email="atifafiorenza@gmail.com",
        emailError = "",
        password = "1245678",
        passwordError = "",
        onLoginClick = {},
        onRegisterClick = {}
    )
}