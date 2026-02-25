package com.example.core.data.mapper

import com.example.core.data.model.DetailRestaurantDto
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.OperationalDay

fun DetailRestaurantDto.toDomain() : DetailRestaurant{
    return DetailRestaurant(
        id=this.id,
        name=this.name,
        imageUrl =this.imageUrl?:"",
        categories=this.categories,
        isVerified = isVerified,
        city=this.city,
        fullAddress =this.fullAddress,
        longitude=this.longitude,
        latitude =this.latitude,
        rank=this.rank?:0,
        rating=this.rating,
        totalReviews=this.totalReviews,
        distance =this.distance,
        deliveryCost = this.deliveryCost,
        deliveryTime = this.deliveryTime,
        operationalHours = this.operationalHours.map { (day, hours) ->
            OperationalDay(day = day, hours = hours)
        },
        isOpen = this.isOpen,
        closingTimeServer = this.closingTimeServer?:"",
        currentDay = this.currentDay?:"",
    )
}
