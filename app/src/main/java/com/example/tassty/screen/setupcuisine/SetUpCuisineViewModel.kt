package com.example.tassty.screen.setupcuisine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.categories
import com.example.tassty.screen.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupCuisineViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupCuisineUiState())
    val uiState: StateFlow<SetupCuisineUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQueryText: StateFlow<String> = _searchQuery

    private val _event = MutableSharedFlow<SetupCuisineEvent>()
    val event = _event.asSharedFlow()

    init {
        loadDummyCategories()
        observeSearchQueryWithDebounce()
    }

    private fun loadDummyCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.invoke().collect { result ->
                when(result) {
                    is TasstyResponse.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.emit(SetupCuisineEvent.ShowError(result.meta.message))
                    }
                    is TasstyResponse.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is TasstyResponse.Success -> {
                        val list = result.data?.map { it.toUiModel() }?:emptyList()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                categories = list,
                                filteredCategories = list
                            )
                        }
                    }
                }
            }
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            _event.emit(SetupCuisineEvent.NavigateToSetUpLocation(uiState.value.selectedCategoryIds))
        }
    }

    /** Observe search query dengan debounce */
    @OptIn(FlowPreview::class)
    private fun observeSearchQueryWithDebounce() {
        _searchQuery
            .debounce(500) // tunggu 500ms user berhenti mengetik
            .distinctUntilChanged()
            .onEach { query ->
                val filtered = if (query.isBlank()) {
                    uiState.value.categories
                } else {
                    uiState.value.categories.filter {
                        it.category.name.contains(query, ignoreCase = true)
                    }
                }

                _uiState.update { current ->
                    current.copy(
                        filteredCategories = filtered,
                        currentSearchQuery = query,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchTextChanged(newText: String) {
        _searchQuery.value = newText

        _uiState.update { current ->
            if (newText.isBlank()) {
                current.copy(
                    filteredCategories = current.categories,
                    isLoading = false,
                    currentSearchQuery = ""
                )
            } else {
                current.copy(isLoading = true)
            }
        }
    }


    fun toggleCategorySelection(categoryId: String) {
        val current = uiState.value.selectedCategoryIds.toMutableList()
        if (current.contains(categoryId)) {
            current.remove(categoryId)
        } else {
            current.add(categoryId)
        }
        _uiState.update {  it.copy(selectedCategoryIds = current) }
    }

    fun setError(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedCategoryIds = emptyList()) }
    }
}
