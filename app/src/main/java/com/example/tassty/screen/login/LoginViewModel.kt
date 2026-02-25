package com.example.tassty.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.LoginEmailPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class LoginViewModel @Inject constructor(
    private val loginEmailPasswordUseCase: LoginEmailPasswordUseCase
): ViewModel(){
    // --- STATE FLOW ---
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- EVENT FLOW (SharedFlow) ---
    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    // Update email
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    // Update password
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, passwordError = null)
    }

    // Button enable: hanya cek non-empty, tanpa validasi
    val isLoginEnabled: StateFlow<Boolean> = _uiState.map { state ->
        state.email.isNotBlank() && state.password.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun resetLoginInput(){
        _uiState.update { it.copy(
            isBottomSheetVisible = false,
            bottomSheetMessage = null
        ) }
    }

    fun setBottomSheetVisible(isVisible: Boolean) {
        _uiState.update { it.copy(isBottomSheetVisible = isVisible) }
    }

    fun login() {
        val state = _uiState.value
        val emailError = InputValidator.validateEmail(state.email)
        val passwordError = InputValidator.validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _uiState.value = state.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        viewModelScope.launch {
            loginEmailPasswordUseCase.invoke(uiState.value.email, uiState.value.password)
                .collect { result ->
                    when(result) {
                        is TasstyResponse.Error -> {

                                _uiState.update {
                                    it.copy(bottomSheetMessage = result.meta.message)
                                }

                                  _uiState.update { it.copy(isLoading = false) }

                                // 4. Emit Event: Sinyal untuk Composable agar tampilkan sheet
                                _events.emit(LoginEvent.ShowBottomSheet)
                        }

                        is TasstyResponse.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is TasstyResponse.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _events.emit(LoginEvent.NavigateToHome)
                        }
                    }
                }
        }
    }
}

private val MINIMUM_LOADING_TIME = 500L
//suspend fun handleMinimumDelay(startTime: Long) {
//    val elapsedTime = System.currentTimeMillis() - startTime
//    val requiredDelay = MINIMUM_LOADING_TIME - elapsedTime
//
//    if (requiredDelay > 0) {
//        // Tunda hanya jika waktu pemrosesan API lebih cepat dari batas minimum
//        delay(requiredDelay)
//    }
//}
