package com.example.core.data.source.remote.request

data class EditRequest(
    val name: String,
    val profileImage: String,
    val phone: String,
    val categoryIds: List<String>
)
