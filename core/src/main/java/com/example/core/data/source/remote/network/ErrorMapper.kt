package com.example.core.data.source.remote.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.UUID

object ErrorMapper {

    fun mapError(throwable: Throwable): Meta {
        val traceId = UUID.randomUUID().toString()

        return when (throwable) {
            is SocketTimeoutException -> Meta(
                code = ErrorCode.TIMEOUT.code,
                status = "error",
                message = "Request timed out, please try again.",
                traceId = traceId
            )

            is IOException -> Meta(
                code = ErrorCode.NETWORK_IO.code,
                status = "error",
                message = "No internet connection. Please try again.",
                traceId = traceId
            )

            is HttpException -> {
                val body = throwable.response()?.errorBody()?.string()
                val message = try {
                    val json = org.json.JSONObject(body ?: "{}")
                    json.getJSONObject("meta").optString(
                        "message",
                        throwable.message()
                    )
                } catch (e: Exception) {
                    throwable.message()
                }

                Meta(
                    code = throwable.code(),
                    status = "error",
                    message = message,
                    traceId = traceId
                )
            }

            else -> Meta(
                code = ErrorCode.UNKNOWN.code,
                status = "error",
                message = "Unexpected error: ${throwable.localizedMessage ?: "Unknown"}",
                traceId = traceId
            )
        }
    }
}