package com.example.core.domain.utils

import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse

inline fun <T, R> TasstyResponse<T>.mapToResource(
    crossinline mapper: (T) -> R
): Resource<R> = when (this) {
    is TasstyResponse.Loading ->
        Resource(isLoading = true)

    is TasstyResponse.Error ->
        Resource(
            errorMessage = meta.message,
            isLoading = false
        )

    is TasstyResponse.Success ->
        Resource(
            data = data?.let(mapper),
            isLoading = false
        )
}

fun <T, R> TasstyResponse<List<T>>.toListState(
    mapper: (T) -> R
): Resource<List<R>> =
    mapToResource { list ->
        list.map(mapper)
    }