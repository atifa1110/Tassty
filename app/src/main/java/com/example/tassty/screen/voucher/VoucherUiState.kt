package com.example.tassty.screen.voucher

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.VoucherUiModel

data class VoucherSections(
    val available: List<VoucherUiModel> = emptyList(),
    val upcoming: List<VoucherUiModel> = emptyList()
)

// Gunakan Resource untuk membungkus VoucherSections
typealias VoucherState = Resource<VoucherSections>