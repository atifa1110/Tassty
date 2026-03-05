package com.example.core.domain.model

data class OrderItem(
    val id: String,
    val quantity: Int,
    val price: Long,
    val menuName: String,
    val imageUrl: String,
    val options: String,
    val notes: String
)