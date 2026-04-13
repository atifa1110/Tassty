package com.example.core.ui.mapper

import com.example.core.domain.model.Favorite
import com.example.core.domain.model.LocationDetail
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.RestaurantStatusResult
import com.example.core.ui.model.RestaurantUiModel
import com.google.android.gms.maps.model.LatLng

fun Favorite.toUiModel() : RestaurantUiModel{
    return RestaurantUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        location = LatLng(0.0,0.0),
        categories = this.categories,
        rank = 0,
        rating = this.rating,
        totalReviews = 0,
        distance = this.distance,
        deliveryCost = 0,
        deliveryTime = "",
        locationDetail = LocationDetail(
            fullAddress = "",
            longitude = 0.0,
            latitude = 0.0,
            city = this.city
        ),
        statusResult = RestaurantStatusResult(RestaurantStatus.OPEN,""),
    )
}