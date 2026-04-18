package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.repository.VoucherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserAvailableVoucherUseCase @Inject constructor(
    private val repository: VoucherRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<Voucher>>> = flow {
        repository.getUserVouchers().collect { result ->
            val mappedResult = when (result) {
                is TasstyResponse.Success -> {
                    val filteredVouchers = result.data?.filter {
                        it.status == VoucherStatus.AVAILABLE
                    } ?: emptyList()
                    TasstyResponse.Success(filteredVouchers, result.meta)
                }
                is TasstyResponse.Error -> TasstyResponse.Error(result.meta)
                is TasstyResponse.Loading -> TasstyResponse.Loading()
            }

            emit(mappedResult)
        }
    }
}