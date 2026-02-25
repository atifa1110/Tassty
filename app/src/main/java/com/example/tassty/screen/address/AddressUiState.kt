package com.example.tassty.screen.address

import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.AddressType
import com.example.core.ui.model.UserAddressUiModel

data class AddressUiState(
    val selectedTab: AddressTab = AddressTab.ALL,
    val addressResource: Resource<List<UserAddressUiModel>> = Resource(isLoading = true)
)

enum class AddressTab(
    val title: String,
    val type: AddressType?
) {
    ALL("All", null),
    PERSONAL("Personal", AddressType.PERSONAL),
    BUSINESS("Business", AddressType.BUSINESS)
}
