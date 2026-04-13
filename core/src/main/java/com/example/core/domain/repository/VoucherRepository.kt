package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import kotlinx.coroutines.flow.Flow

interface VoucherRepository {
    fun getTodayVouchers(): Flow<TasstyResponse<List<Voucher>>>

    fun getRestaurantVouchers(id: String): Flow<TasstyResponse<List<Voucher>>>

    fun getUserVouchers(): Flow<TasstyResponse<List<Voucher>>>
}