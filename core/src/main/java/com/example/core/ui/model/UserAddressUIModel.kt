package com.example.core.ui.model

import com.example.core.domain.model.AddressType
import com.google.android.gms.maps.model.LatLng

data class UserAddressUiModel (
    val id: String,
    val fullAddress: String,
    val addressName: String,
    val landmarkDetail: String,
    val latitude: Double,
    val longitude: Double,
    val location: LatLng,
    val addressType: AddressType = AddressType.NONE,
    val isPrimary: Boolean,
    val isSelected: Boolean,
    val isSwipeActionVisible: Boolean
)