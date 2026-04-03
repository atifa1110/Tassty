package com.example.tassty.screen.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.usecase.GetUserVouchersUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VoucherViewModel @Inject constructor(
    private val getUserVouchersUseCase: GetUserVouchersUseCase
) : ViewModel() {

    val uiState : StateFlow<VoucherUiState> = getUserVouchersUseCase()
        .map { response ->
            val resourceList = response.toListState { it.toUiModel() }

            VoucherUiState(
                vouchers = resourceList
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VoucherUiState()
        )
}