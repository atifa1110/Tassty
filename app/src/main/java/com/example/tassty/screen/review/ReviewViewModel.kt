package com.example.tassty.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetReviewsDetailByIdUseCase
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewsDetailByIdUseCase: GetReviewsDetailByIdUseCase
) : ViewModel() {

    val uiState: StateFlow<ReviewUiState> = getReviewsDetailByIdUseCase("RES-001").map { response ->
            ReviewUiState(
                resource = when (response) {
                    is TasstyResponse.Success -> Resource(data = response.data?.toUiModel())
                    is TasstyResponse.Error -> Resource(errorMessage = response.meta.message)
                    is TasstyResponse.Loading -> Resource(isLoading = true)
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReviewUiState()
        )
}