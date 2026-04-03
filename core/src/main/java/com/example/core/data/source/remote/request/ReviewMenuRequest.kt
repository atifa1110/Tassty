package com.example.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class ReviewMenuRequest (
    @SerializedName("order_item_id")
    val orderItemId: String,
    val rating: Int,
    val tags: String,
    val comment: String
)