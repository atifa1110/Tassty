package com.example.core.ui.model

data class UserUiModel(
    val id: String,
    val email: String,
    val name: String,
    val profileImage: String,
    val categoryIds: List<CategoryUiModel>
)
