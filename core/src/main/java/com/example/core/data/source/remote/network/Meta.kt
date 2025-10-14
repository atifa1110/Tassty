package com.example.core.data.source.remote.network

data class Meta(
    val code: Int,
    val status: String,
    val message: String,
    val traceId: String? = null
)