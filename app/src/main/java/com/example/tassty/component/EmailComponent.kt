package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink600

@Composable
fun EmailComponent(
    email: String,
    emailError: String,
    onEmailChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Neutral30,
            unfocusedBorderColor = Neutral30,
            focusedPlaceholderColor = Neutral100,
            unfocusedPlaceholderColor = Neutral60,
            unfocusedLeadingIconColor = Neutral70,
            focusedLeadingIconColor = Neutral100,
            errorTextColor = Pink600,
            errorBorderColor = Pink600,
            errorContainerColor = Pink50,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink600
        ),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        value = email,
        onValueChange = { onEmailChanged(it) },
        placeholder = { Text(
            text ="Enter username/email",
            style = LocalCustomTypography.current.bodyMediumRegular
        ) },
        leadingIcon = {
            Icon(
                Icons.Default.Email,
                contentDescription = "Username/Email Icon",
                Modifier.size(20.dp),
            )
        },
        singleLine = true,
        isError = emailError.isNotEmpty(),
    )
    if(emailError.isNotEmpty()){
        TextFieldError(R.string.email_error)
    }
}

@Composable
fun TextFieldError(textError: Int) {
    Row (
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.information_circle), // Ganti dengan resource logo
            contentDescription = "information",
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = textError),
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Pink600
        )
    }
}