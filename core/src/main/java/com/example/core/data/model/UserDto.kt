package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class UserDto (
    val id: String,
    val email: String,
    val name: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("category_ids")
    val categoryIds: List<CategoryDto>
)

data class ProfileDto (
    val name: String,
    val profileImage: String,
)