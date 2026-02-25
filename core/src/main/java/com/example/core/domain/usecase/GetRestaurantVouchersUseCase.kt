package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import com.example.core.domain.repository.VoucherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRestaurantVouchersUseCase @Inject constructor(
    private val repository: VoucherRepository
) {
    operator fun invoke(id: String): Flow<TasstyResponse<List<Voucher>>> = flow {
        val result = repository.getRestaurantVouchers(id)
        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data,result.meta))
                }

                is TasstyResponse.Error -> emit(TasstyResponse.Error(result.meta))
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}