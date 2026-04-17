package com.example.tassty.screen.voucher

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.VoucherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class VoucherUiState(
    val vouchers : Resource<ImmutableList<VoucherUiModel>> = Resource(),
    val groupedVouchers: ImmutableMap<String, ImmutableList<VoucherUiModel>> = persistentMapOf()
)