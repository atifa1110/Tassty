package com.example.core.data.repository

import com.example.core.data.cache.VoucherCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.VoucherNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Voucher
import com.example.core.domain.repository.VoucherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoucherNetworkDataSource,
    private val cache: VoucherCache
) : VoucherRepository {

    companion object {
        private const val META_KEY_TODAY = "today_vouchers"
    }

    override suspend fun getTodayVouchers(): ResultWrapper<List<Voucher>> {
        return withContext(Dispatchers.IO) {
            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_TODAY)
            if (cachedData.isNotEmpty()) {
                return@withContext ResultWrapper.Success(
                    cachedData.map { it.toDomain() },
                    cachedMeta ?: Meta(0, "", "", null)
                )
            }

            val result = remoteDataSource.getTodayVouchers()
            return@withContext when (result) {
                is ResultWrapper.Success -> {
                    cache.saveAll(META_KEY_TODAY, result.data)
                    cache.saveMeta(META_KEY_TODAY, result.meta)

                    ResultWrapper.Success(
                        result.data.map { it.toDomain() }, // mapping DTO → Domain
                        result.meta
                    )
                }

                is ResultWrapper.Error -> result
                is ResultWrapper.Loading -> result
            }
        }
    }

}