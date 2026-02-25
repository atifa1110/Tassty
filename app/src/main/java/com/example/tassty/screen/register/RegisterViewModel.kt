package com.example.tassty.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.RegisterEmailPasswordUseCase
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.screen.login.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerEmailPasswordUseCase: RegisterEmailPasswordUseCase,
    private val updateAuthStatusUseCase: UpdateAuthStatusUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RegisterEvent>()
    val events: SharedFlow<RegisterEvent> = _events.asSharedFlow()

    fun onFullNameChange(newName: String) = _uiState.update { it.copy(fullName = newName, fullNameError = null) }
    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email, emailError = null) }
    fun onPasswordChange(password: String) = _uiState.update { it.copy(password = password, passwordError = null) }
    fun onTermCheckChanged(isChecked: Boolean) = _uiState.update { it.copy(isTermSelected = isChecked) }

    fun onUserConfirmVerification() {
        viewModelScope.launch {
            updateAuthStatusUseCase { current ->
                current.copy(registrationStep = RegistrationStep.REGISTERED)
            }
            _events.emit(RegisterEvent.NavigateToVerify)
        }
    }

    fun setBottomSheetVisible(isVisible: Boolean) {
        _uiState.update { it.copy(isBottomSheetVisible = isVisible) }
    }

    fun register() {
        val state = _uiState.value
        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)
        val fullNameError = InputValidator.validateNotEmpty(state.fullName,"Name")

        if (emailError != null || passwordError != null  || fullNameError != null) {
            _uiState.value = state.copy(emailError = emailError, passwordError = passwordError, fullNameError = fullNameError)
            return
        }

        viewModelScope.launch {
            registerEmailPasswordUseCase(uiState.value.email,
                uiState.value.password,uiState.value.fullName,"USER").collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _uiState.update { it.copy(isLoading = false, bottomSheetMessage = result.meta.message) }
                        _events.emit(RegisterEvent.ShowBottomSheet)
                    }
                    is TasstyResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success-> {
                        _uiState.update { it.copy(isLoading = false) }
                        _events.emit(RegisterEvent.ShowBottomSheet)
                    }
                }
            }
        }
    }
}