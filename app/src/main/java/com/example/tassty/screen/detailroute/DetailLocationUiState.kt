package com.example.tassty.screen.detailroute

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantLocationArgs
import com.example.core.ui.model.RouteInfoUiModel
import com.google.android.gms.maps.model.LatLng

data class DetailLocationUiState(
    val route : Resource<RouteInfoUiModel> = Resource(),
    val routeCenterPoint: LatLng? = null,
    val userLatLng: LatLng? = null,
    val restaurant : RestaurantLocationArgs
)
