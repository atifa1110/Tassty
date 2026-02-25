package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantDto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("categories")
    val categories: List<String> = emptyList(),

    @SerializedName("city")
    val city: String = "",

    @SerializedName("full_address")
    val fullAddress: String = "",

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("rank")
    val rank: Int? = null,

    @SerializedName("rating")
    val rating: Double = 0.0,

    @SerializedName("total_reviews")
    val totalReviews: Int = 0,

    @SerializedName("distance")
    val distance: Int = 0,

    @SerializedName("delivery_cost")
    val deliveryCost: Int = 0,

    @SerializedName("delivery_time")
    val deliveryTime: String = "",

    @SerializedName("is_open")
    val isOpen: Boolean = false,

    @SerializedName("closing_time_server")
    val closingTimeServer: String? = null
)

data class RestaurantShortDto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("city")
    val city: String = "",

    @SerializedName("rating")
    val rating: Double = 0.0,

    @SerializedName("distance")
    val distance: Int = 0,

    @SerializedName("delivery_cost")
    val deliveryCost: Int? = 0,

    @SerializedName("delivery_time")
    val deliveryTime: String? = "",

    @SerializedName("is_open")
    val isOpen: Boolean = false,

    @SerializedName("closing_time_server")
    val closingTimeServer: String? = ""
)
