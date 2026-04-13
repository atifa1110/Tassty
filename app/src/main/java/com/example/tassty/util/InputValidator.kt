package com.example.tassty.util

import android.util.Patterns
import androidx.compose.ui.res.stringResource
import com.example.tassty.R

object InputValidator {

    fun validateEmail(email: String): Int? {
        return if (email.isBlank()) {
            R.string.email_cannot_be_empty
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            R.string.email_is_not_valid
        } else null
    }

    fun validatePassword(password: String, minLength: Int = 8): Int? {
        return when {
            password.isBlank() -> R.string.password_cannot_be_empty
            password.length < minLength -> R.string.password_too_short
            else -> null
        }
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

    fun validateNotEmpty(value: String): Int? {
        return if (value.isBlank()) R.string.error_field_empty else null
    }

    fun validateConfirmPassword(password: String, confirm: String): Int? {
        return when {
            confirm.isBlank() -> R.string.confirm_password_cannot_be_empty
            password != confirm -> R.string.passwords_do_not_match
            else -> null
        }
    }
}