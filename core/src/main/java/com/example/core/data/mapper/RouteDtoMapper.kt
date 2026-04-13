package com.example.core.data.mapper

import com.example.core.data.model.RouteDto
import com.example.core.domain.model.Route

fun RouteDto.toDomain(): Route{
    return Route(
        distance = this.distance?:"",
        duration = this.duration?:"",
        polylinePoints = this.polylinePoints?:"",
        status = this.status ?:""
    )
}