package com.example.tassty.screen.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.AddCardToStripeUseCase
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.PatternImage
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the logic of adding a new card via Stripe.
 * It manages the UI state, input validation, and communication with the Stripe SDK.
 */
@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val addCardToStripeUseCase: AddCardToStripeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState

    private val _stripeTrigger = MutableStateFlow<ConfirmSetupIntentParams?>(null)
    val stripeTrigger: StateFlow<ConfirmSetupIntentParams?> = _stripeTrigger

    private val _uiEffect = Channel<AddCardUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    /**
     * Updates the UI state and automatically triggers input validation
     * to ensure the [AddCardUiState.buttonEnable] remains synchronized.
     */
    private fun updateState(transform: (AddCardUiState) -> AddCardUiState) {
        _uiState.update { current ->
            val updated = transform(current)
            updated.copy(buttonEnable = validateInput(updated))
        }
    }

    /**
     * Validates user inputs to determine if the submit button should be enabled.
     * @param state The current UI state containing card details.
     * @return True if all inputs meet the required criteria.
     */
    private fun validateInput(state: AddCardUiState): Boolean {
        return state.cardName.isNotBlank() &&
                state.cardNumber.length == 16 &&
                state.expireDate.length == 4 &&
                state.cvv.length >= 3
    }

    /**
     * Handles changes to the cardholder name input.
     */
    fun onCardNameChange(newName: String) {
        updateState { it.copy(cardName = newName) }
    }

    /**
     * Handles changes to the card number input, allowing only digits up to 16 characters.
     */
    fun onCardNumberChange(newNumber: String) {
        val clean = newNumber.filter { it.isDigit() }
        if (clean.length <= 16) {
            updateState { it.copy(cardNumber = clean) }
        }
    }

    /**
     * Handles changes to the expiration date input (MMYY format).
     */
    fun onExpireDateChange(newDate: String) {
        val clean = newDate.filter { it.isDigit() }
        if (clean.length <= 4) {
            updateState { it.copy(expireDate = clean) }
        }
    }

    /**
     * Handles changes to the CVV input, limited to 3 characters.
     */
    fun onCvvChange(newCvv: String) {
        val clean = newCvv.filter { it.isDigit() }
        if (clean.length <= 3) {
            updateState { it.copy(cvv = clean) }
        }
    }

    /**
     * Updates the selected color option for the card preview.
     */
    fun onColorSelected(color: CardColorOption) {
        updateState { it.copy(selectedColor = color) }
    }

    /**
     * Updates the selected pattern/background image for the card preview.
     */
    fun onPatternSelected(pattern: PatternImage) {
        updateState { it.copy(selectedImage = pattern) }
    }

    /**
     * Initiates the card saving process by requesting a Setup Intent secret from the backend.
     * On success, it triggers the Stripe SDK confirmation flow.
     */
    fun onSaveClicked() {
        viewModelScope.launch {
            addCardToStripeUseCase.getSecret().collect { response ->
                when (response) {
                    is TasstyResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is TasstyResponse.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.meta.message)
                        }
                    }

                    is TasstyResponse.Success -> {
                        val secret = response.data ?: return@collect
                        _uiState.update { it.copy(isLoading = true, setupIntentClientSecret = secret) }
                        _stripeTrigger.value = createStripeParams(secret)
                    }
                }
            }
        }
    }

    /**
     * Resets the Stripe trigger state after the SDK has been launched.
     */
    fun onStripeLaunched() {
        _stripeTrigger.value = null
    }

    /**
     * Handles failures occurring within the Stripe SDK flow.
     * @param message The error message returned by Stripe.
     */
    fun onStripeFailed(message: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false) }
            _uiEffect.send(
                AddCardUiEffect.ShowMessage(message ?: "Stripe payment verification failed")
            )
        }
    }

    /**
     * Resets the loading state and notifies the UI when the user cancels the Stripe process.
     */
    fun onStripeCanceled() {
        _uiState.update { it.copy(isLoading = false) }
        viewModelScope.launch {
            _uiEffect.send(AddCardUiEffect.ShowMessage("Payment canceled"))
        }
    }

    /**
     * Called after successful Stripe validation.
     * Finalizes the process by sending the PaymentMethod ID and card customization to the backend.
     * @param paymentMethodId The ID generated by Stripe for the verified card.
     */
    fun onStripeValidationSuccess(paymentMethodId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            addCardToStripeUseCase.finalize(
                id = paymentMethodId,
                color = state.selectedColor.id,
                background = state.selectedImage.id
            ).collect { response ->
                when (response) {
                    is TasstyResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is TasstyResponse.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.meta.message)
                        }
                    }

                    is TasstyResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false, isSuccessSheetVisible = true) }
                    }
                }
            }
        }
    }

    fun onDismissSheet() = viewModelScope.launch {
        _uiState.update { it.copy(isSuccessSheetVisible = false)}
        _uiEffect.send(AddCardUiEffect.NavigateBack)
    }

    /**
     * Constructs the necessary parameters for the Stripe Setup Intent.
     * @param clientSecret The secret obtained from the backend.
     * @return Prepared [ConfirmSetupIntentParams] for the Stripe SDK.
     */
    private fun createStripeParams(clientSecret: String): ConfirmSetupIntentParams {
        val state = _uiState.value

        val month = state.expireDate.take(2).toIntOrNull() ?: 0
        val year = state.expireDate.takeLast(2).toIntOrNull() ?: 0

        val cardParams = PaymentMethodCreateParams.Card.Builder()
            .setNumber(state.cardNumber)
            .setExpiryMonth(month)
            .setExpiryYear(2000 + year)
            .setCvc(state.cvv)
            .build()

        return ConfirmSetupIntentParams.create(
            PaymentMethodCreateParams.create(cardParams, null),
            clientSecret
        )
    }
}