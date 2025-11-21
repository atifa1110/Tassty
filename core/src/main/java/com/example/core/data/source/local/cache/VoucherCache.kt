package com.example.core.data.source.local.cache

import com.example.core.data.model.VoucherDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoucherCache @Inject constructor() : BaseCache<VoucherDto>() {

    private val globalMap = mutableMapOf<String, VoucherDto>()

    suspend fun saveAll(key: String, vouchers: List<VoucherDto>) {
        super.saveAll(key, vouchers) { it.id }

        // update globalMap juga di dalam mutex BaseCache
        mutex.withLock {
            vouchers.forEach { voucher ->
                globalMap[voucher.id] = voucher
            }
        }
    }

}