package com.example.core.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class RouteDto(
    @SerializedName("distance")
    val distance: String?= null,

    @SerializedName("duration")
    val duration: String? = null,

    @SerializedName("polylinePoints")
    val polylinePoints: String? = null,

    @SerializedName("status")
    val status: String? = null
)

@Serializable
data class RouteUpdatePayload(
    val currentStepIndex: Int,
    val isSimulated: Boolean
)