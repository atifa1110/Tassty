package com.example.tassty.screen.addcard

import com.example.tassty.R
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.PatternImage
import com.example.tassty.model.colorList
import com.example.tassty.model.patterns

data class AddCardUiState(
    val colorOptions: List<CardColorOption> = colorList,
    val backgroundPatterns: List<PatternImage> = patterns,
    val selectedImage: PatternImage = patterns[0],
    val selectedColor: CardColorOption = colorList[0],
    val cardName: String  = "",
    val cardNumber : String =  "",
    val expireDate : String = "",
    val cvv : String = "",
    val buttonEnable: Boolean = false,
    val setupIntentClientSecret: String = "",
    val errorMessage: String="",
    val isLoading: Boolean = false
)

sealed class AddCardUiEffect {
    object NavigateBack : AddCardUiEffect()
    data class ShowError(val message: String) : AddCardUiEffect()
    object ShowCanceledMessage : AddCardUiEffect()
}