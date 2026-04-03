package com.example.tassty.screen.emailinput

data class EmailInputUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isTextEditable: Boolean = true,
    val errorMessage: String? = null,
)

data class EmailInputInternalState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface EmailInputEvent {
    data class NavigateToVerifyReset(val expireIn: Int, val resendAvailableIn: Int) : EmailInputEvent
    data class ShowMessage(val message: String) : EmailInputEvent
}