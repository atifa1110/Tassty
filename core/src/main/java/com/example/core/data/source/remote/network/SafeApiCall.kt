package com.example.core.data.source.remote.network

import kotlinx.coroutines.CancellationException

suspend fun <T> safeApiCall2(
    apiCall: suspend () -> BaseResponse<T>
): TasstyResponse<T> {
    return try {
        val response = apiCall()

        if (response.meta.code in 200..299) {

            val safeData = when {
                response.data == null && List::class.java.isAssignableFrom(response.data?.javaClass) -> emptyList<T>() as T
                else -> response.data
            }
            TasstyResponse.Success(
                data = safeData,
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
