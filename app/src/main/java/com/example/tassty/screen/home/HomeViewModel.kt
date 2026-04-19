package com.example.tassty.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
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
import com.example.core.domain.usecase.GetRefreshTokenUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
import kotlin.collections.map

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getAuthStatusUseCase: GetAuthStatusUseCase,
    val getCollectionsUseCase: GetCollectionsUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase,
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase,
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(HomeInternalState())

    private val _refreshTrigger = MutableSharedFlow<Boolean>(replay = 1).apply {
        tryEmit(false)
    }

    private val _uiEffect = Channel<HomeEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val contentFlow = _refreshTrigger.flatMapLatest { isRefreshed ->
    combine(
            getAllCategoriesUseCase(isRefreshed),
            getNearbyRestaurantsUseCase(isRefreshed),
            getRecommendedRestaurantsUseCase(isRefreshed),
            getTodayVouchersUseCase(isRefreshed)
        ) { categories, nearby, recommended, vouchers ->
            HomeContent(
                categories = categories.toImmutableListState { it.toUiModel() },
                nearby = nearby.toImmutableListState { it.toUiModel() },
                recommended = recommended.toImmutableListState { it.toUiModel() },
                vouchers = vouchers.toImmutableListState { it.toUiModel() }
            )
        }
    }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val menuFlow = _refreshTrigger.flatMapLatest { isRefreshed ->
        combine(
            getRecommendedMenusUseCase(isRefreshed),
            getSuggestedMenusUseCase(isRefreshed),
        ) { recommended, suggested ->
            HomeMenuSection(
                recommendedMenus = recommended.toImmutableListState { it.toUiModel() },
                suggestedMenus = suggested.toImmutableListState { it.toUiModel() }
            )
        }
    }

    private val collectionsFlow = combine(
        getCollectionsUseCase().map { list -> list.map { it.toUiModel() }.toImmutableList() },
        _internalState.map { it.selectedCollectionIds }.distinctUntilChanged()
    ) { collections, selectedIds ->
        collections.map { it.copy(isSelected = selectedIds.contains(it.id)) }.toImmutableList()
    }

    private val uiFlagsFlow = _internalState.map {
        HomeUiFlags(
            isRefreshing = it.isRefreshing,
            isCollectionSheetVisible = it.isCollectionSheetVisible,
            isAddCollectionSheet = it.isAddCollectionSheet,
            isDetailMenuModalVisible = it.isDetailMenuModalVisible,
            newCollectionName = it.newCollectionName,
            errorMessage = it.errorMessage,
            quantity = it.quantity
        )
    }.distinctUntilChanged()

    val uiState : StateFlow<HomeUiState> = combine(
        uiFlagsFlow,
        getAuthStatusUseCase(),
        contentFlow,
        menuFlow,
        collectionsFlow,
    ) { internal, auth, content, menu, collections ->
        val isMainDataLoading = content.categories.isLoading || menu.recommendedMenus.isLoading

        if (internal.isRefreshing && !isMainDataLoading) {
            _internalState.update { it.copy(isRefreshing = false) }
        }

        HomeUiState(
            userName = auth.name ?: "Guest",
            profileImage = auth.profileImage ?: "",
            addressName = auth.addressName ?: "Guest Address",
            allCategories = content.categories,
            nearbyRestaurants = content.nearby,
            recommendedRestaurants = content.recommended,
            todayVouchers = content.vouchers,
            recommendedMenus = menu.recommendedMenus,
            suggestedMenus = menu.suggestedMenus,
            collections = collections,
            isRefreshing = internal.isRefreshing && isMainDataLoading,
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            isAddCollectionSheet = internal.isAddCollectionSheet,
            isDetailMenuModalVisible = internal.isDetailMenuModalVisible,
            newCollectionName = internal.newCollectionName,
            quantity = internal.quantity
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnFavoriteClick -> handleFavoriteClick(menu = event.menu)
            is HomeEvent.OnDismissCollectionSheet -> _internalState.update {
                it.copy(isCollectionSheetVisible = false, menuForCollection = null, selectedCollectionIds = emptySet())
            }
            is HomeEvent.OnCollectionCheckChange -> handleCollectionCheckChange(event.collectionId)
            is HomeEvent.OnSaveToCollection -> handleSaveToCollection()
            is HomeEvent.OnShowAddCollectionSheet -> _internalState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is HomeEvent.OnDismissAddCollectionSheet -> { _internalState.update { it.copy(isAddCollectionSheet = false, isCollectionSheetVisible = true) } }
            is HomeEvent.OnNewCollectionNameChange -> _internalState.update { it.copy(newCollectionName = event.name) }
            is HomeEvent.OnCreateCollection -> handleCreateNewCollection()
            is HomeEvent.OnRefreshToken -> refreshToken()
        }
    }

    private fun refreshToken() = viewModelScope.launch {
        getRefreshTokenUseCase().collect { response ->
            when(response){
                is TasstyResponse.Error -> {
                    _internalState.update { it.copy(isRefreshing = false) }
                }
                is TasstyResponse.Loading -> {
                    _internalState.update { it.copy(isRefreshing = true) }
                }
                is TasstyResponse.Success  -> {
                    _internalState.update { it.copy(isRefreshing = true) }
                    _refreshTrigger.emit(true)
                }
            }
        }
    }

    fun onPullToRefresh() {
        viewModelScope.launch {
            _internalState.update { it.copy(isRefreshing = true) }
            _refreshTrigger.emit(true)
        }
    }

    private fun handleFavoriteClick(menu: MenuUiModel) {
        viewModelScope.launch {
            val selectedIds = getCollectionsByIdUseCase(menu.id)
            _internalState.update { state ->
                state.copy(
                    isCollectionSheetVisible = true,
                    menuForCollection = menu,
                    selectedCollectionIds = selectedIds.toSet()
                )
            }
        }
    }

    private fun handleSaveToCollection() {
        val currentState = _internalState.value
        val menu = currentState.menuForCollection ?: return
        val selectedIds = currentState.selectedCollectionIds.toList()

        viewModelScope.launch {
            try {
                saveMenuCollectionsUseCase(
                    menu = menu.toDomain(),
                    collectionIdsFromUser = selectedIds
                )
                _internalState.update { it.copy(isCollectionSheetVisible = false) }
            } catch (e: Exception) {
                _uiEffect.send(HomeEffect.ShowSnackbar(e.message?:""))
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
            _uiEffect.send(HomeEffect.ShowSnackbar(e.message?:""))
        }
    }
}

