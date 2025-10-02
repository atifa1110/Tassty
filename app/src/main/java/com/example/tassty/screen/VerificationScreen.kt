package com.example.tassty.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500

@Composable
fun VerificationRoute(){

}

@Composable
fun VerificationScreen(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendCodeClick: () -> Unit,
    email: String = "rafiq@email.com"
) {
    var isCodeWrong by remember { mutableStateOf(false) }
    val otpLength = 6

    Scaffold(
        containerColor = Color.White,
        topBar = {
            BackTopAppBar(onBackClick = {})
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TextButton(
                    onClick = onResendCodeClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.didnt_get_the_email),
                            style = LocalCustomTypography.current.bodyMediumRegular,
                            color = Neutral70,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.resend_code),
                            style = LocalCustomTypography.current.bodyMediumMedium,
                            color = Orange500,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                ButtonComponent(
                    enabled = otpCode.isNotEmpty(),
                    labelResId = R.string.verify,
                    onClick = onVerifyClick
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Verify your Email.",
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Please enter the verification code sent to:",
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = Neutral70,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = email,
                        style = LocalCustomTypography.current.bodyMediumMedium,
                        color = Neutral100,
                        textAlign = TextAlign.Start,
                    )
                }

            }

            HorizontalDivider(
                color = Neutral30
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Pesan error di atas input OTP
                if (isCodeWrong) {
                    ErrorBanner(message = "OTP code is wrong!")
                }

                // OTP Input Fields
                BasicTextField(
                    value = otpCode,
                    onValueChange = {
//                if (it.length <= otpLength) {
//                    otpCode = it
//                }
                        onOtpChange(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(otpLength) { index ->
                                val char = when {
                                    index < otpCode.length -> otpCode[index]
                                    else -> ' '
                                }

                                // Warna bersyarat berdasarkan isCodeWrong
                                val boxBackgroundColor = if (isCodeWrong) Pink50 else Neutral10
                                val boxBorderColor = if (isCodeWrong) Pink500 else Neutral40
                                val boxActiveBorderColor = if (isCodeWrong) Pink500 else Neutral40
                                val boxFilledBorderColor = if (isCodeWrong) Pink500 else Neutral40

                                val borderColor = when {
                                    isCodeWrong -> Pink500
                                    index < otpCode.length -> boxFilledBorderColor
                                    index == otpCode.length -> boxActiveBorderColor
                                    else -> boxBorderColor
                                }

                                if (index == 3) { // Letakkan pemisah di antara digit ke-3 dan ke-4
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
                                        color = if (isCodeWrong) Pink500 else Neutral100,
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

// Tambahkan Composable untuk banner pesan error
@Composable
fun ErrorBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Pink500)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icon
        Image(
            painter = painterResource(id = R.drawable.information_circle), // Ganti dengan ikon info Anda
            contentDescription = "Error",
            modifier = Modifier.size(18.dp),
            colorFilter = ColorFilter.tint(Neutral10)
        )
        Text(
            text = message,
            style = LocalCustomTypography.current.bodySmallSemiBold,
            color = Neutral10
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            modifier = Modifier.size(18.dp),
            tint = Neutral10,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VerificationPreview() {
    VerificationScreen(
        otpCode = "",
        onOtpChange = {},
        onVerifyClick = {},
        onResendCodeClick = {}
    )
}