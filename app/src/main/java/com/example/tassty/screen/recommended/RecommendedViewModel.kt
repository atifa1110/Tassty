package com.example.tassty.screen.recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetRecommendedCategoryRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class RecommendedViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getRecommendedCategoryRestaurantsUseCase: GetRecommendedCategoryRestaurantsUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(RecommendedInternalState())

    private val categoriesResourceFlow = getAllCategoriesUseCase()
        .map { it.toImmutableListState{ category -> category.toUiModel() } }
        .distinctUntilChanged().flowOn(Dispatchers.Default)

    private val selectedCategoriesFlow = combine(
        categoriesResourceFlow,
        _internalState.map { it.selectedCategoryId }.distinctUntilChanged()
    ) { res, selectedId ->
        res.copy(
            data = res.data?.map { it.copy(isSelected = it.id == selectedId) }?.toImmutableList()
        )
    }

    private val recommendedRestaurantsFlow = getRecommendedRestaurantsUseCase()
        .map { it.toImmutableListState { res -> res.toUiModel() } }
        .distinctUntilChanged().flowOn(Dispatchers.Default)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val recommendedRestaurantCatFlow = _internalState
        .map { it.selectedCategoryId }
        .distinctUntilChanged()
        .flatMapLatest { id ->
            getRecommendedCategoryRestaurantsUseCase(id)
        }
        .map { it.toImmutableListState { res -> res.toUiModel() } }

    val uiState: StateFlow<RecommendedUiState> = combine(
        _internalState.map { it.selectedCategoryId }.distinctUntilChanged(),
        selectedCategoriesFlow,
        recommendedRestaurantsFlow,
        recommendedRestaurantCatFlow
    ) { selectedId, categoriesRes, recommendedRestaurantsRes, recommendedRestaurantCatsRes ->

        RecommendedUiState(
            selectedCategoryId = selectedId,
            allCategories = categoriesRes,
            recommendedRestaurant = recommendedRestaurantsRes,
            recommendedRestaurantCategories = recommendedRestaurantCatsRes
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecommendedUiState()
    )

    fun onCategoryClicked(categoryId: String) {
        _internalState.update { it.copy(selectedCategoryId = categoryId) }
    }
}