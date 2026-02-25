package com.example.core.data.source.remote.request

data class VerifyCodeRequest (
    val email: String,
    val code: String
)