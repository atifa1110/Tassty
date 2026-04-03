package com.example.tassty.screen.voucher

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.OrderUiModel
import com.example.core.ui.model.VoucherUiModel

data class VoucherUiState(
    val vouchers : Resource<List<VoucherUiModel>> = Resource()
){
    val groupedVouchers: Map<String, List<VoucherUiModel>>
        get() = vouchers.data?.groupBy { it.header } ?: emptyMap()
}
