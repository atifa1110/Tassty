package com.example.core.data.source.remote.api

import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface VoucherApiService{
    @GET("vouchers/todays")
    suspend fun getTodayVoucher()
    : BaseResponse<List<VoucherDto>>

    @GET("vouchers/restaurant/{id}")
    suspend fun getRestaurantVoucher(
        @Path("id") id: String
    ): BaseResponse<List<VoucherDto>>

    @GET("vouchers/user")
    suspend fun getUserVoucher(
    ): BaseResponse<List<VoucherDto>>
}
