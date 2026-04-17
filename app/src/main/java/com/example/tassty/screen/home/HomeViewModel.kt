package com.example.tassty.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
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
import com.example.core.domain.usecase.InitializeSystemCollectionsUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.utils.toImmutableListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase,
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase,
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val initializeSystemCollectionsUseCase: InitializeSystemCollectionsUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(HomeInternalState())

    private val _refreshTrigger = MutableSharedFlow<Boolean>(replay = 1).apply { tryEmit(false) }

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
    }

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
    }.flowOn(Dispatchers.Default)

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

        val errorList = listOf(
            content.categories.errorMessage,
            content.nearby.errorMessage,
            content.recommended.errorMessage,
            menu.recommendedMenus.errorMessage,
            menu.suggestedMenus.errorMessage
        )

        val criticalError = errorList.firstOrNull { message ->
            message?.contains("expired", ignoreCase = true) == true ||
                    message?.contains("401") == true ||
                    message?.contains("Token is missing", ignoreCase = true) == true
        }


        HomeUiState(
            userName = auth.name ?: "Guest",
            profileImage = auth.profileImage ?: "",
            addressName = auth.addressName ?: "Guest Address",
            allCategories = content.categories.applyLoadingIf(internal.isRefreshingToken),
            nearbyRestaurants = content.nearby.applyLoadingIf(internal.isRefreshingToken),
            recommendedRestaurants = content.recommended.applyLoadingIf(internal.isRefreshingToken),
            todayVouchers = content.vouchers.applyLoadingIf(internal.isRefreshingToken),
            recommendedMenus = menu.recommendedMenus.applyLoadingIf(internal.isRefreshingToken),
            suggestedMenus = menu.suggestedMenus.applyLoadingIf(internal.isRefreshingToken),
            collections = collections,
            isRefreshing = internal.isRefreshing,
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            isAddCollectionSheet = internal.isAddCollectionSheet,
            isDetailMenuModalVisible = internal.isDetailMenuModalVisible,
            newCollectionName = internal.newCollectionName,
            quantity = internal.quantity,
            isTokenExpired = criticalError != null
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
            is HomeEvent.OnFavoriteClick -> handleFavoriteClick(menu = event.menu)
            is HomeEvent.OnShowCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = true) }
            is HomeEvent.OnDismissCollectionSheet -> _internalState.update {
                it.copy(isCollectionSheetVisible = false, menuForCollection = null, selectedCollectionIds = emptySet())
            }
            is HomeEvent.OnCollectionCheckChange -> handleCollectionCheckChange(event.collectionId)
            is HomeEvent.OnSaveToCollection -> handleSaveToCollection()
            is HomeEvent.OnShowAddCollectionSheet -> _internalState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is HomeEvent.OnDismissAddCollectionSheet -> { _internalState.update { it.copy(isAddCollectionSheet = false, isCollectionSheetVisible = true) } }
            is HomeEvent.OnNewCollectionNameChange -> _internalState.update { it.copy(newCollectionName = event.name) }
            is HomeEvent.OnCreateCollection -> handleCreateNewCollection()
            is HomeEvent.OnShowDetailMenu -> onCartClick(event.id)
            is HomeEvent.OnRefreshToken -> refreshToken()
        }
    }

    private fun initialSystemCollection() = viewModelScope.launch {
        initializeSystemCollectionsUseCase()
    }

    private fun refreshToken() = viewModelScope.launch {
        getRefreshTokenUseCase().collect { response ->
            when(response){
                is TasstyResponse.Error -> {
                    _internalState.update { it.copy(isRefreshing = true) }
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
            // 1. Nyalakan indikator loading di UI
            _internalState.update { it.copy(isRefreshing = true) }

            // 2. Trigger Flow untuk ambil data dari API (fetchFromRemote = true)
            _refreshTrigger.emit(true)

            // 3. (Opsional) Beri jeda sedikit atau tunggu collect selesai
            // Agar animasi swipe tidak langsung balik ke atas terlalu cepat
            kotlinx.coroutines.delay(1000)

            // 4. Matikan loading
            _internalState.update { it.copy(isRefreshing = false) }

            // 5. Reset trigger ke false agar navigasi berikutnya balik ke mode cache
            _refreshTrigger.emit(false)
        }
    }

    private fun onCartClick(id: String) = viewModelScope.launch {
        _uiEffect.send(HomeEffect.NavigateToDetailMenu(id))
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

    fun <T> Resource<T>.applyLoadingIf(condition: Boolean): Resource<T> {
        return if (condition) this.copy(isLoading = true) else this
    }
}

