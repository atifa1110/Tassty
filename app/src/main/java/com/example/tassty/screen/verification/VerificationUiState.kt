package com.example.tassty.screen.verification

data class VerificationUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val timerSeconds: Int = 60,
    val isResendEnabled: Boolean = false
)

sealed class VerificationEvent {
    data object NavigateToSetUp : VerificationEvent()
    data class Snackbar(val message: String) : VerificationEvent()
}