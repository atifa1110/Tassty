package com.example.tassty.screen.emailinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.ForgotPasswordUseCase
import com.example.tassty.util.InputValidator
import com.example.tassty.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailInputViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(EmailInputInternalState())

    val uiState: StateFlow<EmailInputUiState> = _internalState.map { state ->
        EmailInputUiState(
            email = state.email,
            emailError = state.emailError,
            isLoading = state.isLoading,
            isTextEditable = !state.isLoading,
            isButtonEnabled = state.email.isNotBlank() && !state.isLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EmailInputUiState()
    )

    private val _events = MutableSharedFlow<EmailInputEvent>()
    val events: SharedFlow<EmailInputEvent> = _events.asSharedFlow()

    fun onEmailChange(email: String) {
        _internalState.update {  it.copy(email = email, emailError = null) }
    }

    fun onSendOtpToEmail(){
        val state = _internalState.value
        val emailError = InputValidator.validateEmail(state.email)

        if (emailError != null) {
            _internalState.update { it.copy(emailError = UiText.StringResource(emailError)) }
            return
        }

        viewModelScope.launch {
            forgotPasswordUseCase(state.email).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(isLoading = false, errorMessage = result.meta.message) }
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success -> {
                        val data = result.data?: return@collect
                        _internalState.update { it.copy(isLoading = false) }
                        _events.emit(EmailInputEvent.NavigateToVerifyReset(data.expireIn, data.resendAvailableIn))
                    }
                }
            }
        }
    }
}