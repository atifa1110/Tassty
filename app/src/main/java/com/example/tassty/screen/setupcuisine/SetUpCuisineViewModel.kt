package com.example.tassty.screen.setupcuisine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupCuisineViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupCuisineUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SetupCuisineEvent>()
    val event = _event.asSharedFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { result ->
                when (result) {
                    is TasstyResponse.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is TasstyResponse.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.emit(SetupCuisineEvent.ShowError(result.meta.message))
                    }
                    is TasstyResponse.Success -> {
                        val list = result.data?.map { it.toUiModel() } ?: emptyList()
                        _uiState.update { it.copy(isLoading = false, categories = list) }
                    }
                }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update { it.copy(currentSearchQuery = newText) }
    }

    fun toggleCategorySelection(categoryId: String) {
        _uiState.update { state ->
            val newSelection = if (state.selectedCategoryIds.contains(categoryId)) {
                state.selectedCategoryIds - categoryId
            } else {
                state.selectedCategoryIds + categoryId
            }
            state.copy(selectedCategoryIds = newSelection)
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            _event.emit(SetupCuisineEvent.NavigateToSetUpLocation(_uiState.value.selectedCategoryIds))
        }
    }
}
