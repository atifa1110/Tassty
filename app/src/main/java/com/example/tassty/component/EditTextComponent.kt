package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.AddressType
import com.example.tassty.R
import com.example.tassty.screen.setuplocation.AddressTypeRow
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Pink600

fun iconPainter(resId: Int) = @Composable {
    Icon(painter = painterResource(resId), contentDescription = null, modifier = Modifier.size(20.dp))
}

fun iconVector(vector: ImageVector, description: String = "") = @Composable {
    Icon(imageVector = vector, contentDescription = description, modifier = Modifier.size(20.dp))
}

@Composable
fun TextFieldError(textError: String) {
    Row (
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            painter = painterResource(id = R.drawable.information_circle),
            contentDescription = "information",
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = textError,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Pink600
        )
    }
}

@Composable
fun EmailComponent(
    placeholder: String = "Enter your email",
    text: String,
    textError: String = "",
    onTextChanged: (String) -> Unit,
) {
    TextFieldComponent(
        text = text,
        onTextChanged = onTextChanged,
        placeholder = placeholder,
        leadingIcon = iconVector(Icons.Default.Email),
        textError = textError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun EmailSection(
    label: String = stringResource(R.string.email_address),
    email: String,
    emailError: String,
    onEmailChanged: (String) -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = label,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        EmailComponent(
            text = email,
            textError = emailError,
            onTextChanged = onEmailChanged
        )
    }
}

@Composable
fun PasswordComponent(
    placeholder: String = stringResource(R.string.enter_password),
    text: String,
    textError: String = "",
    onTextChanged: (String) -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }

    TextFieldComponent(
        text = text,
        onTextChanged = onTextChanged,
        placeholder = placeholder,
        leadingIcon = iconVector(Icons.Default.Lock),
        textError = textError,
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingIcon = {
            val iconImage = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (isVisible) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }
            IconButton(onClick = { isVisible = !isVisible }) {
                iconVector(vector = iconImage,description = description)()
            }
        }
    )
}

@Composable
fun PasswordSection(
    placeholder: String = stringResource(R.string.password),
    label: String = stringResource(R.string.password),
    password: String,
    passwordError: String,
    onPasswordChanged: (String) -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = label,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        PasswordComponent(
            placeholder = placeholder,
            text = password,
            textError = passwordError,
            onTextChanged = onPasswordChanged
        )
    }
}

@Composable
fun TextComponent(
    placeholder: String = stringResource(R.string.enter_your_name),
    text: String,
    textError: String = "",
    singleLine: Boolean = true,
    onTextChanged: (String) -> Unit,
    leadingIcon: Int? = null,
) {
    TextFieldComponent(
        text = text,
        onTextChanged = onTextChanged,
        placeholder = placeholder,
        leadingIcon = leadingIcon?.let { iconPainter(it) },
        textError = textError,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun TextSection(
    label: String = "Name",
    placeholder: String = "Enter name",
    text: String,
    textError: String,
    singleLine: Boolean = true,
    leadingIcon: Int? = null,
    onTextChanged: (String) -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = label,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        TextComponent(
            placeholder = placeholder,
            text = text,
            textError = textError,
            singleLine = singleLine,
            onTextChanged = onTextChanged,
            leadingIcon = leadingIcon
        )
    }
}

@Composable
fun AddressTypeSection(
    addressType: AddressType,
    onTypeSelected: (AddressType) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            text = "Address type",
            color = Neutral100,
            style = LocalCustomTypography.current.h5Bold,
        )

        AddressTypeRow(
            selectedType = addressType,
            onTypeSelected = onTypeSelected
        )
    }
}

@Composable
fun DetailNotesSection(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Notes",
                style = LocalCustomTypography.current.h5Bold,
                color = Neutral100
            )
            Text(
                text = "${text.length} / 100",
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Neutral70
            )
        }
        TextComponent(
            text = text,
            textError = "",
            singleLine = false,
            placeholder = "Place your notes here...",
            onTextChanged = onTextChanged,
            leadingIcon = R.drawable.clipboard_list
        )
    }
}

@Composable
fun TextTransformationSection(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    placeholder: String,
    leadingIcon: Int,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextChanged: (String) -> Unit
){
    Column(
        modifier=  modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = label,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )

        TextFieldComponent(
            text = text,
            placeholder = placeholder,
            leadingIcon = iconPainter(leadingIcon),
            onTextChanged = onTextChanged,
            visualTransformation = visualTransformation
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditTextReview(){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EmailSection(
            email = "",
            emailError = "",
            onEmailChanged = {}
        )

        PasswordSection(
            password = "",
            passwordError = "",
            onPasswordChanged = {}
        )

        TextSection(
            text = "",
            textError = "",
            onTextChanged = {}
        )
    }
}