package com.example.tassty.util

import android.util.Patterns

object InputValidator {

    fun validateEmail(email: String): String? {
        return if (email.isBlank()) {
            "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email is not valid"
        } else null
    }

    fun validatePassword(password: String, minLength: Int = 8): String? {
        return if (password.isBlank()) {
            "Password cannot be empty"
        } else if (password.length < minLength) {
            "Password must be at least $minLength characters"
        } else null
    }

//    fun validatePassword(password: String, minLength: Int = 8): String? {
//        val hasUppercase = password.any { it.isUpperCase() }
//        val hasLowercase = password.any { it.isLowerCase() }
//        val hasDigit = password.any { it.isDigit() }
//
//        return when {
//            password.isBlank() -> "Password cannot be empty"
//            password.length < minLength -> "Password must be at least $minLength characters"
//            !hasUppercase -> "Password must contain at least one uppercase letter"
//            !hasLowercase -> "Password must contain at least one lowercase letter"
//            !hasDigit -> "Password must contain at least one number"
//            else -> null
//        }
//    }

    fun validateNotEmpty(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName cannot be empty" else null
    }

    fun validateConfirmPassword(password: String, confirm: String): String? {
        return when {
            confirm.isBlank() -> "Confirm password cannot be empty"
            password != confirm -> "Passwords do not match"
            else -> null
        }
    }
}