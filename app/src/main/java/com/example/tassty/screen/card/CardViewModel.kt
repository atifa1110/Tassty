package com.example.tassty.screen.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserCardUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getUserCardUseCase: GetUserCardUseCase
) : ViewModel() {

    val uiState: StateFlow<CardUiState> = getUserCardUseCase()
        .map { cards ->
            val cardUiModels = cards.toListState { it.toUiModel() }
            CardUiState(
                cardPayment = cardUiModels
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CardUiState()
        )
}