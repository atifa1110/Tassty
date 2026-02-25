package com.example.tassty.screen.addcard

import com.example.tassty.R
import com.example.tassty.model.CardColorOption
import com.example.tassty.model.colorList
import com.example.tassty.model.patterns

data class AddCardUiState(
    val colorOptions: List<CardColorOption> = colorList,
    val backgroundPatterns: List<Int> = patterns,
    val selectedImage: Int = R.drawable.background_design,
    val selectedColor: CardColorOption = colorList[0],
    val cardName: String  = "",
    val cardNumber : String =  "",
    val expireDate : String = "",
    val cvv : String = "",
    val buttonEnable: Boolean = false
)

data class AddCardInternalState(
    val selectedImage: Int = R.drawable.background_design,
    val selectedColor: CardColorOption = colorList[0],
    val cardName: String  = "",
    val cardNumber : String =  "",
    val expireDate : String = "",
    val cvv : String = "",
    val buttonEnable: Boolean = false
)