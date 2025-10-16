package com.example.tassty.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedMenusUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.domain.usecase.GetSuggestedMenusUseCase
import com.example.core.domain.usecase.GetTodayVouchersUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.screen.search.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase,
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase,
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    init {
        fetchHomeData(isRefresh = false)
    }

    fun onPullToRefresh() {
        fetchHomeData(isRefresh = true)
    }

    private fun fetchHomeData(isRefresh: Boolean) {
        viewModelScope.launch {
            Log.d("Diana", "fetchHomeData start, isRefresh=$isRefresh")

            _homeState.update {
                it.copy(
                    recommendedRestaurants = Resource(isLoading = true),
                    nearbyRestaurants = Resource(isLoading = true),
                    recommendedMenus = Resource(isLoading = true),
                    suggestedMenus = Resource(isLoading = true),
                    todayVouchers = Resource(isLoading = true),
                    isRefreshing = isRefresh
                )
            }
            try {
                supervisorScope {
                    Log.d("Diana", "SupervisorScope started")

                    val recommendedDeferred = async {
                        Log.d("Diana", "Calling getRecommendedRestaurantsUseCase")
                        getRecommendedRestaurantsUseCase().also {
                            Log.d("Diana", "getRecommendedRestaurantsUseCase finished: $it")
                        }
                    }

                    val nearbyDeferred = async { getNearbyRestaurantsUseCase() }

                    val recommendedMenusDeferred = async {
                        Log.d("Diana", "Calling getRecommendedMenusUseCase")
                        getRecommendedMenusUseCase().also {
                            Log.d("Diana", "getRecommendedMenusUseCase finished: $it")
                        }
                    }

                    val suggestedMenusDeferred = async {
                        Log.d("Diana", "Calling getSuggestedMenusUseCase")
                        getSuggestedMenusUseCase().also {
                            Log.d("Diana", "getSuggestedMenusUseCase finished: $it")
                        }
                    }

                    val vouchersDeferred = async {
                        Log.d("Diana", "Calling getTodayVouchersUseCase")
                        getTodayVouchersUseCase().also {
                            Log.d("Diana", "getTodayVouchersUseCase finished: $it")
                        }
                    }

                    val recommendedResult = recommendedDeferred.await()
                    val nearbyResult = nearbyDeferred.await()
                    val recMenusResult = recommendedMenusDeferred.await()
                    val sugMenusResult = suggestedMenusDeferred.await()
                    val vouchersResult = vouchersDeferred.await()

                    Log.d("Diana", "All await finished, performing mapping...")

                    // Lakukan mapping dan update state di Dispatchers.Default
                    val finalStateData = withContext(Dispatchers.Default) {
                        HomeUiState(
                            recommendedRestaurants = recommendedResult.toResource { it.toUiModel() },
                            nearbyRestaurants = nearbyResult.toResource { it.toUiModel() },
                            recommendedMenus = recMenusResult.toResource { it.toUiModel() },
                            suggestedMenus = sugMenusResult.toResource { it.toUiModel() },
                            todayVouchers = vouchersResult.toResource { it.toUiModel() },
                            isRefreshing = false
                        )
                    }

                    _homeState.update { finalStateData }

                    Log.d("Diana", "UI state updated, fetchHomeData finished")
                }
            } catch (e: CancellationException) {
                // WAJIB: Biarkan coroutine dibatalkan
                throw e
            } catch (e: Exception) {
                // Tangani error umum (jaringan, dll.)
                Log.e("Diana", "General fetch error: ${e.message}", e)
                _homeState.update {
                    it.copy(isRefreshing = false, errorMessage = "Terjadi kegagalan memuat data: ${e.message}")
                }
            }
        }
    }
}

suspend fun <T, R> ResultWrapper<List<T>>.toResource(mapper: (T) -> R): Resource<List<R>> {
    return when (this) {
        is ResultWrapper.Success -> {
            val mappedData = withContext(Dispatchers.Default) { data.map(mapper) }
            Resource(data = mappedData, isLoading = false)
        }
        is ResultWrapper.Error -> Resource(errorMessage = meta.message, isLoading = false)
        is ResultWrapper.Loading -> Resource(isLoading = true)
    }
}