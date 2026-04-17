package com.example.tassty.screen.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserCardUseCase
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.utils.toImmutableListState
import com.example.tassty.screen.chat.ChatInternalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val getUserCardUseCase: GetUserCardUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(CardInternalState())
    val uiState: StateFlow<CardUiState> = combine(
        getUserCardUseCase(),
        _internalState
    ) { cards, internal ->
        val cardUiModels = cards.toImmutableListState { card -> card.toUiModel(
            isSwipeActionVisible = internal.revealedCardIds.contains(card.id))
        }
        CardUiState(
            cardPayment = cardUiModels
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardUiState()
    )

    fun onRevealChange(cardId: String, isRevealed: Boolean) {
        _internalState.update { state ->
            state.copy(
                revealedCardIds = if (isRevealed) {
                    setOf(cardId)
                } else {
                    emptySet()
                }
            )
        }
    }

}