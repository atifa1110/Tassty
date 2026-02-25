package com.example.tassty.screen.addcard

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.AddCardToStripeUseCase
import com.example.tassty.model.CardColorOption
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val addCardToStripeUseCase: AddCardToStripeUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(AddCardInternalState())

    private val _apiStatus = MutableStateFlow<TasstyResponse<String>>(TasstyResponse.Success("",
        Meta(0,"","","")))
    val uiState: StateFlow<AddCardUiState> = combine(
        _internalState,
        _apiStatus
    ) { internal, api ->

        AddCardUiState(
            selectedImage = internal.selectedImage,
            selectedColor = internal.selectedColor,
            cardName = internal.cardName,
            cardNumber = internal.cardNumber,
            expireDate = internal.expireDate,
            cvv = internal.cvv,
            buttonEnable = validateInput(internal) && api !is TasstyResponse.Loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AddCardUiState()
    )

    fun onCardNameChange(newName: String) {
        _internalState.update { it.copy(cardName = newName) }
    }

    fun onCardNumberChange(newNumber: String) {
        val cleanNumber = newNumber.filter { it.isDigit() }
        if (cleanNumber.length <= 16) {
            _internalState.update { it.copy(cardNumber = cleanNumber) }
        }
    }

    fun onExpireDateChange(newDate: String) {
        if (newDate.length <= 5) {
            _internalState.update { it.copy(expireDate = newDate) }
        }
    }

    fun onCvvChange(newCvv: String) {
        if (newCvv.length <= 4) {
            _internalState.update { it.copy(cvv = newCvv) }
        }
    }

    fun onColorSelected(newColor: CardColorOption) {
        _internalState.update { it.copy(selectedColor = newColor) }
    }

    fun onPatternSelected(newPatternRes: Int) {
        _internalState.update { it.copy(selectedImage = newPatternRes) }
    }

    private fun validateInput(state: AddCardInternalState): Boolean {
        return state.cardName.isNotBlank() &&
                state.cardNumber.length == 16 &&
                state.expireDate.length == 5 &&
                state.cvv.length >= 3
    }

    fun onSaveClicked(activity: ComponentActivity) {
        viewModelScope.launch {
            // Step 1: Ambil Client Secret dari Backend
            addCardToStripeUseCase.getSecret().collect { response ->
                _apiStatus.value = response // Set status Loading/Error/Success ke UI

                if (response is TasstyResponse.Success) {
                    val secret = response.data ?: return@collect

                    // Step 2: Siapkan Params & Panggil SDK Stripe
                    // (Ini akan membuka popup/layar Stripe)
                    val params = createStripeParams(secret)
                    addCardToStripeUseCase.launchStripe(activity, params)

                    // Note: Setelah ini alur berhenti dulu karena pindah ke layar Stripe.
                    // Hasilnya akan ditangkap di Activity/Fragment lewat callback.
                }
            }
        }
    }

    // Fungsi ini dipanggil dari Activity setelah Stripe sukses (dapet PaymentMethod ID)
    fun onStripeValidationSuccess(paymentMethodId: String) {
        viewModelScope.launch {
            // Step 3: Kirim PM_ID dan warna ke Backend kita
            val selectedColorId = _internalState.value.selectedColor.id

            addCardToStripeUseCase.finalize(paymentMethodId, selectedColorId).collect { response ->
                _apiStatus.value = response
                if (response is TasstyResponse.Success) {
                    // Berhasil simpan kartu! Bisa navigasi balik atau munculin success dialog
                }
            }
        }
    }

    // Helper untuk buat parameter Stripe
    private fun createStripeParams(clientSecret: String): ConfirmSetupIntentParams {
        val state = _internalState.value

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