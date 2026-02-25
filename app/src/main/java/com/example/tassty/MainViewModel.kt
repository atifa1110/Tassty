package com.example.tassty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.AuthStatus
import com.example.core.domain.usecase.GetAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase
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
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthStatus()
        )

    // 3. Gabungkan isAuthLoaded agar lebih reaktif (Splash Screen Logic)
    // Alih-alih pakai init collect, kita buat Flow yang bakal bernilai true
    // setelah data pertama kali masuk (ditambah delay jika memang ingin splash screen agak lama)
    val isAuthLoaded: StateFlow<Boolean> = authState
        .transform { auth ->
            delay(3000)
            emit(true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}