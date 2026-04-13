package com.example.tassty.screen.register

import com.example.tassty.util.UiText

data class RegisterUiState (
    val fullName: String = "",
    val fullNameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isTermSelected: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isTextEditable: Boolean = true,
    val isLoading: Boolean = false,
    val isBottomSuccessVisible: Boolean = false,
    val isBottomFailedVisible: Boolean = false,
    val bottomSheetMessage: String? = null
)

data class RegisterInternalState (
    val fullName: String = "",
    val fullNameError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isTermSelected: Boolean = false,
    val isLoading: Boolean = false,
    val isBottomSuccessVisible: Boolean = false,
    val isBottomFailedVisible: Boolean = false,
    val bottomSheetMessage: String? = null,
    val expireIn: Int = 0,
    val resendAvailableIn: Int = 0
)

sealed interface RegisterEvent {
    data class NavigateToVerify(val expireIn: Int, val resendAvailableIn: Int) : RegisterEvent
}