package com.example.tassty.screen.review

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantReviewUiModel

data class ReviewUiState(
    val resource : Resource<RestaurantReviewUiModel> = Resource()
)