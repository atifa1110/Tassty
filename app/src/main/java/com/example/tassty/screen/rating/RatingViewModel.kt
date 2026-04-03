package com.example.tassty.screen.rating

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateReviewMenuUseCase
import com.example.core.domain.usecase.CreateReviewRestaurantUseCase
import com.example.tassty.RatingType
import com.example.tassty.navigation.RatingDestination
import com.example.tassty.navigation.RatingNavArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createReviewRestaurantUseCase: CreateReviewRestaurantUseCase,
    private val createReviewMenuUseCase: CreateReviewMenuUseCase
) : ViewModel(){

    val ratingData: RatingNavArg = RatingDestination.getRatingData(savedStateHandle)

    private val _uiEffect = Channel<RatingEvent>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val _uiState = MutableStateFlow(RatingUiState(
        headerName = if(ratingData.ratingType == RatingType.DRIVER) "Indah Cafe" else ratingData.ratingType.title,
        orderId = ratingData.orderId,
        orderNumber = ratingData.orderNumber,
        orderItemId = ratingData.menuId,
        createdAt = ratingData.createdAt,
        name = ratingData.name,
        imageUrl = ratingData.imageUrl,
        type = ratingData.ratingType
    ))
    val uiState = _uiState.asStateFlow()

    fun onRatingChanged(rating:Int){
        _uiState.update { it.copy(rating = rating, isButtonEnabled = rating>0) }
    }

    fun onSelectedChanged(tag: String){
        _uiState.update { currentState->
            val currentTags = currentState.selectedTags
            val newTags = if(currentTags.contains(tag)){
                currentTags - tag
            }else{
                currentTags + tag
            }

            currentState.copy(selectedTags = newTags)
        }
    }

    fun onFeedbackChanged(text: String){
        _uiState.update {
            it.copy(feedback = text) }
    }

    fun onSubmitRating(){
        val type = _uiState.value.type

        if(type== RatingType.MENU){
            onCreateReviewMenu()
        } else{
            onCreateReviewRestaurant()
        }
    }

    fun onCreateReviewMenu() = viewModelScope.launch {
        val state = _uiState.value
        val tags = state.selectedTags.joinToString(",")

        createReviewMenuUseCase(orderId = state.orderId, orderItemId = state.orderItemId,
            rating = state.rating,tags, comment = state.feedback).collect { result->
            when(result){
                is TasstyResponse.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RatingEvent.ShowMessage(result.meta.message))
                }
                is TasstyResponse.Loading ->{
                    _uiState.update { it.copy(isLoading = true) }
                }
                is TasstyResponse.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RatingEvent.NavigateBack)
                }
            }
        }
    }

    fun onCreateReviewRestaurant() = viewModelScope.launch {
        val state = _uiState.value

        createReviewRestaurantUseCase(
            orderId = state.orderId, restaurantId = state.orderItemId,
            rating = state.rating, comment = state.feedback
        ).collect { result->
            when(result){
                is TasstyResponse.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RatingEvent.ShowMessage(result.meta.message))
                }
                is TasstyResponse.Loading ->{
                    _uiState.update { it.copy(isLoading = true) }
                }
                is TasstyResponse.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEffect.send(RatingEvent.NavigateBack)
                }
            }
        }
    }
}