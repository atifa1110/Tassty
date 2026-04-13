package com.example.core.data.source.remote.request

data class SetUpRequest(
    val addressName: String,
    val fullAddress: String,
    val landmarkDetail: String,
    val lat: Double,
    val lng: Double,
    val addressType:String,
    val categoryIds: List<String>
)
