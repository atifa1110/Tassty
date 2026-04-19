package com.example.tassty.screen.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.ResetPasswordUseCase
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
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel(){

    private val _internalState = MutableStateFlow(ResetPasswordInternalState())

    val uiState: StateFlow<ResetPasswordUiState> = _internalState.map { state ->
        ResetPasswordUiState(
            password = state.password,
            confirmPassword = state.confirmPassword,
            passwordError = state.passwordError,
            confirmPasswordError = state.confirmPasswordError,
            isLoading = state.isLoading,
            isBottomSheetVisible = state.isBottomSheetVisible,
            isButtonEnabled = state.password.isNotBlank() && state.confirmPassword.isNotBlank() && !state.isLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ResetPasswordUiState()
    )

    private val _events = MutableSharedFlow<ResetPasswordEvent>()
    val events: SharedFlow<ResetPasswordEvent> = _events.asSharedFlow()

    fun onPasswordChange(password: String) {
        _internalState.update {  it.copy(password = password, passwordError = null) }
    }

    fun onConfirmChange(confirm: String) {
        _internalState.update {  it.copy(confirmPassword = confirm, confirmPasswordError = null) }
    }

    fun onValidateReset() {
        val state = _internalState.value
        val password = state.password
        val confirm = state.confirmPassword

        val passwordErrorRes = InputValidator.validatePassword(password)
        val confirmErrorRes = InputValidator.validateConfirmPassword(password, confirm)

        if(passwordErrorRes!= null || confirmErrorRes!= null) {
            _internalState.update {
                it.copy(
                    passwordError = passwordErrorRes?.let { res -> UiText.StringResource(res, 8) },
                    confirmPasswordError =  confirmErrorRes?.let { res -> UiText.StringResource(res) }
                )
            }
            return
        }

        viewModelScope.launch {
            resetPasswordUseCase(password).collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(isLoading = false, errorMessage = result.meta.message) }
                        _events.emit(ResetPasswordEvent.ShowMessage(result.meta.message))
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success -> {
                        _internalState.update { it.copy(isLoading = false, isBottomSheetVisible = true) }
                    }
                }
            }
        }
    }
}