package com.example.tassty.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.screen.search.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        fetchRecommendedRestaurants()
        fetchNearbyRestaurants()
    }

    fun fetchRecommendedRestaurants() {
        viewModelScope.launch {
            _homeState.update { it.copy(recommendedRestaurants = Resource(isLoading = true)) }

            when (val result = getRecommendedRestaurantsUseCase.invoke()) {
                is ResultWrapper.Success -> _homeState.update {
                    it.copy(
                        recommendedRestaurants = Resource(
                            data = result.data.map { it.toUiModel() }
                        )
                    )
                }
                is ResultWrapper.Error -> _homeState.update {
                    it.copy(recommendedRestaurants = Resource(errorMessage = result.meta.message))
                }
                is ResultWrapper.Loading -> {
                    _homeState.update {
                        it.copy(
                            recommendedRestaurants = Resource(isLoading = true)
                        )
                    }
                }
            }
        }
    }

    fun fetchNearbyRestaurants() {
        viewModelScope.launch {
            _homeState.update { it.copy(nearbyRestaurants = Resource(isLoading = true)) }

            when (val result = getNearbyRestaurantsUseCase.invoke()) {
                is ResultWrapper.Success -> _homeState.update {
                    it.copy(
                        nearbyRestaurants = Resource(
                            data = result.data.map { it.toUiModel() }
                        )
                    )
                }
                is ResultWrapper.Error -> _homeState.update {
                    it.copy(nearbyRestaurants = Resource(errorMessage = result.meta.message))
                }
                is ResultWrapper.Loading -> {
                    _homeState.update {
                        it.copy(
                            nearbyRestaurants = Resource(isLoading = true)
                        )
                    }
                }
            }
        }
    }

}