package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class AuthDto(
    // --- Data User & Profile ---
    @SerializedName("user_id")
    val userId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("profile_image")
    val profileImage: String? = null,

    @SerializedName("address_name")
    val addressName: String? = null,

    // --- Data Security / Tokens ---
    @SerializedName("access_token")
    val accessToken: String? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    @SerializedName("stream_token")
    val streamToken: String? = null,

    // --- Data OTP / Signup Status ---
    @SerializedName("expires_in")
    val expiresIn: Int? = null,

    @SerializedName("resend_available_in")
    val resendAvailableIn: Int? = null
)