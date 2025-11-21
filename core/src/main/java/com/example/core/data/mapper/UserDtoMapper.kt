package com.example.core.data.mapper

import com.example.core.data.model.AuthDto
import com.example.core.domain.model.AuthUser

fun AuthDto.toDomain() : AuthUser {
    return AuthUser(
        email = this.email,
        token = this.token,
        name = this.name,
        profileName = this.profileName,
        addressName = this.addressName
    )
}
