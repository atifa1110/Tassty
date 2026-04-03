package com.example.tassty.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedMenusUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.domain.usecase.GetSuggestedMenusUseCase
import com.example.core.domain.usecase.GetTodayVouchersUseCase
import com.example.core.domain.usecase.GetCollectionsByIdUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.domain.usecase.InitializeSystemCollectionsUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MenuUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase,
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase,
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase,
    private val getCollectionsListUseCase: GetCollectionsUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val initializeSystemCollectionsUseCase: InitializeSystemCollectionsUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(HomeInternalState())
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    private val _uiEffect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val contentFlow = _refreshTrigger.flatMapLatest {
        combine(
            getAllCategoriesUseCase(),
            getNearbyRestaurantsUseCase(),
            getRecommendedRestaurantsUseCase(),
            getTodayVouchersUseCase()
        ) { categories, nearby, recommended, vouchers ->
            HomeContent(
                categories = categories.toListState { it.toUiModel() },
                nearby = nearby.toListState { it.toUiModel() },
                recommended = recommended.toListState { it.toUiModel() },
                vouchers = vouchers.toListState { it.toUiModel() }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuFlow = _refreshTrigger.flatMapLatest {
        combine(
            getRecommendedMenusUseCase(),
            getSuggestedMenusUseCase(),
        ) { recommended, suggested ->
            HomeMenuSection(
                recommendedMenus = recommended.toListState { it.toUiModel() },
                suggestedMenus = suggested.toListState { it.toUiModel() }
            )
        }
    }

    private val collectionFlows = combine(
        getCollectionsListUseCase().map { it.toListState { c -> c.toUiModel() } },
        _internalState.map { it.selectedCollectionIds }.distinctUntilChanged()
    ) { res, selectedIds ->
        res.copy(
            data = res.data?.map { it.copy(isSelected = selectedIds.contains(it.id)) }
        )
    }.distinctUntilChanged()

    private val uiFlagsFlow = _internalState.map {
        HomeUiFlags(
            isRefreshing = it.isRefreshing,
            isCollectionSheetVisible = it.isCollectionSheetVisible,
            isAddCollectionSheet = it.isAddCollectionSheet,
            newCollectionName = it.newCollectionName,
            errorMessage = it.errorMessage
        )
    }.distinctUntilChanged()

    val uiState : StateFlow<HomeUiState> = combine(
        uiFlagsFlow,
        getAuthStatusUseCase(),
        contentFlow,
        menuFlow,
        collectionFlows
    ) { internal, auth, content, menuContent, collectionRes ->
        HomeUiState(
            userName = auth.name ?: "Guest",
            profileImage = auth.profileImage ?: "",
            addressName = auth.addressName ?: "Guest Address",
            allCategories = content.categories,
            nearbyRestaurants = content.nearby,
            recommendedRestaurants = content.recommended,
            todayVouchers = content.vouchers,
            recommendedMenus = menuContent.recommendedMenus,
            suggestedMenus = menuContent.suggestedMenus,
            collectionsResource = collectionRes,
            isRefreshing = internal.isRefreshing,
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            isAddCollectionSheet = internal.isAddCollectionSheet,
            newCollectionName = internal.newCollectionName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    init {
        initialSystemCollection()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDismissCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = false) }
            is HomeEvent.OnShowCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = true) }
            is HomeEvent.OnDismissAddCollectionSheet -> { _internalState.update { it.copy(isAddCollectionSheet = false, isCollectionSheetVisible = true) } }
            is HomeEvent.OnShowAddCollectionSheet -> _internalState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is HomeEvent.OnFavoriteClick -> handleFavoriteClick(menu = event.menu)
            is HomeEvent.OnCreateCollection -> handleCreateNewCollection()
            is HomeEvent.OnNewCollectionNameChange -> _internalState.update { it.copy(newCollectionName = event.name) }
            is HomeEvent.OnCollectionCheckChange -> handleCollectionCheckChange(event.collectionId)
            is HomeEvent.OnSaveToCollection -> handleSaveToCollection()
        }
    }

    private fun initialSystemCollection() = viewModelScope.launch {
        initializeSystemCollectionsUseCase()
    }

    fun onPullToRefresh() {
        viewModelScope.launch {
            _refreshTrigger.emit(Unit)
        }
    }

    private fun handleFavoriteClick(menu: MenuUiModel) {
        viewModelScope.launch {
            val selectedIds = getCollectionsByIdUseCase(menu.id)
            _internalState.update { state ->
                state.copy(
                    isCollectionSheetVisible = true,
                    selectedMenu = menu,
                    selectedCollectionIds = selectedIds.toSet()
                )
            }
        }
    }

    private fun handleSaveToCollection() {
        val currentState = _internalState.value
        val menu = currentState.selectedMenu ?: return
        val selectedIds = currentState.selectedCollectionIds.toList()

        viewModelScope.launch {
            try {
                saveMenuCollectionsUseCase(
                    menu = menu.toDomain(),
                    collectionIdsFromUser = selectedIds
                )
                _internalState.update { it.copy(isCollectionSheetVisible = false) }
            } catch (e: Exception) {
                _uiEffect.send(HomeUiEffect.ShowSnackbar(e.message?:""))
            }
        }
    }

    private fun handleCollectionCheckChange(id: String) {
        _internalState.update { state ->
            val current = state.selectedCollectionIds.toMutableSet()
            if (current.contains(id)) current.remove(id) else current.add(id)
            state.copy(selectedCollectionIds = current)
        }
    }

    private fun handleCreateNewCollection() = viewModelScope.launch {
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
            _uiEffect.send(HomeUiEffect.ShowSnackbar(e.message?:""))
        }
    }
}

