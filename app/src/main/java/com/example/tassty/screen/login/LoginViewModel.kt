package com.example.tassty.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.LoginEmailPasswordUseCase
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
class LoginViewModel @Inject constructor(
    private val loginEmailPasswordUseCase: LoginEmailPasswordUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(LoginInternalState())

    val uiState: StateFlow<LoginUiState> = _internalState.map { state ->
        LoginUiState(
            email = state.email,
            password = state.password,
            emailError = state.emailError,
            passwordError = state.passwordError,
            isLoading = state.isLoading,
            isTextEditable = !state.isLoading,
            isButtonEnabled = state.email.isNotBlank() && state.password.isNotBlank() && !state.isLoading,
            isBottomSheetVisible = state.isBottomSheetVisible,
            bottomSheetMessage = state.bottomSheetMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LoginUiState()
    )

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onEmailChange(email: String) {
        _internalState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _internalState.update { it.copy(password = password, passwordError = null) }
    }

    fun onDismissBottomSheet() {
        _internalState.update {
            it.copy(isBottomSheetVisible = false, bottomSheetMessage = null)
        }
    }

    fun onLogin() {
        val currentState = _internalState.value

        val emailErrorRes = InputValidator.validateEmail(currentState.email)
        val passwordErrorRes = InputValidator.validatePassword(currentState.password)

        if (emailErrorRes != null || passwordErrorRes != null) {
            _internalState.update {
                it.copy(
                    emailError = emailErrorRes?.let { res -> UiText.StringResource(res) },
                    passwordError = passwordErrorRes?.let { res -> UiText.StringResource(res) }
                )
            }
            return
        }

        viewModelScope.launch {
            loginEmailPasswordUseCase(currentState.email, currentState.password)
                .collect { result ->
                    when (result) {
                        is TasstyResponse.Loading -> {
                            _internalState.update { it.copy(isLoading = true) }
                        }
                        is TasstyResponse.Success -> {
                            _internalState.update { it.copy(isLoading = false) }
                            _events.emit(LoginEvent.NavigateToHome)
                        }
                        is TasstyResponse.Error -> {
                            _internalState.update {
                                it.copy(
                                    isLoading = false,
                                    bottomSheetMessage = result.meta.message,
                                    isBottomSheetVisible = true
                                )
                            }
                        }
                    }
                }
        }
    }
}