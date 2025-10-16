package com.example.tassty.screen.detailrestaurant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.calculateRestaurantStatus
import com.example.tassty.filterVouchersByRestaurant
import com.example.tassty.getTodayOperatingHoursString
import com.example.tassty.markToday
import com.example.tassty.menus
import com.example.tassty.model.Cart
import com.example.tassty.restaurantDetails
import com.example.tassty.reviews
import com.example.tassty.screen.search.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DetailRestaurantViewModel (): ViewModel() {

    // Persistent State
    private val _uiState = MutableStateFlow(
        DetailRestaurantUiState()
    )
    val uiState: StateFlow<DetailRestaurantUiState> = _uiState.asStateFlow()

    init {
        loadAllData()
    }

    fun onEvent(event: DetailRestaurantEvent) {
        when (event) {
            DetailRestaurantEvent.OnDismissScheduleSheet -> _uiState.update {
                it.copy(isScheduleModalVisible = false)
            }

            DetailRestaurantEvent.OnShowScheduleSheet -> _uiState.update {
                it.copy(isScheduleModalVisible = true)
            }

            DetailRestaurantEvent.OnRestaurantFavoriteSheet -> handleRestaurantFavoriteClick()
            DetailRestaurantEvent.OnRestaurantDismissFavoriteSheet -> _uiState.update {
                it.copy(isFavoriteModalVisible = false)
            }

            is DetailRestaurantEvent.OnAllMenuFavorite -> handleAllMenuItemWishlist(event.menuId)
            is DetailRestaurantEvent.OnRecommendedFavorite -> handleRecommendedMenuItemWishlist(event.menuId)
            is DetailRestaurantEvent.OnBestSellerFavorite -> handleBestSellerMenuItemWishlist(event.menuId)
            is DetailRestaurantEvent.OnAddToCart -> handleAddToCart(event.menu)

            DetailRestaurantEvent.OnDismissSearchSheet -> _uiState.update { it.copy(showSearch = false) }
            DetailRestaurantEvent.OnShowSearchSheet -> _uiState.update { it.copy(showSearch = true) }
            is DetailRestaurantEvent.OnSearchQueryChange -> handleSearchQueryChange(event.newQuery)
            DetailRestaurantEvent.OnDismissCloseSheet -> _uiState.update { it.copy(isShowCloseModalVisible = false) }
        }
    }

    private fun loadAllData() {
        loadRestaurantDetails()
        loadAllMenus()
        loadBestSellers()
        loadRecommendedMenus()
        loadReviews()
        loadVouchers()
    }

    private fun loadRestaurantDetails() {
        viewModelScope.launch {
            try {
                // Simulation call api
                val restaurant = restaurantDetails.find { it.restaurant.id == "2" }

                if (restaurant == null) {
                    throw Exception("Restaurant data not found on server.")
                }

                val markedOperationalHours = markToday(restaurant.restaurant.operationalHours)
                val currentStatus = calculateRestaurantStatus(markedOperationalHours)
                val hoursString = getTodayOperatingHoursString(markedOperationalHours)
                val updatedRestaurant = restaurant.restaurant.copy(operationalHours = markedOperationalHours)

                _uiState.update { currentState ->
                    currentState.copy(
                       // restaurantResource = Resource(data = updatedRestaurant, isLoading = false),
                        status = currentStatus,
                        todayHoursString = hoursString,
                        cartItemsResource = emptyList()
                    )
                }
            } catch (e: Exception) {
                // Global error
                Log.e("DetailVM", "FATAL Error loading restaurant: ${e.message}")
                _uiState.update {
                    it.copy(
                        restaurantResource = Resource(
                            errorMessage = "Failed to load restaurant details. Check your connection or retry.",
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    private fun loadAllMenus() = viewModelScope.launch {
        // Check Global Error
        if (_uiState.value.restaurantResource.errorMessage != null) return@launch

        try {
            delay(5000)
            val menuList = menus

            _uiState.update { it.copy(
                allMenusResource = Resource(data = menuList, isLoading = false)
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                allMenusResource = Resource(errorMessage = "Failed to load all menu list.", isLoading = false)
            ) }
        }
    }

    private fun loadBestSellers() = viewModelScope.launch {
        if (_uiState.value.restaurantResource.errorMessage != null) return@launch
        try {
            delay(5000)
            val bestSellerList = menus

            _uiState.update { it.copy(
                bestSellersResource = Resource(data = bestSellerList, isLoading = false)
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                bestSellersResource = Resource(errorMessage = "Failed to load Best Sellers.", isLoading = false)
            ) }
        }
    }

    private fun loadRecommendedMenus() = viewModelScope.launch {
        if (_uiState.value.restaurantResource.errorMessage != null) return@launch
        try {
            delay(5000)
            val restaurant = restaurantDetails.find { it.restaurant.id == "2" }
            val recommendedList = menus.take(4)

            _uiState.update { it.copy(
                recommendedMenusResource = Resource(data = recommendedList, isLoading = false)
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                recommendedMenusResource = Resource(errorMessage = "Failed to load Recommended Menus.", isLoading = false)
            ) }
        }
    }

    private fun loadReviews() = viewModelScope.launch {
        if (_uiState.value.restaurantResource.errorMessage != null) return@launch
        try {
            delay(5000)
            val reviewList = reviews

            _uiState.update { it.copy(
                reviewsResource = Resource(data = reviewList, isLoading = false)
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                reviewsResource = Resource(errorMessage = "Failed to load reviews.", isLoading = false)
            ) }
        }
    }

    private fun loadVouchers() = viewModelScope.launch {
        if (_uiState.value.restaurantResource.errorMessage != null) return@launch
        try {
            delay(5000)
            val voucherList = filterVouchersByRestaurant("2")

            _uiState.update { it.copy(
                voucherResource = Resource(data = voucherList, isLoading = false)
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                reviewsResource = Resource(errorMessage = "Failed to load vouchers.", isLoading = false)
            ) }
        }
    }

    fun handleRestaurantFavoriteClick() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val oldFavoriteStatus = currentState.isFavorite
                val newFavoriteStatus = !oldFavoriteStatus
                val shouldShowModal = !oldFavoriteStatus && newFavoriteStatus

                currentState.copy(
                    isFavorite = newFavoriteStatus,
                    isFavoriteModalVisible = shouldShowModal
                )
            }

        }
    }

    private fun handleAllMenuItemWishlist(menuId: String) {
        _uiState.update { currentState ->
            val updatedMenus = currentState.allMenusResource.data?.map { menu ->
                if (menu.menu.id == menuId) {
                    menu.copy(isWishlist = !menu.isWishlist)
                } else {
                    menu
                }
            }

            currentState.copy(
                allMenusResource = Resource(data = updatedMenus)
            )
        }
    }

    private fun handleBestSellerMenuItemWishlist(menuId: String) {
        _uiState.update { currentState ->
            val updatedMenus = currentState.bestSellersResource.data?.map { menu ->
                if (menu.menu.id == menuId) {
                    menu.copy(isWishlist = !menu.isWishlist)
                } else {
                    menu
                }
            }

            currentState.copy(
                bestSellersResource = Resource(updatedMenus)
            )
        }
    }

    private fun handleRecommendedMenuItemWishlist(menuId: String) {
        _uiState.update { currentState ->
            val updatedMenus = currentState.recommendedMenusResource.data?.map { menu ->
                if (menu.menu.id == menuId) {
                    menu.copy(isWishlist = !menu.isWishlist)
                } else {
                    menu
                }
            }

            currentState.copy(
                recommendedMenusResource = Resource(data = updatedMenus)
            )
        }
    }

    private fun handleAddToCart(menu: MenuUiModel) {
        viewModelScope.launch {
            val ui = _uiState.value

            // Check restaurant close status
            val restaurantStatus = ui.status?.status ?: RestaurantStatus.CLOSED
            if (restaurantStatus == RestaurantStatus.CLOSED) {
                _uiState.update { current ->
                    current.copy(
                        isShowCloseModalVisible = true
                    )
                }
                return@launch
            }

            // Get cart resource
            val currentCart = ui.cartItemsResource

            // Check if item exist
            val existingItem = currentCart.find { it.id == menu.menu.id }

            val newCartList = if (existingItem != null) {
                currentCart.map { item ->
                    if (item.id == menu.menu.id) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
            } else {
                // add cart if empty
                currentCart + Cart(
                    id = menu.menu.id,
                    name = menu.menu.name,
                    imageUrl = menu.menu.imageUrl,
                    price = menu.menu.originalPrice,
                    quantity = 1,
                    note = null,
                    isSwipeActionVisible = false,
                    isChecked = false
                )
            }

            // Update UiState
            _uiState.update { currentState ->
                currentState.copy(
                    cartItemsResource = newCartList
                )
            }
        }
    }

    private var searchJob: Job? = null

    private fun handleSearchQueryChange(newQuery: String) {
        // Cancel job before running
        searchJob?.cancel()

        // Always update state query and set LOADING
        _uiState.update {
            it.copy(
                searchQuery = newQuery,
                searchResultsResource = it.searchResultsResource.copy(isLoading = true)
            )
        }

        // 3. Logika Bersyarat dan Debounce
        if (newQuery.isNotBlank()) {
            // Set new Job (Debounce dan Filter)
            searchJob = viewModelScope.launch {
                delay(500L)

                // Check if Job cancel when delay (if user need to type)
                if (!isActive) return@launch

                // Do filter
                filterMenus(newQuery)
            }
        } else {
            // Empty Query : Reset without delay
            val allMenus = _uiState.value.allMenusResource.data
            _uiState.update {
                it.copy(
                    searchResultsResource = Resource(data = allMenus, isLoading = false)
                )
            }
        }
    }

    private fun filterMenus(query: String) {
        // Ambil data menu utama yang sudah dimuat
        val allMenus = _uiState.value.allMenusResource.data.orEmpty()

        val filteredList = allMenus.filter { menu ->
            menu.menu.name.contains(query, ignoreCase = true)
        }

        // Update hasil search dan matikan loading
        _uiState.update { currentState ->
            currentState.copy(
                searchResultsResource = Resource(data = filteredList, isLoading = false)
            )
        }
    }

}

