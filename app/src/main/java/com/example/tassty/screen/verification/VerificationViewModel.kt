package com.example.tassty.screen.verification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.ResendVerificationUseCase
import com.example.core.domain.usecase.VerifyEmailUseCase
import com.example.core.domain.utils.mapToResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class VerificationViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val resendVerificationUseCase: ResendVerificationUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(VerificationUiState())
    val uiState: StateFlow<VerificationUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<VerificationEvent>()
    val event: SharedFlow<VerificationEvent> = _event.asSharedFlow()

    val email = getAuthStatusUseCase()
        .map { it.email ?: "" }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(500),
            ""
        )

    init {
        startTimer()
    }
    private var timerJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                _uiState.update { it.copy(
                    timerSeconds = i,
                    isResendEnabled = i == 0
                )}
                delay(1000)
            }
        }
    }
    fun onOtpChange(otp: String) {
        val filteredValue = otp.filter { it.isDigit() }
        val truncatedValue = filteredValue.take(6)
        _uiState.update {
            it.copy(
                otp = truncatedValue,
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun dismissError(){
        _uiState.update {
            it.copy(
                otp = "",
                isError = false,
                errorMessage = ""
            )
        }
    }

    fun resendVerification(){
        if (email.value.isEmpty()) return

        startTimer()

        viewModelScope.launch {
            resendVerificationUseCase(email.value).collect { response ->
                when(response){
                    is TasstyResponse.Loading -> {}
                    is TasstyResponse.Success -> {
                        _event.emit(VerificationEvent.Snackbar(response.meta.message))
                    }
                    is TasstyResponse.Error -> _uiState.update {
                        timerJob?.cancel()
                        it.copy(
                            isError = true,
                            errorMessage = response.meta.message,
                            isResendEnabled = true
                        )
                    }
                }
            }
        }
    }

    fun verifyEmail(){
        viewModelScope.launch {
            verifyEmailUseCase.invoke(email.value, uiState.value.otp)
                .collect { result ->
                    when(result) {
                        is TasstyResponse.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is TasstyResponse.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            Log.d("VerificationViewModel", result.data.toString())
                            _event.emit(VerificationEvent.NavigateToSetUp)
                        }

                        is TasstyResponse.Error -> {
                            Log.d("VerificationViewModel",result.meta.message)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    isError = true,
                                    errorMessage = result.meta.message
                                )
                            }
                        }
                    }
                }
        }
    }
}