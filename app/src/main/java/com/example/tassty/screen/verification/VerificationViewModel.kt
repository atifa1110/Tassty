package com.example.tassty.screen.verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.ForgotPasswordUseCase
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.ResendEmailOtpUseCase
import com.example.core.domain.usecase.VerifyEmailOtpUseCase
import com.example.core.domain.usecase.VerifyResetOtpUseCase
import com.example.tassty.VerificationType
import com.example.tassty.navigation.VerifyDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase,
    private val verifyResetOtpUseCase: VerifyResetOtpUseCase,
    private val resendEmailOtpUseCase: ResendEmailOtpUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
): ViewModel() {

    private val args = VerifyDestination.getArgs(savedStateHandle)

    private val _internalState = MutableStateFlow(VerificationInternalState())

    private val _resendTrigger = MutableSharedFlow<Int>(replay = 1).apply {
        tryEmit(1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _timerSeconds = _resendTrigger.flatMapLatest { command ->
        flow {
            if (command == 0) {
                emit(0)
                return@flow
            }

            var timeLeft = args.resendDelay
            while (timeLeft >= 0) {
                emit(timeLeft)
                if (timeLeft > 0) delay(1000)
                timeLeft--
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = args.resendDelay
    )

    val uiState: StateFlow<VerificationUiState> = combine(
        getAuthStatusUseCase(),
        _internalState,
        _timerSeconds
    ) { auth, state, timer ->
        val currentEmail = auth.email ?: ""

        VerificationUiState(
            otp = state.otp,
            timerSeconds = timer,
            isResendEnabled = timer == 0,
            isLoading = state.isLoading,
            isError = state.isError,
            email = currentEmail,
            isButtonEnabled = state.otp.length == 6 && currentEmail.isNotEmpty() && !state.isLoading,
            errorMessage = state.errorMessage,
            title = args.type.titleRes,
            instruction = args.type.instructionRes,
            recoveryInfo = args.type.recoveryInfoRes
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VerificationUiState(
            title = args.type.titleRes,
            instruction = args.type.instructionRes,
            recoveryInfo = args.type.recoveryInfoRes,
            timerSeconds = args.resendDelay
        )
    )

    private val _event = MutableSharedFlow<VerificationEvent>()
    val event: SharedFlow<VerificationEvent> = _event.asSharedFlow()

    fun startTimer() {
        _resendTrigger.tryEmit(1)
    }

    fun onOtpChange(otp: String) {
        val truncatedValue = otp.filter { it.isDigit() }.take(6)
        _internalState.update { it.copy(otp = truncatedValue, isError = false) }
    }

    fun onErrorDismiss(){
        _internalState.update { it.copy(isError = false, errorMessage = "") }
    }

    fun onVerificationCode() {
        val currentEmail = uiState.value.email
        val otp = uiState.value.otp

        if (otp.length < 6) {
            _internalState.update { it.copy(isError = true, errorMessage = "OTP harus 6 digit") }
            return
        }

        viewModelScope.launch {
            val flow = if (args.type == VerificationType.REGISTRATION) {
                verifyEmailOtpUseCase(currentEmail, otp)
            } else {
                verifyResetOtpUseCase(currentEmail, otp)
            }

            flow.collect { result ->
                handleResponse(result) {
                    if (args.type == VerificationType.REGISTRATION) {
                        _event.emit(VerificationEvent.NavigateToSetUp)
                    } else {
                        _event.emit(VerificationEvent.NavigateToNewPassword)
                    }
                }
            }
        }
    }

    fun onResendVerification() {
        val currentEmail = uiState.value.email
        if (currentEmail.isEmpty()) return

        startTimer()

        viewModelScope.launch {
            val flow = if (args.type == VerificationType.REGISTRATION) {
                resendEmailOtpUseCase(currentEmail)
            } else {
                forgotPasswordUseCase(currentEmail)
            }

            flow.collect { result->
                handleResponse(
                    result = result,
                    onError = {
                        _resendTrigger.tryEmit(0)
                    },
                    onSuccess = {
                        _event.emit(VerificationEvent.ShowMessage(it.meta.message))
                    }
                )
            }
        }
    }

    private suspend fun <T> handleResponse(
        result: TasstyResponse<T>,
        onError: suspend (String) -> Unit = {},
        onSuccess: suspend (TasstyResponse.Success<T>) -> Unit
    ) {
        when(result) {
            is TasstyResponse.Loading -> {
                _internalState.update { it.copy(isLoading = true) }
            }
            is TasstyResponse.Success -> {
                _internalState.update { it.copy(isLoading = false) }
                onSuccess(result)
            }
            is TasstyResponse.Error -> {
                _internalState.update {
                    it.copy(isLoading = false, isError = true, errorMessage = result.meta.message)
                }
                onError(result.meta.message)
            }
        }
    }
}