package com.example.core.data.repository

import com.example.core.data.source.local.cache.VoucherCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.VoucherNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import com.example.core.domain.repository.VoucherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VoucherRepositoryImpl @Inject constructor(
    private val remoteDataSource: VoucherNetworkDataSource,
    private val cache: VoucherCache
) : VoucherRepository {

    companion object {
        private const val META_KEY_TODAY = "today_vouchers"
    }

    override fun getTodayVouchers(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Voucher>>> = flow {
        emit(TasstyResponse.Loading())

        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_TODAY)
        if (cachedData.isNotEmpty()) {
            emit(
                TasstyResponse.Success(
                    data = cachedData.map { it.toDomain() },
                    meta = cachedMeta ?: Meta(0, "", "", null)
                )
            )
            return@flow
        }

        // Take from remote
        when (val result = remoteDataSource.getTodayVouchers()) {
            is TasstyResponse.Success -> {
                // save to cache
                cache.saveAll(META_KEY_TODAY, result.data?:emptyList())
                cache.saveMeta(META_KEY_TODAY, result.meta)

                emit(
                    TasstyResponse.Success(
                        data = result.data?.map { it.toDomain() },
                        meta = result.meta
                    )
                )
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getRestaurantVouchers(id: String): Flow<TasstyResponse<List<Voucher>>>  = flow {
        emit(TasstyResponse.Loading())

        when (val result = remoteDataSource.getRestaurantVouchers(id)) {
            is TasstyResponse.Success -> {
                emit(
                    TasstyResponse.Success(
                        data = result.data?.map { it.toDomain() },
                        meta = result.meta
                    )
                )
            }

            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserVouchers(): Flow<TasstyResponse<List<Voucher>>>  = flow {
        emit(TasstyResponse.Loading())

        when (val result = remoteDataSource.getUserVouchers()) {
            is TasstyResponse.Success -> {
                emit(
                    TasstyResponse.Success(
                        data = result.data?.map { it.toDomain() },
                        meta = result.meta
                    )
                )
            }

            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

}