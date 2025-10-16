package com.example.core.data.mapper

import com.example.core.data.model.CategoryDto
import com.example.core.domain.model.Category

fun CategoryDto.toDomain() : Category {
    return Category(
        id= id,
        name = name,
        imageUrl = imageUrl
    )
}