package com.example.core.domain.model

data class OtpTimer(
    val expireIn: Int, // Map dari expires_in
    val resendAvailableIn: Int // Map dari resend_available_in
)