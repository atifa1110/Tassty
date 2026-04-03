package com.example.core.data.model

data class AuthStatus(
    val isLoggedIn: Boolean = false,
    val registrationStep: RegistrationStep = RegistrationStep.NONE,
    val isVerified: Boolean = false,
    val hasCompletedSetup: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? =null,
    val role: String? = null,
    val email: String? = null,
    val name: String? = null,
    val profileImage: String?= null,
    val addressName: String? = null,
    val accessToken: String?=null,
    val refreshToken: String?=null,
    val streamToken: String?=null,
    val firebaseToken: String? = null
)

enum class RegistrationStep {
    NONE,
    REGISTERING,
    AWAITING_CONFIRMATION,
    REGISTERED,
    VERIFIED
}

