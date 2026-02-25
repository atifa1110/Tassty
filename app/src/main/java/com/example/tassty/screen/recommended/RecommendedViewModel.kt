package com.example.tassty.screen.recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetRecommendedCategoryRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.domain.utils.toListState
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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

    private val categoryFlow = getAllCategoriesUseCase().map {
        it.toListState { category-> category.toUiModel() }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryRestaurantFlow = _internalState
        .map { it.selectedCategoryId }.flatMapLatest { id ->
        getRecommendedCategoryRestaurantsUseCase(id)
    }
    val recommendedState: StateFlow<RecommendedUiState> = combine(
        _internalState,
        categoryFlow,
        getRecommendedRestaurantsUseCase(),
        categoryRestaurantFlow
    ) { internal, categories, restaurants, categoryRestaurants ->

        val mappedCategoriesRes = categories.copy(
            data = categories.data?.map { model ->
                model.copy(isSelected = internal.selectedCategoryId.contains(model.id))
            }
        )

        RecommendedUiState(
            selectedCategoryId = internal.selectedCategoryId,
            allCategories = mappedCategoriesRes,
            recommendedRestaurant = restaurants.toListState { it.toUiModel() },
            recommendedRestaurantCategories = categoryRestaurants.toListState { it.toUiModel() }
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