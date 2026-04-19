package com.example.core.data.source.local.cache

import com.example.core.data.model.CategoryDto
import com.example.core.data.model.VoucherDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoucherCache @Inject constructor() : BaseCache<VoucherDto>() {

    suspend fun saveVouchers(key: String,vouchers: List<VoucherDto>) {
        super.saveAll(key, vouchers) { it.id }
    }
}