package com.example.core.data.source.remote.request

data class VerifyRequest(
    val email: String,
    val verificationCode: String
)