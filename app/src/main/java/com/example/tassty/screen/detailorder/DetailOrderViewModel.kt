package com.example.tassty.screen.detailorder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetDetailOrderUseCase
import com.example.core.domain.utils.mapToResource
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.navigation.DetailOrderDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailOrderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailOrderUseCase: GetDetailOrderUseCase
) : ViewModel(){

    val orderId = DetailOrderDestination.getId(savedStateHandle)
    val uiState : StateFlow<DetailOrderUiState> = getDetailOrderUseCase(orderId).map { resource ->
        DetailOrderUiState(
            detail = resource.mapToResource { it.toUiModel() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailOrderUiState()
    )

}