package com.example.core.data.source.remote.network

data class Resource<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)