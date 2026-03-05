package com.example.tassty.screen.card

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel

data class CardUiState(
    val cardPayment: Resource<List<CardUserUiModel>> = Resource()
)
