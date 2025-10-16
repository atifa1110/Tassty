package com.example.core.data.source.remote.api

import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.network.ApiResponse

interface VoucherApi {
    suspend fun getTodayVoucher(): ApiResponse<List<VoucherDto>>
}
