package com.example.tassty.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.RegisterEmailPasswordUseCase
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.screen.login.LoginUiState
import com.example.tassty.util.InputValidator
import com.example.tassty.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerEmailPasswordUseCase: RegisterEmailPasswordUseCase,
    private val updateAuthStatusUseCase: UpdateAuthStatusUseCase
): ViewModel() {

    private val _internalState = MutableStateFlow(RegisterInternalState())
    val uiState : StateFlow<RegisterUiState> = _internalState.map { state ->
        RegisterUiState(
            fullName = state.fullName,
            fullNameError = state.fullNameError,
            email = state.email,
            emailError = state.emailError,
            password = state.password,
            passwordError = state.passwordError,
            isTermSelected = state.isTermSelected,
            isButtonEnabled = state.fullName.isNotBlank() &&
                    state.email.isNotBlank() &&
                    state.password.isNotBlank() &&
                    state.isTermSelected &&
                    !state.isLoading,
            isTextEditable = !state.isLoading,
            isLoading = state.isLoading,
            isBottomSuccessVisible = state.isBottomSuccessVisible,
            isBottomFailedVisible = state.isBottomFailedVisible,
            bottomSheetMessage = state.bottomSheetMessage
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RegisterUiState()
    )

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onFullNameChange(newName: String) = _internalState.update { it.copy(fullName = newName, fullNameError = null) }

    fun onEmailChange(email: String) = _internalState.update { it.copy(email = email, emailError = null) }

    fun onPasswordChange(password: String) = _internalState.update { it.copy(password = password, passwordError = null) }

    fun onTermCheckChanged(isChecked: Boolean) = _internalState.update { it.copy(isTermSelected = isChecked) }

    fun onConfirmVerification() {
        val state = _internalState.value
        viewModelScope.launch {
            updateAuthStatusUseCase { current ->
                current.copy(registrationStep = RegistrationStep.REGISTERED)
            }
            _events.emit(RegisterEvent.NavigateToVerify(state.expireIn,state.resendAvailableIn))
        }
    }

    fun onDismissBottomSheet(){
        _internalState.update { it.copy(isBottomFailedVisible = false) }
    }

    fun onRegister() {
        val state = _internalState.value
        val emailErrorRes = InputValidator.validateEmail(state.email)
        val passwordErrorRes = InputValidator.validatePassword(state.password)
        val nameErrorRes = InputValidator.validateNotEmpty(state.fullName)

        if (emailErrorRes != null || passwordErrorRes != null  || nameErrorRes != null) {
            _internalState.update {
                it.copy(
                    emailError = emailErrorRes?.let { res -> UiText.StringResource(res) },
                    passwordError = passwordErrorRes?.let { res -> UiText.StringResource(res, 8) },
                    fullNameError = nameErrorRes?.let { res-> UiText.StringResource(resId = res,"Name") }
                )
            }
            return
        }

        viewModelScope.launch {
            registerEmailPasswordUseCase(uiState.value.email,
                uiState.value.password,uiState.value.fullName,"USER").collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        val serverMessage = result.meta.message

                        _internalState.update { state ->
                            if (serverMessage.contains("email", ignoreCase = true)) {
                                state.copy(
                                    isLoading = false,
                                    emailError = UiText.DynamicString(serverMessage),
                                    isBottomFailedVisible = false
                                )
                            }
                            else if (serverMessage.contains("password", ignoreCase = true)) {
                                state.copy(
                                    isLoading = false,
                                    passwordError = UiText.DynamicString(serverMessage),
                                    isBottomFailedVisible = false
                                )
                            }

                            else {
                                state.copy(
                                    isLoading = false,
                                    bottomSheetMessage = serverMessage,
                                    isBottomFailedVisible = true
                                )
                            }
                        }
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success-> {
                        val data = result.data ?: return@collect
                        _internalState.update {
                            it.copy(
                                isLoading = false,
                                isBottomSuccessVisible = true,
                                expireIn = data.expireIn,
                                resendAvailableIn = data.resendAvailableIn
                            )
                        }
                    }
                }
            }
        }
    }
}