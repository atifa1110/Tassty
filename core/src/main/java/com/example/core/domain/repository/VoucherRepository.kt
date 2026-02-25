package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import kotlinx.coroutines.flow.Flow

interface VoucherRepository {
    suspend fun getTodayVouchers(): Flow<TasstyResponse<List<Voucher>>>
    suspend fun getRestaurantVouchers(id: String): Flow<TasstyResponse<List<Voucher>>>
    suspend fun getUserVouchers(): Flow<TasstyResponse<List<Voucher>>>
}