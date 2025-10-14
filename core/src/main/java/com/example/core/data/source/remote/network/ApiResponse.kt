package com.example.core.data.source.remote.network

// ApiResponse<T> same
data class ApiResponse<T>(
    val meta: Meta,
    val data: T? // Data type whether null or not
)
