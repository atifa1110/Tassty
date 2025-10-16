package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.repository.VoucherRepository
import com.example.core.domain.utils.computeStatus
import org.threeten.bp.LocalDate
import javax.inject.Inject

class GetTodayVouchersUseCase @Inject constructor(
    private val repository: VoucherRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<Voucher>> {
        val result = repository.getTodayVouchers()
        return when (result) {
            is ResultWrapper.Success -> {
                val now = LocalDate.now()

                val vouchers = result.data
                    .map { voucher ->
                        val computedStatus = computeStatus(voucher, now)
                        voucher.copy(status = computedStatus)
                    }
                    .filter { it.status == VoucherStatus.AVAILABLE }
                ResultWrapper.Success(vouchers, result.meta)
            }
            is ResultWrapper.Error -> ResultWrapper.Error(result.meta)
            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }
}