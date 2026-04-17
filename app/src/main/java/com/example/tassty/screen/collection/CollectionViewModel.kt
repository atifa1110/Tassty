package com.example.tassty.screen.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(CollectionInternalState())
    val sheetState: StateFlow<CollectionInternalState> = _internalState.asStateFlow()

    val collectionsState = getCollectionsUseCase()
        .map { result ->
            val collection = result.map { it.toUiModel() }.toImmutableList()

            CollectionUiState(
                collections = collection
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollectionUiState(),
        )

    fun onEvent(event: CollectionEvent) {
        when (event) {
            is CollectionEvent.OnCreateCollection -> {
                handleCreateNewCollection()
            }
            is CollectionEvent.OnDismissAddCollectionSheet -> {
                _internalState.update {
                    it.copy(isAddCollectionSheet = false)
                }
            }
            is CollectionEvent.OnNewCollectionNameChange -> {
                _internalState.update { it.copy(newCollectionName = event.name) }
            }
            is CollectionEvent.OnShowAddCollectionSheet -> {
                _internalState.update {
                    it.copy(isAddCollectionSheet = true)
                }
            }
        }
    }

    fun handleCreateNewCollection() {
        viewModelScope.launch {
            createNewCollectionUseCase(_internalState.value.newCollectionName)
            _internalState.update {
                it.copy(
                    isAddCollectionSheet = false,
                    newCollectionName = ""
                )
            }
        }
    }
}
