package com.example.core.data.source.remote.network

sealed class ResultWrapper<out T> {

    // Success: Membawa data sukses T dan Meta info
    data class Success<out T>(
        val data: T,
        val meta: Meta
    ) : ResultWrapper<T>()

    // Error: Hanya membawa Meta info, karena semua error (termasuk jaringan)
    // akan direpresentasikan oleh Meta.
    data class Error(
        val meta: Meta
    ) : ResultWrapper<Nothing>()

    object Loading : ResultWrapper<Nothing>()
}