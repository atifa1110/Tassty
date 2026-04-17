package com.example.tassty.screen.detailrestaurant

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.usecase.AddCartMenuUseCase
import com.example.core.domain.usecase.AddFavoriteRestaurantUseCase
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetCartsByRestaurantIdUseCase
import com.example.core.domain.usecase.GetCollectionsByIdUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.domain.usecase.GetDetailAllMenuUseCase
import com.example.core.domain.usecase.GetDetailBestSellerMenuUseCase
import com.example.core.domain.usecase.GetDetailRecommendedMenuUseCase
import com.example.core.domain.usecase.GetDetailRestaurantUseCase
import com.example.core.domain.usecase.GetRestaurantVouchersUseCase
import com.example.core.domain.usecase.GetReviewsByIdUseCase
import com.example.core.domain.usecase.ObserveCartByMenuIdUseCase
import com.example.core.domain.usecase.RemoveFavoriteRestaurantUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.utils.mapToResource
import com.example.core.utils.toListState
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.mapper.toDomainDetail
import com.example.core.ui.mapper.toNavArg
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.utils.toImmutableListState
import com.example.tassty.navigation.DetailRestaurantDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class DetailRestaurantViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailRestaurantUseCase: GetDetailRestaurantUseCase,
    private val getDetailBestSellerMenuUseCase: GetDetailBestSellerMenuUseCase,
    private val getDetailRecommendedMenuUseCase: GetDetailRecommendedMenuUseCase,
    private val getDetailAllMenuUseCase: GetDetailAllMenuUseCase,
    private val getRestaurantVouchersUseCase: GetRestaurantVouchersUseCase,
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase,
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val getReviewsByIdUseCase: GetReviewsByIdUseCase,
    private val addFavoriteRestaurantUseCase: AddFavoriteRestaurantUseCase,
    private val removeFavoriteRestaurantUseCase: RemoveFavoriteRestaurantUseCase,
    private val getCartsByRestaurantIdUseCase: GetCartsByRestaurantIdUseCase,
    //private val getDetailMenuUseCase: GetDetailMenuUseCase,
    private val observeCartByMenuIdUseCase: ObserveCartByMenuIdUseCase,
    private val addCartMenuUseCase: AddCartMenuUseCase
): ViewModel() {

    val id = DetailRestaurantDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(DetailInternalState())

    private val _uiEffect = Channel<DetailUiEvent>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    private val restaurantFlow = getDetailRestaurantUseCase(id).map {
        it.mapToResource { detail -> detail.toUiModel() }
    }

    private val allMenusFlow = getDetailAllMenuUseCase(id)
        .map { it.toImmutableListState { menu -> menu.toUiModel() } }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private val recommendedFlow = getDetailRecommendedMenuUseCase(id)
        .map { it.toImmutableListState { menu -> menu.toUiModel() } }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private val bestSellerFlow = getDetailBestSellerMenuUseCase(id)
        .map { it.toImmutableListState { menu -> menu.toUiModel() } }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private val reviewsFlow = getReviewsByIdUseCase(id)
        .map { it.toImmutableListState { rev -> rev.toUiModel() } }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private val vouchersFlow = getRestaurantVouchersUseCase(id)
        .map { it.toImmutableListState { v -> v.toUiModel() } }
        .flowOn(Dispatchers.Default)
        .distinctUntilChanged()

    private val contentSectionFlow = combine(
        allMenusFlow,
        recommendedFlow,
        bestSellerFlow,
        reviewsFlow,
        vouchersFlow
    ) { all, recommended, best, reviews, vouchers ->
        DetailListContent(
            allMenus = all,
            recommendedMenus = recommended,
            bestSellerMenus = best,
            reviews = reviews,
            vouchers = vouchers
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailListContent()
    )

    private val collectionsFlow = combine(
        getCollectionsUseCase().map { list -> list.map { it.toUiModel() }.toImmutableList() },
        _internalState.map { it.selectedCollectionIds }.distinctUntilChanged()
    ) { collections, selectedIds ->
        collections.map { it.copy(isSelected = selectedIds.contains(it.id)) }.toImmutableList()
    }.flowOn(Dispatchers.Default)


    val uiState: StateFlow<DetailRestaurantUiState> = combine(
        _internalState,
        restaurantFlow,
        contentSectionFlow,
        collectionsFlow,
        getCartsByRestaurantIdUseCase(id)
    ) { internal, detail, content, collections, cart ->

        DetailRestaurantUiState(
            restaurantResource = detail,
            reviewsResource = content.reviews,
            vouchersResource = content.vouchers,
            allMenusResource = content.allMenus,
            recommendedMenusResource = content.recommendedMenus,
            bestSellerMenusResource = content.bestSellerMenus,
            collectionsResource = collections,
            isScheduleModalVisible = internal.isScheduleModalVisible,
            isFavoriteModalVisible = internal.isFavoriteModalVisible,
            isCollectionSheetVisible = internal.isCollectionSheetVisible,
            isAddCollectionSheetVisible = internal.isAddCollectionSheetVisible,
            isShowCloseModalVisible = internal.isShowCloseModalVisible,
            isSearchModalVisible = internal.isSearchModalVisible,
            menu = internal.selectedMenu,
            searchQuery = internal.searchQuery,
            totalItems = cart.totalQuantity,
            totalPrice = cart.totalPrice,
            newCollectionName = internal.newCollectionName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailRestaurantUiState()
    )

    @OptIn(FlowPreview::class)
    val searchResultsUiState: StateFlow<Resource<ImmutableList<MenuUiModel>>> = combine(
        allMenusFlow,
        _internalState.map { it.searchQuery }.debounce(500).distinctUntilChanged()
    ) { menuResource, query ->
        when {
            menuResource.isLoading -> Resource(isLoading = true)
            menuResource.errorMessage != null -> Resource(errorMessage = menuResource.errorMessage)
            query.isBlank() -> menuResource

            else -> {
                val filtered = menuResource.data?.filter {
                    it.name.contains(query, ignoreCase = true)
                }?.toImmutableList()

                Resource(data = filtered ?: persistentListOf(), isLoading = false)
            }
        }
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource(isLoading = true)
        )

    fun onEvent(event: DetailRestaurantEvent) {
        when (event) {
            is DetailRestaurantEvent.OnDismissScheduleSheet -> _internalState.update { it.copy(isScheduleModalVisible = false) }
            is DetailRestaurantEvent.OnShowScheduleSheet -> _internalState.update { it.copy(isScheduleModalVisible = true) }
            is DetailRestaurantEvent.OnRestaurantFavoriteSheet -> handleRestaurantFavoriteClick()
            is DetailRestaurantEvent.OnRestaurantDismissFavoriteSheet -> _internalState.update { it.copy(isFavoriteModalVisible = false) }
            is DetailRestaurantEvent.OnDismissAddCollectionSheet -> _internalState.update { it.copy(isAddCollectionSheetVisible = false) }
            is DetailRestaurantEvent.OnShowAddCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = false, isAddCollectionSheetVisible = true) }
            is DetailRestaurantEvent.OnCreateCollection -> handleCreateNewCollection()
            is DetailRestaurantEvent.OnNewCollectionNameChange ->  _internalState.update { it.copy(newCollectionName = event.name) }
            is DetailRestaurantEvent.OnMenuFavoriteClick -> handleMenuFavoriteClick(event.menu)
            is DetailRestaurantEvent.OnShowCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = true) }
            is DetailRestaurantEvent.OnDismissCollectionSheet -> _internalState.update { it.copy(isCollectionSheetVisible = false) }
            is DetailRestaurantEvent.OnCollectionCheckChange -> handleCollectionCheckChange(event.collectionId)
            is DetailRestaurantEvent.OnSaveToCollection -> handleSaveToCollection()
            is DetailRestaurantEvent.OnDismissSearchSheet -> _internalState.update { it.copy(isSearchModalVisible = false) }
            is DetailRestaurantEvent.OnShowSearchSheet -> _internalState.update { it.copy(isSearchModalVisible = true) }
            is DetailRestaurantEvent.OnSearchQueryChange -> handleSearchQueryChange(event.newQuery)
            is DetailRestaurantEvent.OnDismissCloseSheet -> _internalState.update { it.copy(isShowCloseModalVisible = false) }
        }
    }

    fun onLocationClick() = viewModelScope.launch {
        val state = uiState.value.restaurantResource.data?: return@launch
        val data = state.toNavArg()
        _uiEffect.send(DetailUiEvent.NavigateToLocation(data))
    }

    private fun handleCreateNewCollection() = viewModelScope.launch {
        try {
            createNewCollectionUseCase(_internalState.value.newCollectionName)
            _internalState.update {
                it.copy(
                    isAddCollectionSheetVisible = false,
                    isCollectionSheetVisible = true,
                    newCollectionName = ""
                )
            }
        }catch (e: Exception){
            _uiEffect.send(DetailUiEvent.ShowSnackbar(e.message?:""))
        }
    }

    private fun handleRestaurantFavoriteClick() {
        viewModelScope.launch {
            val restaurant = uiState.value.restaurantResource.data?: return@launch
            val isFavorite = restaurant.isWishlist

            if (isFavorite) {
                removeFavoriteRestaurantUseCase(id)
            } else {
                val domain = restaurant.toDomainDetail()
                addFavoriteRestaurantUseCase(domain)
                _internalState.update { it.copy(isFavoriteModalVisible = true) }
            }
        }
    }

    private fun handleMenuFavoriteClick(menu: MenuUiModel) {
        viewModelScope.launch {
            val savedIds = getCollectionsByIdUseCase(menu.id)
            _internalState.update { state ->
                state.copy(
                    isCollectionSheetVisible = true,
                    selectedMenu = menu,
                    selectedCollectionIds = savedIds.toSet()
                )
            }
        }
    }

    private fun handleSaveToCollection() {
        val state = _internalState.value
        val menu = state.selectedMenu?: return
        val restaurant = uiState.value.restaurantResource.data?:return
        val selectedCollectionIds = state.selectedCollectionIds.toList()

        viewModelScope.launch {
            try {
                saveMenuCollectionsUseCase(
                    menu = menu.toDomain(restaurant),
                    collectionIdsFromUser = selectedCollectionIds
                )
                _internalState.update {
                    it.copy(isCollectionSheetVisible = false)
                }
            } catch (e: Exception) {
                Log.e("DetailRestaurantViewModel", e.message.toString())
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

    fun handleSearchQueryChange(newQuery: String) {
        _internalState.update {
            it.copy(searchQuery = newQuery)
        }
    }
}