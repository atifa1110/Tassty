package com.example.tassty.screen.profile

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.UserUiModel

data class ProfileUiState(
    val name: String = "",
    val imageUrl: String = "",
    val email: String = "",
    val isDarkMode: Boolean = false,
    val isLogoutSheetVisible: Boolean = false
)

data class ProfileInternalState(
    val isLogoutSheetVisible: Boolean = false
)

sealed class ProfileEffect {
    data class ShowMessage(val message: String) : ProfileEffect()
    data object NavigateToLogin : ProfileEffect()
}