package com.example.tassty.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetFavoriteRestaurantsUseCase
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteRestaurantsUseCase: GetFavoriteRestaurantsUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoriteUiState> =
        getFavoriteRestaurantsUseCase()
            .map { favorite ->
                val data = favorite.map { it.toUiModel() }.toImmutableList()
                FavoriteUiState(
                    resource = data
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteUiState()
            )
}
