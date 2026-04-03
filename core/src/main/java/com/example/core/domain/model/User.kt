package com.example.core.domain.model


data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImage: String,
    val categoryIds: List<Category>
)
