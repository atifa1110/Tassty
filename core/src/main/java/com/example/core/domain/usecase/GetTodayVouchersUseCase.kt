package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.repository.VoucherRepository
import com.example.core.domain.utils.computeStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDate
import javax.inject.Inject

class GetTodayVouchersUseCase @Inject constructor(
    private val repository: VoucherRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<Voucher>>> = flow {
        val result = repository.getTodayVouchers()
        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    val now = LocalDate.now()

                    val vouchers = result.data
                        ?.map { voucher ->
                            val computedStatus = computeStatus(voucher, now)
                            voucher.copy(status = computedStatus)
                        }
                        ?.filter { it.status == VoucherStatus.AVAILABLE }

                    emit(TasstyResponse.Success(vouchers,result.meta))
                }

                is TasstyResponse.Error -> emit(TasstyResponse.Error(result.meta))
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}