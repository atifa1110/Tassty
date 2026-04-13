package com.example.core.ui.model

import com.google.android.gms.maps.model.LatLng

data class RouteInfoUiModel(
    val distance: String,
    val duration: String,
    val polylinePoints: List<LatLng>
)

data class RouteOrderUiModel(
    val distance: String,
    val duration: String,
    val polylinePoints: List<LatLng>,
    val status: String
)
