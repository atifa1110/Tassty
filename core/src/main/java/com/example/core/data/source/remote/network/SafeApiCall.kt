package com.example.core.data.source.remote.network

import kotlinx.coroutines.CancellationException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> BaseResponse<T>
): TasstyResponse<T> {
    return try {
        val response = apiCall()

        if (response.meta.code in 200..299) {
            TasstyResponse.Success(
                data = response.data,
                meta = response.meta
            )
        } else {
            TasstyResponse.Error(response.meta)
        }

    } catch (e: Exception) {
        if (e is CancellationException) throw e
        TasstyResponse.Error(ErrorMapper.mapError(e))
    }
}
