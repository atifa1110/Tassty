package com.example.core.data.cache

import com.example.core.data.model.MenuDto
import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.VoucherDto
import com.example.core.data.source.remote.network.Meta
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoucherCache @Inject constructor() : BaseCache<VoucherDto>() {

    private val globalMap = mutableMapOf<String, VoucherDto>()

    /**
     * Simpan semua restaurant sekaligus ke cache
     */
    suspend fun saveAll(key: String, vouchers: List<VoucherDto>) {
        // pakai BaseCache.saveAll
        super.saveAll(key, vouchers) { it.id }

        // update globalMap juga di dalam mutex BaseCache
        mutex.withLock {
            vouchers.forEach { voucher ->
                globalMap[voucher.id] = voucher
            }
        }
    }

}