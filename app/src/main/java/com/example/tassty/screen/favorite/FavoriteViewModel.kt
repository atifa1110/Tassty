package com.example.tassty.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetFavoriteRestaurantsUseCase
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteRestaurantsUseCase: GetFavoriteRestaurantsUseCase
) : ViewModel() {

    val uiState: StateFlow<FavoriteUiState> =
        getFavoriteRestaurantsUseCase()
            .map { response ->
                when (response) {
                    is TasstyResponse.Loading -> {
                        FavoriteUiState(
                            resource = Resource(isLoading = true)
                        )
                    }

                    is TasstyResponse.Success -> {
                        FavoriteUiState(
                            resource = Resource(
                                isLoading = false,
                                data = response.data?.map { it.toUiModel() }
                            )
                        )
                    }

                    is TasstyResponse.Error -> {
                        FavoriteUiState(
                            resource = Resource(
                                isLoading = false,
                                errorMessage = response.meta.message
                            )
                        )
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteUiState(
                    resource = Resource(isLoading = true)
                )
            )
}
