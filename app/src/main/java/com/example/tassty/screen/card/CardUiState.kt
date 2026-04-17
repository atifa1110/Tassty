package com.example.tassty.screen.card

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import kotlinx.collections.immutable.ImmutableList

data class CardUiState(
    val cardPayment: Resource<ImmutableList<CardUserUiModel>> = Resource()
)

data class CardInternalState(
    val revealedCardIds: Set<String> = emptySet(),
    val isDeleteCardSheetVisible: Boolean = false,
    val selectedCard: CardUserUiModel? = null
)

