package com.example.tassty.screen.verification

data class VerificationUiState(
    val otp: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isTextEditable: Boolean = true,
    val isButtonEnabled: Boolean = false,
    val errorMessage: String = "",
    val timerSeconds: Int = 60,
    val isResendEnabled: Boolean = false,
    val title: String = "",
    val instruction: String = "",
    val recoveryInfo: String = ""
)

data class VerificationInternalState(
    val otp: String = "",
    val timerSeconds: Int = 0,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)

sealed class VerificationEvent {
    data class ShowMessage(val message: String) : VerificationEvent()
    object NavigateToSetUp : VerificationEvent()
    object NavigateToNewPassword : VerificationEvent()
}