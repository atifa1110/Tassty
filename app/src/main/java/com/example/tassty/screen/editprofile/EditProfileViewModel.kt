package com.example.tassty.screen.editprofile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.core.domain.usecase.GetUserProfileUseCase
import com.example.core.ui.mapper.toUiModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.UpdateUserProfileUseCase
import com.example.core.ui.utils.mapToResource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(){

    private val _internalState = MutableStateFlow(EditProfileInternalState())
    private val profileFlow = getUserProfileUseCase()
        .map { resource ->
            if (resource is TasstyResponse.Success) {
                val user = resource.data?.toUiModel()
                _internalState.update { current ->
                    current.copy(
                        name = user?.name ?: current.name,
                        email = user?.email ?: current.email,
                        profileImage = user?.profileImage ?: current.profileImage
                    )
                }
            }
            resource.mapToResource { it.toUiModel() }
        }

    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events: SharedFlow<EditProfileEvent> = _events.asSharedFlow()

    val uiState : StateFlow<EditProfileUiState> = combine(
        _internalState,
        profileFlow
    ){ internal , profileResource ->

        EditProfileUiState(
            profile = profileResource,
            email = internal.email,
            name = internal.name,
            phone = internal.phone,
            profileImage = internal.profileImage,
            selectedLocalUri = internal.selectedLocalUri,
            isLoading = internal.isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EditProfileUiState()
    )

    fun onNameChange(newName: String) {
        _internalState.update { it.copy(name = newName) }
    }

    fun onPhoneChange(newPhoneNumber: String) {
        _internalState.update { it.copy(phone = newPhoneNumber) }
    }

    fun onImageSelected(uri: Uri?) {
        _internalState.update { it.copy(selectedLocalUri = uri) }
    }
    fun saveProfile(){
        val state = uiState.value
        val imageUri = state.selectedLocalUri
        viewModelScope.launch {
            updateUserProfileUseCase(state.name, imageUri, context).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(isLoading = false) }
                        _events.emit(EditProfileEvent.ShowMessage(result.meta.message))
                        _events.emit(EditProfileEvent.NavigateBack)
                    }
                    is TasstyResponse.Loading -> {
                        _internalState.update { it.copy(isLoading = true) }
                    }
                    is TasstyResponse.Success -> {
                        _internalState.update { it.copy(isLoading = false) }
                        _events.emit(EditProfileEvent.ShowMessage(result.data?:""))
                    }
                }
            }
        }
    }
}