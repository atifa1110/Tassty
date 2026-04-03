package com.example.tassty.screen.detailorder

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.DetailOrderUiModel
import com.example.tassty.RatingType
import com.example.tassty.navigation.RatingNavArg

data class DetailOrderUiState (
    val detail : Resource<DetailOrderUiModel> = Resource(),
    val channelId : String = ""
)

data class DetailOrderInternalState (
    val channelId : String = ""
)

sealed interface DetailOrderEvent {
    data class ShowMessage(val message: String) : DetailOrderEvent
    data class NavigateToRating(val data: RatingNavArg) : DetailOrderEvent
}
