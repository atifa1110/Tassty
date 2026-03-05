package com.example.tassty.screen.detailorder

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.DetailOrderUiModel

data class DetailOrderUiState (
    val detail : Resource<DetailOrderUiModel> = Resource()
)