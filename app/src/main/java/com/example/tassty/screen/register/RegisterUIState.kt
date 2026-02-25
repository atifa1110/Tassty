package com.example.tassty.screen.register

data class RegisterUiState (
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isTermSelected: Boolean = false,
    val isLoading: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetMessage: String? = null
){
    val isRegisterButtonEnabled: Boolean
        get() = fullName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                isTermSelected &&
                !isLoading
}

sealed interface RegisterEvent {
    data object ShowBottomSheet : RegisterEvent
    data object NavigateToVerify : RegisterEvent
}