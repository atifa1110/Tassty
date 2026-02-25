package com.example.core.domain.model

data class Favorite (
    val id: String,
    val name: String,
    val imageUrl: String,
    val categories : String,
    val city: String,
    val distance: Int,
    val rating: Double
)