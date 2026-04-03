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
    operator fun invoke(id: String): Flow<TasstyResponse<List<Voucher>>>{
        return repository.getRestaurantVouchers(id)
    }
}