package com.example.tassty.screen.login

data class LoginUiState(
    val email: String = "",
    val emailError: String? = "",
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetMessage: String? = null
)

sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data object ShowBottomSheet : LoginEvent
}