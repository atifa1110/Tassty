package com.example.tassty.screen.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserAddressUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getUserAddressUseCase: GetUserAddressUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(AddressTab.ALL)

    val uiState = combine(
        getUserAddressUseCase(),
        _selectedTab
    ) { response, tab ->
        val resource = response.toListState { it.toUiModel() }

        val filteredResource = if (resource.data == null) {
            resource
        } else {
            val filtered = when (tab.type) {
                null -> resource.data
                else -> resource.data?.filter { it.addressType == tab.type }
            }
            resource.copy(data = filtered)
        }
        AddressUiState(
            selectedTab = tab,
            addressResource = filteredResource
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

