package com.example.tassty.screen.setupcuisine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tassty.categories
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SetupCuisineViewModel : ViewModel() {

    var uiState by mutableStateOf(SetupCuisineUiState())
        private set

    private val _searchQuery = MutableStateFlow("")
    val searchQueryText: StateFlow<String> = _searchQuery

    init {
        loadDummyCategories()
        observeSearchQuery()
    }

    private fun loadDummyCategories() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(5000)
            uiState = uiState.copy(
                isLoading = false,
                categories = categories,
                filteredCategories = categories
            )
        }
    }

    /** 🧠 Listen perubahan search text + debounce untuk melakukan filtering */
    private fun observeSearchQuery() {
        _searchQuery
            .debounce(500) // wait user stop 500ms
            .distinctUntilChanged()
            .onEach { query ->
                // 1. Lakukan filtering
                val filtered = uiState.categories.filter {
                    it.category.name.contains(query, ignoreCase = true)
                }

                // 2. Update UIState dengan hasil filter dan hilangkan status loading
                uiState = uiState.copy(
                    filteredCategories = filtered,
                    currentSearchQuery = query, // Menyimpan query yang sudah di-filter
                    isLoading = false // ✅ Selesai “loading” pencarian
                )
            }
            .launchIn(viewModelScope)
    }

    fun onSearchTextChanged(newText: String) {
        _searchQuery.value = newText

        uiState = if (newText.isBlank()) {
            uiState.copy(
                filteredCategories = uiState.categories,
                isLoading = false,
                currentSearchQuery = ""
            )
        } else {
            uiState.copy(isLoading = true)
        }
    }

    fun toggleCategorySelection(categoryId: String) {
        val current = uiState.selectedCategoryIds.toMutableList()
        if (current.contains(categoryId)) {
            current.remove(categoryId)
        } else {
            current.add(categoryId)
        }
        uiState = uiState.copy(selectedCategoryIds = current)
    }

    fun setError(message: String?) {
        uiState = uiState.copy(errorMessage = message)
    }

    fun clearSelection() {
        uiState = uiState.copy(selectedCategoryIds = emptyList())
    }
}
