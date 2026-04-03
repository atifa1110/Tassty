package com.example.tassty.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(ProfileInternalState())

    val uiState = combine(
        _internalState,
        getAuthStatusUseCase(),
    ){ internal , profile ->
        ProfileUiState(
            name = profile.name?:"Guest",
            imageUrl = profile.profileImage?:"",
            email = profile.email?:"Guest",
            isLogoutSheetVisible = internal.isLogoutSheetVisible
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState()
        )


    fun handleShowLogoutSheet(visible: Boolean){
        _internalState.update { it.copy(isLogoutSheetVisible = visible) }
    }

    fun onLogout() = viewModelScope.launch {
        logoutUseCase()
    }
}