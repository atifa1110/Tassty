package com.example.tassty.screen.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.VerifyEmailUseCase
import com.example.tassty.screen.login.handleMinimumDelay
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
class VerificationViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val getAuthStatusUseCase: GetAuthStatusUseCase
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

    fun onOtpChange(otp: String) {
        val filteredValue = otp.filter { it.isDigit() }
        val truncatedValue = filteredValue.take(6)
        _uiState.update {
            it.copy(
                otp = truncatedValue,
                isOtpInvalid = false,
                isShowGlobalError = false,
                globalErrorMessage = ""
            )
        }
    }

    fun dismissError(){
        _uiState.update {
            it.copy(
                isOtpInvalid = false,
                isShowGlobalError = false,
                globalErrorMessage = ""
            )
        }
    }

    fun verifyEmail(){
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            verifyEmailUseCase.invoke(email.value, uiState.value.otp)
                .collect { result ->
                when(result){
                    is TasstyResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success -> {
                        handleMinimumDelay(startTime)
                        _uiState.update { it.copy(isLoading = false) }
                        _event.emit(VerificationEvent.NavigateToSetUp)
                    }
                    is TasstyResponse.Error -> _uiState.update {
                        handleMinimumDelay(startTime)
                        it.copy(
                            isLoading = false,
                            isOtpInvalid = true,
                            isShowGlobalError = true,
                            globalErrorMessage = result.meta.message
                        )
                    }
                }
            }
        }
    }
}