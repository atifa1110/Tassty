package com.example.core.ui

import com.example.core.ui.mapper.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class FilterDataState(
    val selectedSort: String? = null,
    val selectedPrice: String? = null,
    val selectedRating: String? = null,
    val selectedMode: String? = null,
    val selectedCuisine: String? = null,

    val appliedSort: String? = null,
    val appliedPrice: String? = null,
    val appliedRating: String? = null,
    val appliedMode: String? = null,
    val appliedCuisine: String? = null
)

interface FilterManager {
    val filterData: StateFlow<FilterDataState>
    fun onFilterSelected(category: FilterCategory, optionKey: String)
    fun applyFilters()
    fun applySort()
    fun resetFilters()
    fun resetSort()
}

class FilterManagerImpl @Inject constructor() : FilterManager {
    private val _state = MutableStateFlow(FilterDataState())
    override val filterData: StateFlow<FilterDataState> = _state.asStateFlow()

    override fun onFilterSelected(category: FilterCategory, optionKey: String) {
        _state.update { current ->
            when (category) {
                FilterCategory.SORT -> current.copy(selectedSort = if (current.selectedSort == optionKey) null else optionKey)
                FilterCategory.PRICE -> current.copy(selectedPrice = if (current.selectedPrice == optionKey) null else optionKey)
                FilterCategory.RATING -> current.copy(selectedRating = if (current.selectedRating == optionKey) null else optionKey)
                FilterCategory.CUISINE -> current.copy(selectedCuisine = if (current.selectedCuisine == optionKey) null else optionKey)
                FilterCategory.MODE -> current.copy(selectedMode = if (current.selectedMode == optionKey) null else optionKey)
                else -> current
            }
        }
    }

    override fun applyFilters() {
        _state.update { it.copy(
            appliedPrice = it.selectedPrice,
            appliedRating = it.selectedRating,
            appliedMode = it.selectedMode,
            appliedCuisine = it.selectedCuisine
        )}
    }

    override fun applySort() {
        _state.update { it.copy(appliedSort = it.selectedSort) }
    }

    override fun resetFilters() {
        _state.update { it.copy(
            selectedPrice = null, selectedRating = null, selectedMode = null, selectedCuisine = null,
            appliedPrice = null, appliedRating = null, appliedMode = null, appliedCuisine = null
        )}
    }

    override fun resetSort() {
        _state.update { it.copy(selectedSort = null, appliedSort = null) }
    }
}