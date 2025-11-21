package com.example.tassty.screen.verification

data class VerificationUiState(
    // Input State
    val otp: String = "",
    val isLoading: Boolean = false,

    // Error State (Visual Feedback untuk Input Field)
    val isOtpInvalid: Boolean = false, // True jika kode yang dimasukkan salah/expired (membuat kotak input berwarna merah)

    // Error State (General Feedback, misalnya Banner/Snackbar)
    val isShowGlobalError: Boolean = false,
    val globalErrorMessage: String = "",
)

sealed class VerificationEvent {
    data object NavigateToSetUp : VerificationEvent()
}