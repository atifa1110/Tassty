package com.example.tassty.screen.nearby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.core.utils.toImmutableListState
import com.example.core.utils.toListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val nearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase
) : ViewModel(){

    val uiState: StateFlow<NearbyUiState> = nearbyRestaurantsUseCase().map { response ->
         val data = response.toImmutableListState { it.toUiModel() }
        NearbyUiState(
            resource = data
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NearbyUiState()
    )
}