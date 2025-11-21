package com.example.core.data.source.remote.network

data class BaseResponse<T>(
    val meta: Meta,
    val data: T? = null
)

data class Meta(
    val code: Int,
    val status: String,
    val message: String,
    val traceId: String? = null
)
