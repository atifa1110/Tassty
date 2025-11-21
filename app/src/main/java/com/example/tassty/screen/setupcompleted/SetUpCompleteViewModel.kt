package com.example.tassty.screen.setupcompleted

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.screen.register.RegisterEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface SetUpCompleteEvent {
    data object NavigateToHome : SetUpCompleteEvent
}

@HiltViewModel
class SetUpCompleteViewModel @Inject constructor(
    private val updateAuthStatusUseCase: UpdateAuthStatusUseCase
): ViewModel(){

    private val _events = MutableSharedFlow<SetUpCompleteEvent>()
    val events: SharedFlow<SetUpCompleteEvent> = _events.asSharedFlow()

    fun updateLogin() {
        viewModelScope.launch {
            updateAuthStatusUseCase { currentStatus ->
                currentStatus.copy(
                    isLoggedIn = true,
                )
            }
            _events.emit(SetUpCompleteEvent.NavigateToHome)
        }
    }
}