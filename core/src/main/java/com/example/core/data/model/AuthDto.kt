package com.example.core.data.model

data class AuthDto(
    val userId: String? = "",
    val accessToken: String? = "",
    val refreshToken: String?= "",
    val steamToken: String?="",
    val name: String?="",
    val profileImage: String?="",
    val addressName: String?=""
)
