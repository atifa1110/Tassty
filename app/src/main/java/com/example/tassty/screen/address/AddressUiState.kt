package com.example.tassty.screen.address

import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.AddressType
import com.example.core.ui.model.UserAddressUiModel
import kotlinx.collections.immutable.ImmutableList

data class AddressUiState(
    val selectedTab: AddressTab = AddressTab.ALL,
    val addressResource: Resource<ImmutableList<UserAddressUiModel>> = Resource()
)

enum class AddressTab(
    val title: String,
    val type: AddressType?
) {
    ALL("All", null),
    PERSONAL("Personal", AddressType.PERSONAL),
    BUSINESS("Business", AddressType.BUSINESS)
}
