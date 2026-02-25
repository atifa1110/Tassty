package com.example.core.data.source.remote.datasource

import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.api.VoucherApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class VoucherNetworkDataSource @Inject constructor(
    private val voucherApi: VoucherApiService
) {
    suspend fun getTodayVouchers(): TasstyResponse<List<VoucherDto>> {
        return safeApiCall {
            voucherApi.getTodayVoucher()
        }
    }

    suspend fun getRestaurantVouchers(id: String): TasstyResponse<List<VoucherDto>> {
        return safeApiCall {
            voucherApi.getRestaurantVoucher(id)
        }
    }

    suspend fun getUserVouchers(): TasstyResponse<List<VoucherDto>> {
        return safeApiCall {
            voucherApi.getUserVoucher()
        }
    }
}