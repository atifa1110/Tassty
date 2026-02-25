package com.example.tassty.screen.detailcollection

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetCollectionMenuByIdUseCase
import com.example.core.domain.usecase.RemoveCollectionByIdUseCase
import com.example.core.domain.usecase.UpdateCollectionNameUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.navigation.CollectionDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCollectionMenuByIdUseCase: GetCollectionMenuByIdUseCase,
    private val updateCollectionNameUseCase: UpdateCollectionNameUseCase,
    private val removeCollectionByIdUseCase: RemoveCollectionByIdUseCase
) : ViewModel(){

    val id = CollectionDetailDestination.getId(savedStateHandle)
    val name = CollectionDetailDestination.getName(savedStateHandle)
    val image = CollectionDetailDestination.getImage(savedStateHandle)

    private val _uiState = MutableStateFlow(CollectionDetailUiState(
        collectionName = name, nameInput = name, collectionImage = image)
    )
    val uiState: StateFlow<CollectionDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CollectionDetailEvent) {
        when (event) {
            is CollectionDetailEvent.OnNewCollectionNameChange -> {
                _uiState.update { it.copy(nameInput = event.newName) }
            }

            is CollectionDetailEvent.OnEditSheetClick -> {
                _uiState.update { it.copy(isEditCollection = true) }
            }

            is CollectionDetailEvent.OnDismissAddCollectionSheet -> {
                _uiState.update { it.copy(isEditCollection = false) }
            }

            is CollectionDetailEvent.OnUpdateCollection -> {
                updateCollectionToDatabase()
            }

            is CollectionDetailEvent.OnDeleteSheetClick -> {
                _uiState.update { it.copy(isDeleteCollection = true) }
            }

            is CollectionDetailEvent.OnDismissDeleteCollectionSheet -> {
                _uiState.update { it.copy(isDeleteCollection = false) }
            }

            is CollectionDetailEvent.OnDeleteClick -> {
                onDeleteCollection()
            }

        }
    }

    val collectionMenusUi = getCollectionMenuByIdUseCase(id)
        .map { list -> list.map { it.toUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private fun updateCollectionToDatabase() = viewModelScope.launch {
        val newName = uiState.value.nameInput
        updateCollectionNameUseCase(id,newName)
        _uiState.update {
            it.copy(
                collectionName = newName,
                isEditCollection = false
            )
        }
    }

    private fun onDeleteCollection() {
        viewModelScope.launch {
            try {
                removeCollectionByIdUseCase(id)
                // tutup bottom sheet
                _uiState.update { it.copy(isDeleteCollection = false) }
                // Kirim event dengan pesan sukses
                _uiEvent.send(UiEvent.NavigateBackWithResult("Collection $name berhasil dihapus"))
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.ShowError("Gagal menghapus collection"))
            }
        }
    }
}
