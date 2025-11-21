package com.example.core.data.model

data class Resource<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
