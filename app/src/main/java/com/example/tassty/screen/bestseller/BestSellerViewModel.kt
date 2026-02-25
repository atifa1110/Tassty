package com.example.tassty.screen.bestseller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetCartsByRestaurantIdUseCase
import com.example.core.domain.usecase.GetDetailBestSellerMenuUseCase
import com.example.core.domain.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.navigation.BestSellerDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BestSellerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailBestSellerMenuUseCase: GetDetailBestSellerMenuUseCase,
    private val getCartsByRestaurantIdUseCase: GetCartsByRestaurantIdUseCase
) : ViewModel() {

    private val id = BestSellerDestination.getId(savedStateHandle)

    val uiState: StateFlow<BestSellerUiState> = combine(
        getDetailBestSellerMenuUseCase(id),
        getCartsByRestaurantIdUseCase(id)
    ) { menuList, cart ->
        val items = cart.menus.sumOf { it.quantity }
        val price = cart.menus.sumOf { it.quantity * it.price }
        BestSellerUiState(
            menus = menuList.toListState { it.toUiModel() },
            totalItems = items,
            totalPrice = price
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BestSellerUiState()
    )
}