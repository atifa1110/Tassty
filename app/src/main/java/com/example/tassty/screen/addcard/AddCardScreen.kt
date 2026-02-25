package com.example.tassty.screen.addcard

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.component.AddCardTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CardBackgroundItem
import com.example.tassty.component.CardColorItem
import com.example.tassty.component.Divider32
import com.example.tassty.component.TermsAndConditionsText
import com.example.tassty.component.TextInputForm
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.colorList
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.payments.paymentlauncher.rememberPaymentLauncher

@Composable
fun AddCardScreen(
    viewModel: AddCardViewModel= hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddCardContent(
        uiState = uiState,
        onCardNameChange = viewModel::onCardNameChange,
        onCardNumberChange = viewModel::onCardNumberChange,
        onExpireDateChange = viewModel::onExpireDateChange,
        onCvvNumberChange = viewModel::onCvvChange,
        onSaveCardCLicked = {viewModel.onSaveClicked(context as ComponentActivity)},
        onColorSelected = viewModel::onColorSelected,
        onImageSelected = viewModel::onPatternSelected
    )

    val paymentLauncher = rememberPaymentLauncher(
        publishableKey = "pk_test_your_key_disini",
        callback = { result ->
            when (result) {
                is PaymentResult.Completed -> {
                    // Di sini Luna panggil finalize ke ViewModel
                    viewModel.onStripeValidationSuccess()
                }
                is PaymentResult.Failed -> {
                    // Handle error (misal: kartu ditolak)
                }
                is PaymentResult.Canceled -> {
                    // User menutup layar Stripe
                }
            }
        }
    )
}

@Composable
fun AddCardContent(
    uiState: AddCardUiState,
    onCardNameChange: (String) -> Unit,
    onCardNumberChange: (String) -> Unit,
    onExpireDateChange: (String) -> Unit,
    onCvvNumberChange: (String) -> Unit,
    onSaveCardCLicked:()-> Unit,
    onColorSelected: (CardColorOption) -> Unit,
    onImageSelected:(Int) -> Unit,
    buttonEnable: Boolean = false
) {
    Scaffold(
        containerColor = Neutral10,
        bottomBar = {
            Column(Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = buttonEnable,
                    labelResId = R.string.save,
                    onClick = onSaveCardCLicked
                )
                TermsAndConditionsText (onClickTerms = {})
            }
        },
        topBar = {
            AddCardTopAppBar(
                onAddClick = {},
                onBackClick = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Add card",
                            style = LocalCustomTypography.current.h2Bold,
                            color = Neutral100
                        )
                        Icon(
                            painter = painterResource(R.drawable.category),
                            modifier = Modifier.size(38.dp),
                            contentDescription = "icon category",
                            tint = Green500
                        )
                    }
                    Text(
                        text = "Your card details will be saved securely",
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = Neutral70
                    )
                }
                Divider32()
            }

            item {
                Column(Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextInputForm(
                        title = "Card holder's name",
                        placeholder = "Enter name",
                        text = uiState.cardName,
                        leadingIcon = R.drawable.person,
                        onTextChanged = onCardNameChange
                    )

                    TextInputForm(
                        title = "Card number",
                        placeholder = "Enter number",
                        text = uiState.cardNumber,
                        leadingIcon = R.drawable.credit_card,
                        onTextChanged = onCardNumberChange
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextInputForm(
                            modifier = Modifier.weight(1f),
                            title = "Expiry Date",
                            placeholder = "MM/YY",
                            text = uiState.expireDate,
                            leadingIcon = R.drawable.calendar,
                            onTextChanged = onExpireDateChange
                        )

                        TextInputForm(
                            modifier = Modifier.weight(1f),
                            title = "CVV",
                            placeholder = "CVV",
                            text = uiState.cvv,
                            leadingIcon = R.drawable.lock,
                            onTextChanged = onCvvNumberChange
                        )
                    }
                }
                Divider32()
            }

            item {
                Column(
                    Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardBackgroundListContent(
                        selectedPatternRes = uiState.selectedImage,
                        option = uiState.selectedColor,
                        patterns = uiState.backgroundPatterns,
                        onClick = {onImageSelected(uiState.selectedImage)}
                    )

                    CardColorListContent(
                        selectedColor = uiState.selectedColor,
                        colorOptions = uiState.colorOptions,
                        onClick = {onColorSelected(uiState.selectedColor)}
                    )
                }
            }
        }
    }
}

@Composable
fun CardBackgroundListContent(
    selectedPatternRes: Int,
    option: CardColorOption,
    patterns: List<Int>,
    onClick: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = "Card color",
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Kasih jarak antar item
        ) {
            patterns.forEach { resId ->
                CardBackgroundItem(
                    modifier = Modifier.weight(1f),
                    imageRes = resId,
                    isSelected = resId == selectedPatternRes,
                    colorOption = option,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun CardColorListContent(
    selectedColor: CardColorOption,
    colorOptions: List<CardColorOption>,
    onClick:() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            textAlign = TextAlign.Start,
            text = "Card color",
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            colorOptions.forEach { option ->
                CardColorItem(
                    option = option,
                    isSelected = selectedColor.id == option.id,
                    onClick = onClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCardScreenPreview() {
    AddCardContent(
        uiState = AddCardUiState(
            selectedColor = colorList[2],
            selectedImage = R.drawable.background_design_2,
            cardName = "Atifa Fiorenza",
            cardNumber = "2342323248383878",
            expireDate = "10/28",
            cvv = "123",
        ),
        onCardNumberChange = {},
        onCardNameChange = {},
        onCvvNumberChange = {},
        onExpireDateChange = {},
        onSaveCardCLicked = {},
        onColorSelected = {},
        onImageSelected = {}
    )
}