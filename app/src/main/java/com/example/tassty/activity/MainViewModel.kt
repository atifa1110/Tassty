package com.example.tassty.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.AuthStatus
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.ObserveChatConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val observeChatConnectionUseCase: ObserveChatConnectionUseCase
) : ViewModel() {

    private val _snackbarMessage = Channel<String>(Channel.CONFLATED)
    val snackbarMessage = _snackbarMessage.receiveAsFlow()

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarMessage.send(message)
        }
    }

    val authState = getAuthStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthStatus()
        )

    val isChatConnected: StateFlow<Boolean> = observeChatConnectionUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isAuthLoaded: StateFlow<Boolean> = authState
        .transform { auth ->
            delay(2000)
            emit(true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}