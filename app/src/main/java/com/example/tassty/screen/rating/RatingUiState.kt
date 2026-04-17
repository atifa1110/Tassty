package com.example.tassty.screen.rating

import com.example.tassty.RatingType
import com.example.tassty.navigation.RatingNavArg
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.collections.listOf

data class RatingUiState(
    val headerName: String = "",
    val orderId: String = "",
    val orderNumber: String = "",
    val orderItemId: String = "",
    val createdAt: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val type : RatingType = RatingType.MENU,
    val tagList : ImmutableList<String> = persistentListOf("Taste", "Portion", "Price", "Freshness", "Hygiene", "Packaging"),
    val feedback: String = "",
    val selectedTags: Set<String> = setOf(),
    val rating: Int = 0,
    val isButtonEnabled : Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface RatingEvent {
    data class ShowMessage(val message: String) : RatingEvent
    data object NavigateBack : RatingEvent
}
