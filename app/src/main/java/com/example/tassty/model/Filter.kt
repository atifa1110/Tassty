package com.example.tassty.model

import androidx.compose.ui.graphics.Color

data class ChipOption(
    val key: String,
    val label: String,
    val icon: Int? = null,
    val selectedColor: Color,
    val selectedLabelColor : Color,
    val selectedIconColor: Color,
    val selectedBorderColor: Color,
    val isSelected : Boolean = false
)

data class FilterState(
    val sort: String = "",
    val rating: String = "",
    val promo: String = "",
    val delivery: String = "",
    val extras: Map<String, String> = emptyMap()
)


// Data Class untuk Opsi Radio Button (Hanya bisa pilih satu)
data class RadioFilterOption(
    val label: String,
    val key: String // Digunakan untuk melacak opsi yang dipilih
)

// Data Class untuk Opsi Tombol (Toggle/Chip Filter)
data class ChipFilterOption(
    val label: String,
    val iconId: Int? = null // Resource ID untuk ikon (misal: R.drawable.ic_star)
)