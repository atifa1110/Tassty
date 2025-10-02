package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Pink300
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink600

@Composable
fun PasswordComponent(
    password: String,
    passwordError: String,
    onPasswordChanged: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }

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
            unfocusedTrailingIconColor = Neutral60,
            focusedTrailingIconColor = Neutral60,
            errorTextColor = Pink600,
            errorBorderColor = Pink600,
            errorContainerColor = Pink50,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink300
        ),
        value = password,
        onValueChange = { onPasswordChanged(it)},
        singleLine = true,
        isError = passwordError.isNotEmpty(),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        placeholder = { Text(
            text = "Enter password",
            style = LocalCustomTypography.current.bodyMediumRegular
        ) },
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Password Icon",
                Modifier.size(20.dp),
            )
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible.value) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }

            Icon(
                imageVector = iconImage,
                contentDescription = description,
                modifier = Modifier.size(20.dp).clickable{
                    passwordVisible.value = !passwordVisible.value },
            )
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )

    if(passwordError.isNotEmpty()){
        TextFieldError(R.string.password_error)
    }

}