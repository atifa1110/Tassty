package com.example.tassty.screen.resetpassword

import com.example.tassty.util.UiText

data class ResetPasswordUiState(
    val password: String = "",
    val passwordError: UiText? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText? = null,
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isTextEditable: Boolean = true,
    val errorMessage: String? = null,
    val isBottomSheetVisible: Boolean = false
)

data class ResetPasswordInternalState(
    val password: String = "",
    val passwordError: UiText? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isBottomSheetVisible: Boolean = false
)

sealed class ResetPasswordEvent{
    data class ShowMessage(val message: String) : ResetPasswordEvent()
}
