package com.example.tassty.screen.detailmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.collections
import com.example.tassty.menuSections
import com.example.tassty.menus
import com.example.tassty.model.Cart
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailMenuViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(
        DetailMenuUiState(
            menu = menus[1],
            menuChoiceSections = menuSections,
            restaurantStatus = RestaurantStatus.OPEN,
            collections = collections
        )
    )
    val uiState: StateFlow<DetailMenuUiState> = _uiState.asStateFlow()

    init {
        calculatePrices()
    }

    fun onEvent(event: DetailMenuEvent) {
        when (event) {
            is DetailMenuEvent.OnQuantityIncrease -> handleIncrementQuantity()
            is DetailMenuEvent.OnQuantityDecrease -> handleDecrementQuantity()
            is DetailMenuEvent.OnNotesChange -> {
                _uiState.update { it.copy(notesValue = event.notes) }
            }
            is DetailMenuEvent.OnOptionToggle -> handleOptionToggle(event.section, event.option)

            is DetailMenuEvent.OnAddToCartClick -> addToCart()
            is DetailMenuEvent.OnDismissCollectionSheet -> {
                _uiState.update { it.copy(isCollectionSheetVisible = false) }
            }
            is DetailMenuEvent.OnShowCollectionSheet -> handleShowCollectionSheet()
            is DetailMenuEvent.OnCollectionSelected -> handleCollectionSelection(event.collectionId)
            is DetailMenuEvent.OnSaveCollectionClick -> handleSaveCollection()
            is DetailMenuEvent.OnDismissSuccessSheet -> _uiState.update { it.copy(isSuccessSheetVisible = false) }

            is DetailMenuEvent.OnShowAddCollectionSheet -> _uiState.update { it.copy(isAddCollectionSheet = true, isCollectionSheetVisible = false) }
            is DetailMenuEvent.OnDismissAddCollectionSheet -> _uiState.update { it.copy(isAddCollectionSheet = false, isCollectionSheetVisible = true) }
        }
    }

    private fun handleOptionToggle(section: MenuChoiceSection, toggledOption: MenuItemOption) {
        val currentSelected = section.selectedOptions
        val newSelected: List<MenuItemOption>

        if (currentSelected.contains(toggledOption)) {
            // KASUS 1: Opsi sudah dipilih -> Hapus Opsi (Toggle Off)
            newSelected = currentSelected - toggledOption
        } else {
            // KASUS 2: Opsi belum dipilih -> Coba Tambahkan Opsi (Toggle On)
            if (section.maxSelection == 1) {
                // SUB-KASUS 2a: RADIO BUTTON MODE (Pick 1)
                // Selalu ganti pilihan lama dengan pilihan baru
                newSelected = listOf(toggledOption)

            } else if (section.maxSelection > 1) {
                // SUB-KASUS 2b: CHECKBOX MODE (Pick > 1)
                newSelected = if (currentSelected.size < section.maxSelection) {
                    // Batas belum tercapai: Tambahkan opsi baru ke opsi yang sudah ada
                    currentSelected + toggledOption
                } else {
                    // Batas sudah tercapai: Jangan lakukan apa-apa
                    currentSelected
                }
            } else {
                // KASUS DEFAULT/ERROR: maxSelection adalah 0 atau negatif. Do nothing.
                newSelected = currentSelected
            }
        }

        val updatedSection = section.copy(selectedOptions = newSelected)
        // 3. Update UiState secara immutable menggunakan map (Kotlin collection functions)
        _uiState.update { currentState ->
            val updatedSections = currentState.menuChoiceSections.map {
                // Jika section yang diiterasi adalah section yang kita ubah, kembalikan updatedSection
                // Jika bukan, kembalikan section aslinya
                if (it.id == section.id) {
                    updatedSection
                } else {
                    it
                }
            }

            // Update UiState dengan list sections yang baru
            currentState.copy(menuChoiceSections = updatedSections)
        }

        calculatePrices()
    }

    private fun calculatePrices() {
        val state = _uiState.value

        // 1. Calculate the total price of add-on options
        val totalOptionPrice = state.menuChoiceSections.sumOf { section ->
            section.selectedOptions.sumOf { option ->
                // Explicitly define the return type as Int
                option.priceAddon.toInt()
            }
        }

        val priceToUse = if (state.menu.menu.formatDiscountPrice > 0) {
            // If sellingPrice exists and is valid (> 0), use the discounted price
            state.menu.menu.discountPrice
        } else {
            // If sellingPrice does not exist (or is 0), use the original price
            state.menu.menu.originalPrice
        }

        // 2. Calculate the final price per item
        val finalPricePerItem = priceToUse?.plus(totalOptionPrice)

        // 3. Calculate the total cart price
        val cartTotalPrice = finalPricePerItem?.times(state.quantity)

        _uiState.update {
            it.copy(
                cartTotalPrice = cartTotalPrice?:0
            )
        }
    }

    private fun addToCart() {
        val state = _uiState.value

        val requiredSectionsNotFilled = state.menuChoiceSections.filter { section ->
            // Cek section yang wajib dan pilihan yang dipilih kosong
            section.isRequired && section.selectedOptions.isEmpty()
        }

        if (requiredSectionsNotFilled.isNotEmpty()) {
            val missingTitles = requiredSectionsNotFilled.joinToString { it.title }
            val errorMessage = "ERROR: Harap isi pilihan wajib berikut: $missingTitles"

            println(errorMessage)
            return
        }

        val allNotesList = mutableListOf<String>()

        // A. Tambahkan Catatan Teks Bebas (NotesValue)
        if (state.notesValue.isNotEmpty()) {
            allNotesList.add("Notes: ${state.notesValue}")
        }

        // B. Tambahkan Pilihan Menu yang Terpilih (Aggregasi)
        val groupedSelections = state.menuChoiceSections
            .filter { it.selectedOptions.isNotEmpty() }
            .groupBy(
                keySelector = { it.title },
                valueTransform = { section -> section.selectedOptions.map { it.name } }
            )
            .mapValues { (_, listOfLists) -> listOfLists.flatten() }

        // Transformasi data yang diagregasi menjadi string per baris
        groupedSelections.forEach { (title, optionNames) ->
            val formattedOptions = optionNames.joinToString(separator = ", ")
            allNotesList.add("$title: $formattedOptions")
        }

        val item = Cart(
            id = "1",
            name = state.menu.menu.name,
            price = state.cartTotalPrice,
            imageUrl = state.menu.menu.imageUrl,
            note = allNotesList.ifEmpty { null },
            quantity = state.quantity,
        )

        println("\n--- CART ITEM CREATED ---\n$item")
    }

    private fun handleDecrementQuantity() {
        _uiState.update { currentState ->
            if (currentState.quantity > 1) {
                currentState.copy(quantity = currentState.quantity - 1)
            } else {
                currentState
            }
        }
        calculatePrices()
    }

    private fun handleIncrementQuantity() {
        _uiState.update { currentState ->
            // Check if quantity is less than available stock
            if (currentState.quantity < currentState.menu.menu.maxOrderQuantity?:0) {
                currentState.copy(quantity = currentState.quantity + 1)
            } else {
                currentState
            }
        }
        calculatePrices()
    }

    private fun handleShowCollectionSheet() {
        _uiState.update { currentState ->
            if (currentState.menu.isWishlist) {
                val updatedMenu = currentState.menu.copy(isWishlist = false)
                val collectionUpdate = currentState.collections.map {
                    it.copy(isSelected = false)
                }

                currentState.copy(menu = updatedMenu,
                    savedCollectionName = "", collections = collectionUpdate)
            } else {
                currentState.copy(isCollectionSheetVisible = true)
            }
        }
    }

    private fun handleCollectionSelection(collectionId: String) {
        _uiState.update { currentState ->
            // find id collection from current state
            val currentSelectedId = currentState.collections.find { it.isSelected }?.collectionId

            val newSelectedId = if (currentSelectedId == collectionId) {
                null
            } else {
                collectionId
            }

            val newCollections = currentState.collections.map { item ->
                item.copy(isSelected = item.collectionId == newSelectedId)
            }

            currentState.copy(collections = newCollections)
        }
    }

    private fun handleSaveCollection() {
        val state = _uiState.value
        val selectedCollectionItem = state.collections.find { it.isSelected }
        val collectionName = selectedCollectionItem?.name ?: "Default Collection"
        val newWishlistStatus = !state.menu.isWishlist

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    menu = it.menu.copy(isWishlist = newWishlistStatus),
                    savedCollectionName = collectionName,
                    isCollectionSheetVisible = false,
                    isSuccessSheetVisible = true
                )
            }
        }
    }
}