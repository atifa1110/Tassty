package com.example.core.domain.model

data class AuthUser(
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val steamToken: String,
    val name: String,
    val profileImage: String,
    val addressName: String
)
