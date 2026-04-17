package com.example.tassty.screen.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserAddressUseCase
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getUserAddressUseCase: GetUserAddressUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(AddressTab.ALL)

    private val allAddressesFlow = getUserAddressUseCase()
        .map { response ->
            response.toImmutableListState { it.toUiModel() }
        }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    val uiState = combine(
        allAddressesFlow,
        _selectedTab
    ) { resource, tab ->

        val filteredData = if (tab.type == null) {
            resource.data
        } else {
            resource.data?.filter { it.addressType == tab.type }?.toImmutableList()
        }

        AddressUiState(
            selectedTab = tab,
            addressResource = resource.copy(data = filteredData)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddressUiState()
    )

    fun onTabSelected(tab: AddressTab) {
        _selectedTab.value = tab
    }
}

