package com.example.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class ReviewRestaurantRequest (
    @SerializedName("restaurant_id")
    val restaurantId: String,
    val rating: Int,
    val comment: String,
)