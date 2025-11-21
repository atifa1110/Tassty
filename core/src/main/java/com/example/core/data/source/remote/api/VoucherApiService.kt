package com.example.core.data.source.remote.api

import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET

interface VoucherApiService{
    @GET("vouchers")
    suspend fun getTodayVoucher()
    : BaseResponse<List<VoucherDto>>
}
