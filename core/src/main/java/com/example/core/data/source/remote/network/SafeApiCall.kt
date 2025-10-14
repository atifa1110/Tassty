package com.example.core.data.source.remote.network

import kotlinx.coroutines.delay
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(
    simulateError: Boolean = false,
    apiCall: suspend () -> ApiResponse<T>
): ResultWrapper<T> {
    return try {
        delay(300) // simulate latency

        if (simulateError) {
            when (listOf("timeout", "io", "server").random()) {
                "timeout" -> throw SocketTimeoutException("Connection timed out")
                "io" -> throw IOException("Failed to connect")
                "server" -> throw HttpException(
                    Response.error<Any>(
                        500,
                        "Internal server error".toResponseBody("application/json".toMediaType())
                    )
                )
            }
        }

        val response = apiCall()

        response.data?.let {
            if (response.meta.code in 200..299) ResultWrapper.Success(it, response.meta)
            else ResultWrapper.Error(response.meta)
        } ?: ResultWrapper.Error(
            Meta(
                code = ErrorCode.UNKNOWN.code,
                status = "error",
                message = "Empty response data",
                traceId = java.util.UUID.randomUUID().toString()
            )
        )

    } catch (e: Exception) {
        ResultWrapper.Error(ErrorMapper.mapError(e))
    }
}
