package com.example.tassty.screen.bestseller

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetCartsByRestaurantIdUseCase
import com.example.core.domain.usecase.GetCollectionsByIdUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.domain.usecase.GetDetailBestSellerMenuUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.navigation.BestSellerDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BestSellerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailBestSellerMenuUseCase: GetDetailBestSellerMenuUseCase,
    private val getCartsByRestaurantIdUseCase: GetCartsByRestaurantIdUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase
) : ViewModel() {

    private val id = BestSellerDestination.getId(savedStateHandle)
    private val _internalState = MutableStateFlow(BestSellerInternalState())

    private val _uiEffect = Channel<BestSellerUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val collectionsFlow = getCollectionsUseCase().map {
        it.toListState { collection -> collection.toUiModel() }
    }

    val uiState: StateFlow<BestSellerUiState> = combine(
        getDetailBestSellerMenuUseCase(id),
        getCartsByRestaurantIdUseCase(id),
        _internalState,
        collectionsFlow
    ) { menuList, cart, internal, collections ->
        val items = cart.menus.sumOf { it.quantity }
        val price = cart.menus.sumOf { it.quantity * it.price }

        BestSellerUiState(
            menus = menuList.toListState { it.toUiModel() },
            totalItems = items,
            totalPrice = price,
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            collections = collections.copy(
                data = collections.data?.map { model ->
                    model.copy(isSelected = internal.selectedCollectionIds.contains(model.id))
                }
            ),
            isAddCollectionSheet = internal.isAddCollectionSheet,
            newCollectionName = internal.newCollectionName,
            selectedMenu = internal.selectedMenu
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BestSellerUiState()
    )

    fun onShowCollectionSheet(){
        _internalState.update {
            it.copy(isCollectionSheetVisible = false)
        }
    }

    fun onMenuFavorite(menu: MenuUiModel){
        viewModelScope.launch {
            val savedIds = getCollectionsByIdUseCase(menu.id)
            _internalState.update {
                it.copy(
                    //selectedCollectionIds = savedIds.toSet(),
                    isCollectionSheetVisible = true,
                    selectedMenu = menu
                )
            }
        }
    }

    fun onCollectionCheckChange(id: String){
        _internalState.update { state ->
            val current = state.selectedCollectionIds.toMutableSet()
            if (current.contains(id)) current.remove(id) else current.add(id)
            state.copy(selectedCollectionIds = current)
        }
    }

    fun onSaveToCollection(){
        val state = _internalState.value
        val menu = state.selectedMenu
        val selectedCollectionIds = state.selectedCollectionIds.toList()

        viewModelScope.launch {
            try {
                saveMenuCollectionsUseCase(
                    menu = menu.toDomain(),
                    collectionIdsFromUser = selectedCollectionIds
                )
                _internalState.update {
                    it.copy(isCollectionSheetVisible = false)
                }
            } catch (e: Exception) {
                _uiEffect.send(BestSellerUiEffect.ShowMessage(e.message?:""))
            }
        }
    }

    fun onShowAddCollectionSheet(){
        _internalState.update { it.copy(isAddCollectionSheet = true) }
    }

    fun onDismissAddCollectionSheet(){
        _internalState.update { it.copy(isAddCollectionSheet = false) }
    }

    fun onNewCollectionNameChanged(name: String){
        _internalState.update { it.copy(newCollectionName = name) }
    }

    fun onCreateNewCollection() = viewModelScope.launch {
        try {
            createNewCollectionUseCase(_internalState.value.newCollectionName)
            _internalState.update {
                it.copy(
                    isAddCollectionSheet = false,
                    isCollectionSheetVisible = true,
                    newCollectionName = ""
                )
            }
        }catch (e: Exception){
            _uiEffect.send(BestSellerUiEffect.ShowMessage(e.message?:""))
        }
    }
}