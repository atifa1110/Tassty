package com.example.core.data.source.remote.datasource

import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.api.VoucherApi
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class VoucherNetworkDataSource @Inject constructor(
    private val voucherApi: VoucherApi
) {
    suspend fun getTodayVouchers(): ResultWrapper<List<VoucherDto>> {
        return safeApiCall {
            voucherApi.getTodayVoucher()
        }
    }
}