package com.example.tassty.screen.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserVouchersUseCase
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VoucherViewModel @Inject constructor(
    private val getUserVouchersUseCase: GetUserVouchersUseCase
) : ViewModel() {

    val uiState : StateFlow<VoucherUiState> = getUserVouchersUseCase()
        .map { response ->
            val resourceList = response.toImmutableListState { it.toUiModel() }

            val grouped: ImmutableMap<String, ImmutableList<VoucherUiModel>> =
                resourceList.data?.groupBy { it.header }?.mapValues {
                    it.value.toImmutableList() }
                    ?.toImmutableMap()?: persistentMapOf()

            VoucherUiState(
                vouchers = resourceList,
                groupedVouchers = grouped
            )
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VoucherUiState()
        )
}