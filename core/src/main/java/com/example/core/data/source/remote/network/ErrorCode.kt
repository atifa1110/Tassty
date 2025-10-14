package com.example.core.data.source.remote.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

enum class ErrorCode(val code: Int) {
    NETWORK_IO(1001),
    TIMEOUT(1002),
    SERVER(1003),
    UNKNOWN(1004);

    companion object {
        fun fromThrowable(t: Throwable): ErrorCode = when (t) {
            is SocketTimeoutException -> TIMEOUT
            is IOException -> NETWORK_IO
            is HttpException -> SERVER
            else -> UNKNOWN
        }
    }
}
