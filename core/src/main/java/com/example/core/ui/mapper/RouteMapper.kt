package com.example.core.ui.mapper

import com.google.maps.android.PolyUtil
import com.example.core.domain.model.Route
import com.example.core.ui.model.RouteInfoUiModel
import com.example.core.ui.model.RouteOrderUiModel
import com.google.android.gms.maps.model.LatLng

fun Route.toUiInfoModel(): RouteInfoUiModel{
    return RouteInfoUiModel(
        distance = this.distance,
        duration = this.duration,
        polylinePoints = PolyUtil.decode(this.polylinePoints)
    )
}

fun Route.toUiOrderModel(): RouteOrderUiModel {
    val points = if (this.polylinePoints.isNotEmpty()) {
        PolyUtil.decode(this.polylinePoints)
    } else {
        emptyList()
    }


    return RouteOrderUiModel(
        distance = this.distance,
        duration = this.duration,
        polylinePoints = points,
        status = this.status
    )
}