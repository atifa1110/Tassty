package com.example.tassty.screen.login

object InputValidator {

    // Validate Email
    fun validateEmail(email: String): String? {
        return if (email.isBlank()) {
            "Email tidak boleh kosong"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Email tidak valid"
        } else null
    }

    // Validate Password
    fun validatePassword(password: String, minLength: Int = 6): String? {
        return if (password.isBlank()) {
            "Password tidak boleh kosong"
        } else if (password.length < minLength) {
            "Password minimal $minLength karakter"
        } else null
    }

    // Another Empty Validate
    fun validateNotEmpty(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName tidak boleh kosong" else null
    }
}
