package com.example.tassty.model

data class Filter(
    val label: String,
    val iconId: Int
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