package com.example.core.data.source.remote.api

import com.example.core.data.model.UserAddressDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.UserSetUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {
    @GET("user-addresses")
    suspend fun getUserAddresses(): BaseResponse<List<UserAddressDto>>

    @GET("user-addresses/primary")
    suspend fun getUserAddressPrimary(
    ): BaseResponse<UserAddressDto>

    @POST("user-addresses")
    suspend fun addUserAddress(
        @Body request: UserSetUpRequest
    ): BaseResponse<Unit>
}