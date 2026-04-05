package com.example.tassty.screen.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val updateAuthStatusUseCase: UpdateAuthStatusUseCase
): ViewModel(){

    fun onGetStartClick() = viewModelScope.launch {
        updateAuthStatusUseCase{ currentStatus ->
            currentStatus.copy(
                isBoardingCompleted = true
            )
        }
    }
}