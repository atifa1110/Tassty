package com.example.tassty.screen.detailmenu

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.AddCartMenuUseCase
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetCollectionsByIdUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.domain.usecase.GetDetailMenuUseCase
import com.example.core.domain.usecase.ObserveCartByMenuIdUseCase
import com.example.core.domain.usecase.ObserveIsMenuFavoriteUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.domain.utils.mapToResource
import com.example.core.domain.utils.toListState
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.tassty.navigation.DetailMenuDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMenuViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailMenuUseCase: GetDetailMenuUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val addCartMenuUseCase: AddCartMenuUseCase,
    private val observeIsMenuFavoriteUseCase: ObserveIsMenuFavoriteUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val observeCartByMenuIdUseCase: ObserveCartByMenuIdUseCase
) : ViewModel() {

    private val id = DetailMenuDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(DetailMenuInternalState())

    val uiState: StateFlow<DetailMenuUiState> = combine(
        _internalState,
        getDetailMenuUseCase(id),
        observeIsMenuFavoriteUseCase(id),
        getCollectionsUseCase()
    ) { internal, detailRes, isFav, collRes ->

        val detailUi = detailRes.mapToResource { menu ->
            val detailUiModel = menu.toUiModel(isFav)

            // 2. Update option groups-nya based on internal state
            val updatedGroups = detailUiModel.optionGroups.map { group ->
                group.copy(
                    options = group.options.map { option ->
                        option.copy(isSelected = internal.selectedOptionIds.contains(option.id))
                    }
                )
            }

            detailUiModel.copy(optionGroups = updatedGroups)
        }

        val collectionsUi = collRes.toListState { collection ->
            collection.toUiModel().copy(
                isSelected = internal.selectedCollectionIds.contains(collection.id)
            )
        }

        DetailMenuUiState(
            detail = detailUi,
            collections = collectionsUi,
            quantity = internal.quantity,
            notesValue = internal.notesValue,
            isEditMode = internal.isEditMode,
            addToCartButtonText = if (internal.isEditMode) "Update Cart" else "Add to Cart",
            cartTotalPrice = calculatePrices(detailUi.data, internal.quantity),
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            isSuccessSheetVisible = internal.isSuccessSheetVisible,
            isAddCollectionSheet = internal.isAddCollectionSheet,
            newCollectionName = internal.newCollectionName,
            savedCollectionName = internal.savedCollectionName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailMenuUiState()
    )

    private val _uiEffect = Channel<UiEvent>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val savedIds = getCollectionsByIdUseCase(id)
            _internalState.update { it.copy(selectedCollectionIds = savedIds.toSet()) }

            observeCartByMenuIdUseCase(id).collect { cartItem ->
                cartItem?.let { item ->
                    _internalState.update { it.copy(
                        quantity = item.quantity,
                        notesValue = item.notes,
                        isEditMode = true
                    )}
                    if (_internalState.value.selectedOptionIds.isEmpty()) {
                        syncOptionsFromSummary(item.finalSummary)
                    }
                }
            }
        }
    }

    fun onEvent(event: DetailMenuEvent) {
        when (event) {
            is DetailMenuEvent.OnQuantityIncrease -> handleQuantity(1)
            is DetailMenuEvent.OnQuantityDecrease -> handleQuantity(-1)
            is DetailMenuEvent.OnOptionToggle -> handleOptionToggle(event.groupId, event.optionId)
            is DetailMenuEvent.OnNotesChange -> _internalState.update { it.copy(notesValue = event.notes.take(100)) }
            is DetailMenuEvent.OnAddToCartClick -> handleAddToCart()
            is DetailMenuEvent.OnShowCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = true, isSuccessSheetVisible = false) }
            is DetailMenuEvent.OnDismissCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = false) }
            is DetailMenuEvent.OnCollectionCheckChange -> handleCollectionToggle(event.collectionId)
            is DetailMenuEvent.OnSaveCollectionClick -> handleSaveToCollection()
            is DetailMenuEvent.OnDismissSuccessSheet -> _internalState.update { it.copy(isSuccessSheetVisible = false) }
            is DetailMenuEvent.OnCreateCollection -> handleCreateNewCollection()
            is DetailMenuEvent.OnNewCollectionNameChange -> _internalState.update { it.copy(newCollectionName = event.name) }
            is DetailMenuEvent.OnShowAddCollectionSheet ->  _internalState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is DetailMenuEvent.OnDismissAddCollectionSheet -> _internalState.update { it.copy(isAddCollectionSheet = false, isCollectionSheetVisible = false) }
        }
    }

    private fun handleQuantity(delta: Int) {
        _internalState.update { state ->
            val max = uiState.value.detail.data?.maxQuantity ?: 10
            val newQty = (state.quantity + delta).coerceIn(1, max)
            state.copy(quantity = newQty)
        }
    }

    private fun handleOptionToggle(groupId: String, optionId: String) {
        _internalState.update { state ->
            val currentSelected = state.selectedOptionIds.toMutableSet()
            val menuData = uiState.value.detail.data ?: return@update state
            val group = menuData.optionGroups.find { it.id == groupId } ?: return@update state

            if (currentSelected.contains(optionId)) {
                currentSelected.remove(optionId)
            } else {
                // Single Select (maxPick == 1)
                if (group.maxPick == 1) {
                    val otherOptionsInGroup = group.options.map { it.id }
                    currentSelected.removeAll { it in otherOptionsInGroup }
                }

                // Check quota before adding (multi-select)
                val currentInGroup = group.options.count { currentSelected.contains(it.id) }
                if (currentInGroup < group.maxPick) {
                    currentSelected.add(optionId)
                }
            }
            state.copy(selectedOptionIds = currentSelected)
        }
    }

    private fun handleCollectionToggle(id: String) {
        _internalState.update { state ->
            val current = state.selectedCollectionIds.toMutableSet()
            if (current.contains(id)) current.remove(id) else current.add(id)
            state.copy(selectedCollectionIds = current)
        }
    }

    private fun calculatePrices(detail: DetailMenuUiModel?, qty: Int): Int {
        if (detail == null) return 0
        val basePrice = if (detail.promo) detail.priceDiscount else detail.priceOriginal

        val extraPrice = detail.optionGroups
            .flatMap { it.options }
            .filter { it.isSelected }
            .sumOf { it.extraPrice }

        return (basePrice + extraPrice) * qty
    }

    private fun handleSaveToCollection() {
        val state = uiState.value
        val internal = _internalState.value
        val menu = state.detail.data ?: return

        val selectedCollectionIds = internal.selectedCollectionIds.toList()
        val firstSelectedName = state.collections.data
            ?.find { internal.selectedCollectionIds.contains(it.id) }
            ?.title ?: ""

        viewModelScope.launch {
            try {
                saveMenuCollectionsUseCase(
                    menu = menu.toDomain(),
                    restaurant = menu.restaurant.toDomain(),
                    selectedCollectionIds = selectedCollectionIds
                )

                _internalState.update {
                    it.copy(
                        isCollectionSheetVisible = false,
                        isSuccessSheetVisible = true,
                        savedCollectionName = firstSelectedName
                    )
                }
            } catch (e: Exception) {
                Log.e("DetailMenuViewModel", e.message.toString())
                _uiEffect.send(UiEvent.ShowSnackbar("Gagal menyimpan ke koleksi"))
            }
        }
    }
    private fun handleAddToCart() = viewModelScope.launch {
        val state = uiState.value
        val detail = state.detail.data ?: return@launch

        val missingGroups = detail.optionGroups.filter { it.required && it.options.none { opt -> opt.isSelected } }

        if (missingGroups.isNotEmpty()) {
            _uiEffect.send(UiEvent.ShowSnackbar("Wajib pilih: ${missingGroups.joinToString { it.title }}"))
            return@launch
        }

        val selectedOptionsSummary = detail.optionGroups
            .filter { g -> g.options.any { it.isSelected } }
            .joinToString("\n") { g ->
                "${g.title}: ${g.options.filter { it.isSelected }.joinToString { it.name }}"
            }

        addCartMenuUseCase(
            menu = detail.toDomain(),
            restaurant = detail.restaurant.toDomain(),
            quantity = state.quantity,
            summary = selectedOptionsSummary,
            notes = state.notesValue
        )
        val message = if (state.isEditMode) "Cart updated successfully!" else "Added to cart!"
        _uiEffect.send(UiEvent.NavigateBackWithResult(detail.restaurant.id, message))
    }

    private fun handleCreateNewCollection() = viewModelScope.launch {
        createNewCollectionUseCase(_internalState.value.newCollectionName)
        _internalState.update { it.copy(newCollectionName = "", isAddCollectionSheet = false, isCollectionSheetVisible = true) }
    }

    private fun syncOptionsFromSummary(summary: String) {
        viewModelScope.launch {
            val menuDetail = uiState.map { it.detail.data }
                .filterNotNull()
                .first()

            val restoredIds = mutableSetOf<String>()
            val lines = summary.lines()

            lines.forEach { line ->
                val parts = line.split(": ")
                if (parts.size == 2) {
                    val optionNames = parts[1].split(", ").map { it.trim() }
                    menuDetail.optionGroups.forEach { group ->
                        group.options.forEach { option ->
                            if (optionNames.contains(option.name)) {
                                restoredIds.add(option.id)
                            }
                        }
                    }
                }
            }
            _internalState.update { it.copy(selectedOptionIds = restoredIds) }
        }
    }
}