package com.example.tassty.screen.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    val uiState: StateFlow<CollectionUiState> = combine(
        getCollectionsUseCase(),
        _internalState
    ) { result, internal ->
        CollectionUiState(
            collections = result.toListState { it.toUiModel() },
            isAddCollectionSheet = internal.isAddCollectionSheet,
            newCollectionName = internal.newCollectionName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CollectionUiState()
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
            createNewCollectionUseCase(uiState.value.newCollectionName)
            _internalState.update {
                it.copy(
                    isAddCollectionSheet = false,
                    newCollectionName = ""
                )
            }
        }
    }
}
