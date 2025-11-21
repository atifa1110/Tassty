package com.example.core.data.source.remote.request

data class RegisterRequest (
    val email: String,
    val password: String,
    val fullName: String
)