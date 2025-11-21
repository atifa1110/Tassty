package com.example.core.domain.model

data class AuthUser(
    val email: String,
    val token: String,
    val name: String,
    val profileName: String,
    val addressName: String
)
