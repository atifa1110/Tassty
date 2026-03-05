package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class OrderItemDto(
    val id: String,
    val quantity: Int,
    val price: Long,
    @SerializedName("menu_name")
    val menuName: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("options")
    val options: String? = "",
    @SerializedName("notes")
    val notes: String? = ""
)