package com.example.core.utils

import com.example.core.ui.mapper.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class FilterDataState(
    val selected: Map<FilterCategory, String?> = emptyMap(),
    val applied: Map<FilterCategory, String?> = emptyMap()
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
    override val filterData = _state.asStateFlow()

    override fun onFilterSelected(category: FilterCategory, optionKey: String) {
        _state.update { current ->
            val isSame = current.selected[category] == optionKey
            val newVal = if (isSame) null else optionKey

            val newSelected = current.selected + (category to newVal)

            if (category == FilterCategory.SORT) {
                current.copy(
                    selected = newSelected,
                    applied = current.applied + (FilterCategory.SORT to newVal)
                )
            } else {
                current.copy(selected = newSelected)
            }
        }
    }

    override fun applyFilters() {
        _state.update { it.copy(applied = it.selected) }
    }

    override fun applySort() {
        _state.update { it.copy(
            applied = it.applied + (FilterCategory.SORT to it.selected[FilterCategory.SORT])
        )}
    }

    override fun resetFilters() {
        _state.update { it.copy(
            selected = it.selected.filter { s -> s.key == FilterCategory.SORT },
            applied = it.applied.filter { a -> a.key == FilterCategory.SORT }
        )}
    }

    override fun resetSort() {
        _state.update { it.copy(
            selected = it.selected - FilterCategory.SORT,
            applied = it.applied - FilterCategory.SORT
        )}
    }
}