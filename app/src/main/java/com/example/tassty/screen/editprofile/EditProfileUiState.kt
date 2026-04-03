package com.example.tassty.screen.editprofile

import android.net.Uri
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.UserUiModel

data class EditProfileUiState(
    val profile: Resource<UserUiModel> = Resource(),
    val email: String = "",
    val phone: String = "",
    val name: String = "",
    val profileImage: String = "",
    val selectedLocalUri : Uri? = null,
    val isLoading: Boolean = false
)

data class EditProfileInternalState(
    val email: String = "",
    val phone: String = "",
    val name: String = "",
    val profileImage: String ="",
    val selectedLocalUri : Uri? = null,
    val isLoading: Boolean = false
)

sealed interface EditProfileEvent {
    data class ShowMessage(val message: String) : EditProfileEvent
    object NavigateBack : EditProfileEvent
}
