package com.example.core.domain.repository

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.Voucher

interface VoucherRepository {
    suspend fun getTodayVouchers(): ResultWrapper<List<Voucher>>
}