package com.example.core.data.source.remote.request

data class ReviewMenuRequest (
    val rating: Int,
    val tags: String,
    val comment: String
)