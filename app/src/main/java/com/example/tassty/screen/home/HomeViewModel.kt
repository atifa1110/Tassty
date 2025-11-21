package com.example.tassty.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.model.Resource
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetCollectionListIdUseCase
import com.example.core.domain.usecase.GetMenuWishlistStatusUseCase
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedMenusUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.domain.usecase.GetSuggestedMenusUseCase
import com.example.core.domain.usecase.GetTodayVouchersUseCase
import com.example.core.domain.usecase.GetUserCollectionUseCase
import com.example.core.domain.usecase.SaveMenuToCollectionUseCase
import com.example.core.ui.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase,
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase,
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase,
    private val getUserCollectionsUseCase: GetUserCollectionUseCase,
    private val getCollectionListIdUseCase: GetCollectionListIdUseCase,
    private val getMenuWishlistStatusUseCase: GetMenuWishlistStatusUseCase,
    private val createNewCollectionUseCase: CreateNewCollectionUseCase,
    private val saveMenuToCollectionUseCase: SaveMenuToCollectionUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    private val _singleEventFlow = MutableSharedFlow<HomeEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val singleEventFlow = _singleEventFlow.asSharedFlow()

    private fun emitEvent(event: HomeEvent) {
        viewModelScope.launch {
            _singleEventFlow.emit(event)
        }
    }

    init {
        fetchHomeData(isRefresh = false)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnDismissCollectionSheet -> handleDismissCollectionSheet()
            is HomeEvent.OnShowCollectionSheet -> _homeState.update { it.copy(isCollectionSheetVisible = true) }
            is HomeEvent.OnDismissAddCollectionSheet -> handleDismissAddCollectionSheet()
            is HomeEvent.OnShowAddCollectionSheet -> _homeState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is HomeEvent.OnFavoriteClick -> handleFavoriteClick(event.menuId)
            is HomeEvent.OnCreateCollection -> handleCreateNewCollection()
            is HomeEvent.OnNewCollectionNameChange -> _homeState.update { it.copy(newCollectionName = event.name) }
            is HomeEvent.OnCollectionCheckChange -> handleCollectionCheckChange(event.collectionId,event.isChecked)
            is HomeEvent.ShowSnackbar -> TODO()
            is HomeEvent.OnSaveToCollection -> handleSaveToCollection()
        }
    }

    private fun handleFavoriteClick(menuId: String) {
        _homeState.update {
            it.copy(
                isCollectionSheetVisible = true,
                menuIdToSave = menuId,
                collectionsResource = Resource(isLoading = true),
            )
        }

        refreshUserCollections()
    }

    private fun handleSaveToCollection() {
        val state = _homeState.value
        val menuId = state.menuIdToSave
        val selectedCollectionIds = state.collectionsResource.data
            ?.filter { it.isSelected }
            ?.map { it.collection.collectionId }
            ?: emptyList()

        if (menuId == null) {
            emitEvent(HomeEvent.ShowSnackbar("Menu ID tidak ditemukan. Ulangi aksi."))
            return
        }

        _homeState.update { it.copy(collectionsResource = it.collectionsResource.copy(isLoading = true)) }

        viewModelScope.launch {
            when (val result = saveMenuToCollectionUseCase(menuId, selectedCollectionIds)) {
                is ResultWrapper.Success -> {
                    emitEvent(HomeEvent.ShowSnackbar("Menu berhasil disimpan ke ${selectedCollectionIds.size} koleksi!"))

                    val currentMenuId = menuId
                    val newStatus = getMenuWishlistStatusUseCase(currentMenuId)

                    _homeState.update { currentState ->
                        val updatedMenus = currentState.recommendedMenus.data?.map { menu ->
                            if (menu.menu.id == currentMenuId) {
                                menu.copy(isWishlist = newStatus)
                            } else {
                                menu
                            }
                        }
                        currentState.copy(
                            isCollectionSheetVisible = false,
                            collectionsResource = currentState.collectionsResource.copy(isLoading = false),
                            recommendedMenus = Resource(data=updatedMenus)
                        )
                    }
                }

                is ResultWrapper.Error -> {
                    val errorMessage = result.meta.message
                    emitEvent(HomeEvent.ShowSnackbar(errorMessage))
                    _homeState.update { it.copy(collectionsResource = it.collectionsResource.copy(isLoading = false)) }
                }
                is ResultWrapper.Loading -> { }
            }
        }
    }

    fun handleCreateNewCollection() {
        if (homeState.value.newCollectionName.isBlank()) {
            _homeState.update {
                it.copy(
                    createCollectionResource = Resource(errorMessage = "Nama koleksi tidak boleh kosong.")
                )
            }
            return
        }

        viewModelScope.launch {
            _homeState.update {
                it.copy(createCollectionResource = Resource(isLoading = true))
            }

            try {
                when (val result = createNewCollectionUseCase(homeState.value.newCollectionName)) {
                    is ResultWrapper.Success -> {
                        _homeState.update {
                            it.copy(
                                createCollectionResource = Resource(
                                    data = true,
                                    isLoading = false
                                ),
                                isAddCollectionSheet = false,
                                isCollectionSheetVisible = true,
                                newCollectionName = ""
                            )
                        }
                        refreshUserCollections()
                    }

                    is ResultWrapper.Error -> {
                        _homeState.update {
                            it.copy(
                                createCollectionResource = Resource(
                                    errorMessage = result.meta.message,
                                    isLoading = false
                                )
                            )
                        }
                    }

                    is ResultWrapper.Loading -> {}
                }
            } catch (e: Exception) {
                // 6. Handle kegagalan Coroutine/Exception umum
                Log.e("HomeViewModel", "Error creating collection: ${e.message}", e)
                _homeState.update {
                    it.copy(
                        createCollectionResource = Resource(
                            errorMessage = "Terjadi kesalahan sistem: ${e.message}",
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    private fun refreshUserCollections() {
        val menuId = _homeState.value.menuIdToSave

        if (menuId == null) {
            _homeState.update {
                it.copy(
                    collectionsResource = Resource(errorMessage = "ID menu belum dipilih. Ulangi aksi."),
                    isCollectionSheetVisible = true
                )
            }
            return
        }

        _homeState.update {
            it.copy(collectionsResource = Resource(isLoading = true))
        }
        viewModelScope.launch {
            val selectedIds = getCollectionListIdUseCase(menuId)
            try {
                getUserCollectionsUseCase().collect { collections ->
                    val updatedCollections = collections.map { uiModel ->
                        // Cek apakah Collection ID ada di dalam daftar ID yang sudah tersimpan
                        val isSelected = selectedIds.contains(uiModel.collectionId)
                        // Set status isSelected pada CollectionUiModel
                        uiModel.toUiModel(isSelected)
                    }
                    _homeState.update {
                        it.copy(
                            collectionsResource = Resource(data = updatedCollections, isLoading = false),
                        )
                    }
                }
            } catch (e: Exception) {
                _homeState.update {
                    it.copy(
                        collectionsResource = Resource(
                            errorMessage = "Gagal memuat koleksi: ${e.message}",
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    private fun handleCollectionCheckChange(collectionId: Int, isChecked: Boolean) {
        _homeState.update { currentState ->
            // 1. Ambil Resource Koleksi saat ini
            val currentCollectionsResource = currentState.collectionsResource

            // Cek apakah data sudah ada dan tidak null
            val currentCollectionsList = currentCollectionsResource.data

            if (currentCollectionsList.isNullOrEmpty()) {
                // Jika data kosong atau null, tidak ada yang bisa diubah, kembalikan state awal
                return@update currentState
            }

            // 2. Buat daftar baru dengan item yang sudah diubah state-nya
            val updatedList = currentCollectionsList.map { item ->
                if (item.collection.collectionId == collectionId) {
                    item.copy(isSelected = isChecked)
                } else {
                    item
                }
            }

            // 3. Buat Resource baru dengan daftar yang sudah diupdate
            val updatedResource = currentCollectionsResource.copy(data = updatedList)
            currentState.copy(collectionsResource = updatedResource)
        }
    }

    private fun handleDismissAddCollectionSheet(){
        _homeState.update {
            it.copy(
                isAddCollectionSheet = false,
                isCollectionSheetVisible = true,
                collectionsResource = Resource(isLoading = true)
            )
        }
    }

    private fun handleDismissCollectionSheet() {
        _homeState.update {
            it.copy(
                isCollectionSheetVisible = false,
                collectionsResource = Resource(isLoading = false)
            )
        }
    }

    fun onPullToRefresh() {
        fetchHomeData(isRefresh = true)
    }

    private fun fetchHomeData(isRefresh: Boolean) {
        viewModelScope.launch {
            val firstTriple = combine(
                getAllCategoriesUseCase(),
                getNearbyRestaurantsUseCase(),
                getRecommendedRestaurantsUseCase()
            ) { categories, nearby, recommended ->
                Triple(categories, nearby, recommended)
            }

            val secondTriple = combine(
                getRecommendedMenusUseCase(),
                getSuggestedMenusUseCase(),
                getTodayVouchersUseCase()
            ) { menus, suggested, vouchers ->
                Triple(menus, suggested, vouchers)
            }

            // Combine dua Triple
            combine(firstTriple, secondTriple) { first, second ->
                // Hitung global loading
                val allLoading = listOf(
                    first.first, first.second, first.third,
                    second.first, second.second, second.third
                ).all { it is TasstyResponse.Loading }

                HomeUiState(
                    allCategories = first.first.toListState { it.toUiModel() },
                    nearbyRestaurants = first.second.toListState { it.toUiModel() },
                    recommendedRestaurants = first.third.toListState { it.toUiModel() },
                    recommendedMenus = second.first.toListState { it.toUiModel() },
                    suggestedMenus = second.second.toListState { it.toUiModel() },
                    todayVouchers = second.third.toListState { it.toUiModel() }
                )
            }.collect { _homeState.value = it }
        }
    }
}



fun <T, R> TasstyResponse<List<T>>.toListState(mapper: (T) -> R): Resource<List<R>> {
    return when (this) {
        is TasstyResponse.Success -> Resource(
            data = this.data?.map { mapper(it) },
            isLoading = false
        )
        is TasstyResponse.Error -> Resource(
            errorMessage = this.meta.message,
            isLoading = false
        )
        is TasstyResponse.Loading -> Resource(
            isLoading = true
        )
    }
}
