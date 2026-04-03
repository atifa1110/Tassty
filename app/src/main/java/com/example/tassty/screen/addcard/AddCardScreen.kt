package com.example.tassty.screen.addcard

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.BuildConfig
import com.example.tassty.R
import com.example.tassty.component.AddCardTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CardBackgroundItem
import com.example.tassty.component.CardColorItem
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.Divider32
import com.example.tassty.component.LoadingOverlay
import com.example.tassty.component.ModalStatusContent
import com.example.tassty.component.SuccessIcon
import com.example.tassty.component.TextSection
import com.example.tassty.component.TextTransformationSection
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.PatternImage
import com.example.tassty.model.colorList
import com.example.tassty.model.patterns
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.util.Transformations
import com.stripe.android.Stripe
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.payments.paymentlauncher.rememberPaymentLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddCardScreen(
    onNavigateBack:() -> Unit,
    viewModel: AddCardViewModel= hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stripeTrigger by viewModel.stripeTrigger.collectAsStateWithLifecycle()
    val stripe = remember { Stripe(context, BuildConfig.STRIPE_PUBLISH_KEY) }

    val paymentLauncher = rememberPaymentLauncher(
        publishableKey = BuildConfig.STRIPE_PUBLISH_KEY,
        callback = { result ->
            when (result) {
                is PaymentResult.Completed -> {
                    val secret = uiState.setupIntentClientSecret

                    scope.launch (Dispatchers.IO) {
                        val setupIntent = stripe.retrieveSetupIntentSynchronous(secret)
                        val realPmId = setupIntent.paymentMethodId

                        if (realPmId != null) {
                            viewModel.onStripeValidationSuccess(realPmId)
                        }
                    }
                }
                is PaymentResult.Failed -> {
                    viewModel.onStripeFailed(result.throwable.localizedMessage)
                }
                is PaymentResult.Canceled -> {
                    viewModel.onStripeCanceled()
                }
            }
        }
    )

    LaunchedEffect(stripeTrigger) {
        stripeTrigger?.let { stripe->
            paymentLauncher.confirm(stripe)
            viewModel.onStripeLaunched()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                is AddCardUiEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is AddCardUiEffect.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AddCardContent(
            uiState = uiState,
            onCardNameChange = viewModel::onCardNameChange,
            onCardNumberChange = viewModel::onCardNumberChange,
            onExpireDateChange = viewModel::onExpireDateChange,
            onCvvNumberChange = viewModel::onCvvChange,
            onSaveCardCLicked = { viewModel.onSaveClicked() },
            onColorSelected = viewModel::onColorSelected,
            onImageSelected = viewModel::onPatternSelected,
            onNavigateBack = onNavigateBack
        )

        LoadingOverlay(
            isLoading = uiState.isLoading,
            text = "Securely processing..."
        )
    }

    CustomBottomSheet(
        visible = uiState.isSuccessSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        ModalStatusContent(
            title = "A new card has \nbeen added!",
            subtitle = "You can now use this card for faster \ncheckout on all your orders.",
            buttonTitle = "Confirm",
            onClick = viewModel::onDismissSheet
        ) {
            SuccessIcon()
        }
    }
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
    onImageSelected:(PatternImage) -> Unit,
    onNavigateBack: () -> Unit
) {
        Scaffold(
            containerColor = Neutral10,
            bottomBar = {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ButtonComponent(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.buttonEnable,
                        labelResId = R.string.save,
                        onClick = onSaveCardCLicked
                    )
                    CustomTwoColorText(
                        modifier = Modifier.fillMaxWidth(),
                        fullText = "By adding a card, you've read & agree to our \nTerms and conditions",
                        highlightText = "Terms and conditions",
                        onHighlightClick = {}
                    )
                }
            },

            topBar = {
                AddCardTopAppBar(
                    onAddClick = {},
                    onBackClick = onNavigateBack
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
                        TextSection(
                            label = "Card holder's name",
                            placeholder = "Enter name",
                            text = uiState.cardName,
                            leadingIcon = R.drawable.person,
                            onTextChanged = onCardNameChange,
                            textError = ""
                        )

                        TextTransformationSection(
                            label = "Card number",
                            placeholder = "Enter number",
                            text = uiState.cardNumber,
                            leadingIcon = R.drawable.credit_card,
                            onTextChanged = onCardNumberChange,
                            visualTransformation = Transformations.CardNumber()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TextTransformationSection(
                                modifier = Modifier.weight(1f),
                                label = "Expiry Date",
                                placeholder = "MM/YY",
                                text = uiState.expireDate,
                                leadingIcon = R.drawable.calendar,
                                onTextChanged = onExpireDateChange,
                                visualTransformation = Transformations.ExpiryDateTransformation()
                            )

                            TextTransformationSection(
                                modifier = Modifier.weight(1f),
                                label = "CVV",
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
                            onClick = onImageSelected
                        )

                        CardColorListContent(
                            selectedColor = uiState.selectedColor,
                            colorOptions = uiState.colorOptions,
                            onClick = onColorSelected
                        )
                    }
                }
            }
        }
    }

@Composable
fun CardBackgroundListContent(
    selectedPatternRes: PatternImage,
    option: CardColorOption,
    patterns: List<PatternImage>,
    onClick: (PatternImage) -> Unit
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
                    patternImage = resId,
                    isSelected = resId.id == selectedPatternRes.id,
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
    onClick:(CardColorOption) -> Unit
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
    val mockUiState = AddCardUiState(
        selectedColor = colorList[3],
        selectedImage = patterns[0],
        cardName = "Luna Fiorenza",
        cardNumber = "4242424242424242",
        expireDate = "1226",
        cvv = "123",
        isLoading = true
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AddCardContent(
            uiState = mockUiState,
            onCardNumberChange = {},
            onCardNameChange = {},
            onCvvNumberChange = {},
            onExpireDateChange = {},
            onSaveCardCLicked = {},
            onColorSelected = {},
            onImageSelected = {},
            onNavigateBack = {}
        )
    }
}