package com.example.tassty.screen.login

import com.example.tassty.util.UiText

/** * UI State that is consumed by the Composable.
 * Represents the final data shown on the screen.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val isLoading: Boolean = false,
    val isTextEditable: Boolean = true,
    val isButtonEnabled: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetMessage: String? = null
)

/** * Internal state for user inputs or raw status from the API.
 * Used within the ViewModel to track state changes before mapping to UI State.
 */
data class LoginInternalState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val bottomSheetMessage: String? = null,
    val emailError: UiText? = null,
    val passwordError: UiText? = null
)

/** * One-time events sent from the ViewModel to the UI (e.g., navigation, showing dialogs).
 */
sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
}