package com.example.tassty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.AuthStatus
import com.example.core.domain.usecase.GetAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase
) : ViewModel() {

    private val _isAuthLoaded = MutableStateFlow(false)
    val isAuthLoaded: StateFlow<Boolean> = _isAuthLoaded

    val authState = getAuthStatusUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AuthStatus() // default
        )

    init {
        viewModelScope.launch {
            authState.collect {
                delay(3000)
                _isAuthLoaded.value = true
            }
        }
    }
}
