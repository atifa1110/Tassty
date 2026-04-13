package com.example.tassty.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.AddressType
import com.example.tassty.R
import com.example.tassty.screen.setuplocation.AddressTypeRow
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink400
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink600
import com.example.tassty.util.UiText

fun iconPainter(resId: Int) = @Composable {
    Icon(painter = painterResource(resId), contentDescription = null, modifier = Modifier.size(20.dp))
}

fun iconVector(vector: ImageVector, description: String = "") = @Composable {
    Icon(imageVector = vector, contentDescription = description, modifier = Modifier.size(20.dp))
}

@Composable
fun EditTextHeader(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelStyle: TextStyle = LocalCustomTypography.current.h5Bold,
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = if (enabled) LocalCustomColors.current.headerText else LocalCustomColors.current.text,
            modifier = Modifier.fillMaxWidth()
        )
        content()
    }
}

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    textError: UiText? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 5,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalCustomTypography.current.bodyMediumMedium,
    placeholderStyle:  TextStyle = LocalCustomTypography.current.bodyMediumRegular,
) {
    val errorMessage = textError?.asString() ?: ""

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = LocalCustomColors.current.headerText,
            unfocusedTextColor = LocalCustomColors.current.headerText,
            disabledTextColor = LocalCustomColors.current.text,
            focusedPlaceholderColor = Neutral60,
            unfocusedPlaceholderColor = Neutral60,
            focusedContainerColor = LocalCustomColors.current.background,
            unfocusedContainerColor = LocalCustomColors.current.background,
            disabledContainerColor = LocalCustomColors.current.background,
            focusedBorderColor = LocalCustomColors.current.border,
            unfocusedBorderColor = LocalCustomColors.current.borderUnfocused,
            disabledBorderColor = LocalCustomColors.current.border,
            focusedLeadingIconColor = LocalCustomColors.current.iconFocused,
            unfocusedLeadingIconColor = LocalCustomColors.current.iconDisable,
            disabledLeadingIconColor = LocalCustomColors.current.iconDisable,
            unfocusedTrailingIconColor = Neutral60,
            focusedTrailingIconColor = Neutral60,
            disabledTrailingIconColor = Neutral60,
            errorPlaceholderColor = Pink600,
            errorTextColor = Pink600,
            errorBorderColor = LocalCustomColors.current.errorBorder,
            errorContainerColor = LocalCustomColors.current.errorBackground,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink600
        ),
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        value = text,
        onValueChange = { onTextChanged(it) },
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderStyle
            )
        },
        enabled = enabled,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        isError = errorMessage.isNotEmpty(),
    )

    if(errorMessage.isNotEmpty()){
        TextFieldError(textError = errorMessage)
    }
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
    textError: UiText?,
    enabled: Boolean = true,
    onTextChanged: (String) -> Unit,
) {
    TextFieldComponent(
        text = text,
        onTextChanged = onTextChanged,
        placeholder = placeholder,
        leadingIcon = iconVector(Icons.Default.Email),
        textError = textError,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun EmailSection(
    label: String = stringResource(R.string.email_address),
    enabled: Boolean = true,
    email: String,
    emailError: UiText?,
    onEmailChanged: (String) -> Unit
) {
    EditTextHeader(label = label) {
        EmailComponent(
            text = email,
            enabled = enabled,
            textError = emailError,
            onTextChanged = onEmailChanged
        )
    }
}

@Composable
fun PasswordComponent(
    placeholder: String = stringResource(R.string.enter_password),
    enabled: Boolean = true,
    text: String,
    textError: UiText?,
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
        enabled = enabled,
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
    enabled: Boolean = true,
    label: String = stringResource(R.string.password),
    password: String,
    passwordError: UiText?,
    onPasswordChanged: (String) -> Unit
) {
    EditTextHeader(label = label) {
        PasswordComponent(
            placeholder = placeholder,
            enabled = enabled,
            text = password,
            textError = passwordError,
            onTextChanged = onPasswordChanged
        )
    }
}

@Composable
fun TextComponent(
    placeholder: String = stringResource(R.string.enter_your_name),
    enabled: Boolean = true,
    text: String,
    textError: UiText? = null,
    singleLine: Boolean = true,
    onTextChanged: (String) -> Unit,
    leadingIcon: Int? = null,
) {
    TextFieldComponent(
        text = text,
        enabled = enabled,
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
    enabled: Boolean = true,
    text: String,
    textError: UiText? = null,
    singleLine: Boolean = true,
    leadingIcon: Int? = null,
    onTextChanged: (String) -> Unit
) {
    EditTextHeader(label = label) {
        TextComponent(
            placeholder = placeholder,
            text = text,
            enabled= enabled,
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
    EditTextHeader(label = "Address type") {
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
                color = LocalCustomColors.current.headerText
            )
            Text(
                text = "${text.length} / 100",
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text
            )
        }
        TextComponent(
            text = text,
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
        modifier =  modifier.fillMaxWidth(),
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

@Composable
fun MessageInputBar(
    text: String,
    placeholder: String,
    onTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onAttachClick: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        placeholder = {
            Text(
                text = placeholder,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = LocalCustomColors.current.text
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.attachment),
                contentDescription = "Attach",
                tint = LocalCustomColors.current.iconFocused,
                modifier = Modifier.clickable { onAttachClick() }
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.send),
                contentDescription = "Send",
                tint = Orange500,
                modifier = Modifier.clickable { onSendMessage() }
            )
        },
        shape = RoundedCornerShape(50.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Neutral30,
            unfocusedBorderColor = Neutral30,
            focusedTextColor = LocalCustomColors.current.headerText,
            unfocusedTextColor = LocalCustomColors.current.text,
            focusedLeadingIconColor = Neutral100,
            unfocusedLeadingIconColor = Neutral70,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions (
            onSend = { onSendMessage() }
        ),
        singleLine = true
    )
}

@Composable
fun FeedbackSection(
    feedback: String,
    onFeedbackChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(title = "Feedback")

        FeedbackInputField(
            value = feedback,
            onValueChange = onFeedbackChanged
        )
    }
}

@Composable
fun FeedbackInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextFieldComponent(
        modifier = Modifier.height(84.dp).fillMaxWidth(),
        text = value,
        singleLine = false,
        maxLines = 5,
        onTextChanged = onValueChange,
        placeholder = "Enter your feedback here..",
        leadingIcon = {
            Box(modifier = Modifier.fillMaxHeight().padding(top = 18.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Neutral70
                )
            }
        },
        enabled = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
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
            emailError = UiText.DynamicString(""),
            onEmailChanged = {}
        )

        PasswordSection(
            password = "",
            passwordError = UiText.DynamicString(""),
            onPasswordChanged = {}
        )

        TextSection(
            text = "",
            onTextChanged = {}
        )

        MessageInputBar(text = "Placed", placeholder = "",
            onTextChanged = {}, onSendMessage = {}) { }
    }
}