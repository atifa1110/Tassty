package com.example.core.data.source.remote.network

sealed class TasstyResponse<out T> {
    data class Success<T>(
        val data: T?,
        val meta: Meta
    ) : TasstyResponse<T>()

    data class Error(
        val meta: Meta
    ) : TasstyResponse<Nothing>()

//    object Loading : TasstyResponse<Nothing>()
    data class Loading(val progress: Float = 0f) : TasstyResponse<Nothing>()
}