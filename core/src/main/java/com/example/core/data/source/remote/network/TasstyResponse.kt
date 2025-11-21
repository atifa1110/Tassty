package com.example.core.data.source.remote.network

//sealed class TasstyResponse<out T> {
//    data class Success<T>(
//        val data: T
//    ) : TasstyResponse<T>()
//
//    data class Error(
//        val meta: Meta
//    ) : TasstyResponse<Nothing>()
//
//    object Loading : TasstyResponse<Nothing>()
//
//    companion object {
//        fun <T> success(data: T): Success<T> = Success(data)
//        fun error(meta: Meta): Error = Error(meta)
//    }
//}

sealed class TasstyResponse<out T> {
    data class Success<T>(
        val data: T?,   // <-- bisa null jika nggak ada data
        val meta: Meta  // <-- tetap ada supaya bisa baca message
    ) : TasstyResponse<T>()

    data class Error(
        val meta: Meta
    ) : TasstyResponse<Nothing>()

    object Loading : TasstyResponse<Nothing>()
}