package com.example.core.ui.model


data class CartGroupUiModel(
    val restaurant : RestaurantUiModel,
    val menus : List<CartItemUiModel>
)

data class CartItemUiModel(
    val cartId: String,
    val menuId: String,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int,
    val summary: String,
    val notes: String?,
    val isSelected: Boolean,
    val isSwipeActionVisible: Boolean
){
    val formattedDisplay: String
        get() = buildString {
            // 1. Pecah summary berdasarkan koma, bersihkan spasi, lalu gabungkan dengan enter
            val bulletSummary = summary
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .joinToString("\n")

            append(bulletSummary)

            // 2. Tambahkan Notes jika ada
            if (!notes.isNullOrBlank()) {
                append("\n\nNotes: ") // Double enter supaya ada jarak dengan summary
                append(notes)
            }
        }
}