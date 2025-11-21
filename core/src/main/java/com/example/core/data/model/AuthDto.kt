package com.example.core.data.model

data class AuthDto(
    val email: String,
    val token: String,
    val name: String,
    val profileName: String,
    val addressName: String
)