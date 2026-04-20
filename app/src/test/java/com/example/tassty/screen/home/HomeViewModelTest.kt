package com.example.tassty.screen.home

import app.cash.turbine.test
import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import com.example.core.domain.model.Collection
import com.example.core.domain.usecase.CreateNewCollectionUseCase
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.GetCollectionsByIdUseCase
import com.example.core.domain.usecase.GetCollectionsUseCase
import com.example.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.example.core.domain.usecase.GetRecommendedMenusUseCase
import com.example.core.domain.usecase.GetRecommendedRestaurantsUseCase
import com.example.core.domain.usecase.GetRefreshTokenUseCase
import com.example.core.domain.usecase.GetSuggestedMenusUseCase
import com.example.core.domain.usecase.GetTodayVouchersUseCase
import com.example.core.domain.usecase.SaveMenuCollectionsUseCase
import com.example.core.ui.model.CollectionUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.util.CollectionData
import com.example.tassty.utils.DataDummy
import com.example.tassty.utils.awaitState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val getAuthStatusUseCase: GetAuthStatusUseCase = mockk(relaxed = true)
    private val getCollectionsUseCase: GetCollectionsUseCase = mockk(relaxed = true)
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase = mockk(relaxed = true)
    private val getRecommendedRestaurantsUseCase: GetRecommendedRestaurantsUseCase = mockk(relaxed = true)
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase = mockk(relaxed = true)
    private val getRecommendedMenusUseCase: GetRecommendedMenusUseCase = mockk(relaxed = true)
    private val getSuggestedMenusUseCase: GetSuggestedMenusUseCase = mockk(relaxed = true)
    private val getTodayVouchersUseCase: GetTodayVouchersUseCase = mockk(relaxed = true)
    private val getCollectionsByIdUseCase: GetCollectionsByIdUseCase = mockk(relaxed = true)
    private val createNewCollectionUseCase: CreateNewCollectionUseCase = mockk(relaxed = true)
    private val saveMenuCollectionsUseCase: SaveMenuCollectionsUseCase = mockk(relaxed = true)
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase = mockk(relaxed = true)

    private lateinit var viewModel: HomeViewModel


    @Before
    fun setup() {
        every { getAuthStatusUseCase() } returns flowOf(DataDummy.dummyAuthStatus)
        every { getCollectionsUseCase() } returns flowOf(CollectionData.collection)

        every { getAllCategoriesUseCase(any()) } returns flowOf(DataDummy.categoryResponseError)
        every { getNearbyRestaurantsUseCase(any()) } returns flowOf(DataDummy.restResponseSuccess)
        every { getRecommendedRestaurantsUseCase(any()) } returns flowOf(DataDummy.restResponseSuccess)
        every { getTodayVouchersUseCase(any()) } returns flowOf(DataDummy.voucherResponseSuccess)
        every { getRecommendedMenusUseCase(any()) } returns flowOf(DataDummy.menuResponseSuccess)
        every { getSuggestedMenusUseCase(any()) } returns flowOf(DataDummy.menuResponseSuccess)

        viewModel = HomeViewModel(
            getAuthStatusUseCase, getCollectionsUseCase, getAllCategoriesUseCase,
            getRecommendedRestaurantsUseCase, getNearbyRestaurantsUseCase,
            getRecommendedMenusUseCase, getSuggestedMenusUseCase, getTodayVouchersUseCase,
            getCollectionsByIdUseCase, createNewCollectionUseCase,
            saveMenuCollectionsUseCase, getRefreshTokenUseCase
        )
    }

    @Test
    fun `when onPullToRefresh is called, uiState should reflect refreshing status`() = runTest {
        every { getAllCategoriesUseCase(any()) } returns flowOf(
            TasstyResponse.Loading()
        )

        every { getRecommendedMenusUseCase(any()) } returns flowOf(
            TasstyResponse.Loading()
        )

        viewModel.uiState.test {
            awaitItem()

            viewModel.onPullToRefresh()

            val state = awaitItem()
            assertEquals(true, state.isRefreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when OnNewCollectionNameChange event, uiState should update name`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            val newName = "My Favorite Snacks"
            viewModel.onEvent(HomeEvent.OnNewCollectionNameChange(newName))

            val updatedState = awaitItem()
            assertEquals(newName, updatedState.newCollectionName)
        }
    }

    @Test
    fun `when handleFavoriteClick is called, state should update with selected collection IDs`() = runTest {
        val menuId = "MEN-011"
        val targetCollectionId = "Collection-1"

        val dummyMenu = mockk<MenuUiModel>(relaxed = true) { every { id } returns menuId }
        coEvery { getCollectionsByIdUseCase(menuId) } returns listOf(targetCollectionId)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnFavoriteClick(dummyMenu))
            val finalState = awaitItem()

            assertTrue(finalState.isCollectionSheetVisible)
            val selected = finalState.collections?.find { it.id == targetCollectionId }

            assertNotNull(selected)
            assertTrue(selected?.isSelected == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when OnSaveToCollection is called, should trigger use case if menu is present`() = runTest {
        val menuId = "MEN-011"
        val targetCollectionId = "Collection-1"
        val dummyMenu = mockk<MenuUiModel>(relaxed = true) {
            every { id } returns menuId
        }

        coEvery { getCollectionsByIdUseCase(menuId) } returns listOf(targetCollectionId)
        coEvery { saveMenuCollectionsUseCase(any(), any()) } returns Unit

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnFavoriteClick(dummyMenu))
            awaitItem()

            viewModel.onEvent(HomeEvent.OnSaveToCollection)

            advanceUntilIdle()

            coVerify(exactly = 1) {
                saveMenuCollectionsUseCase(any(), any())
            }

            val finalState = awaitItem()
            assertFalse(finalState.isCollectionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when handleFavoriteClick then OnDismissCollectionSheet, state should be the same`() = runTest {
        val menuId = "MEN-011"
        val targetCollectionId = "Collection-1"
        val dummyMenu = mockk<MenuUiModel>(relaxed = true) {
            every { id } returns menuId
        }

        coEvery { getCollectionsByIdUseCase(menuId) } returns listOf(targetCollectionId)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnFavoriteClick(dummyMenu))
            val stateWithData = awaitItem()

            assertTrue(stateWithData.isCollectionSheetVisible)

            viewModel.onEvent(HomeEvent.OnDismissCollectionSheet)
            val finalState = awaitItem()
            assertFalse(finalState.isCollectionSheetVisible)

            val targetCollection = finalState.collections?.find { it.id == targetCollectionId }
            assertEquals(true, targetCollection?.isSelected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `navigation between collection sheets should work correctly`() = runTest {
        val dummyMenu = mockk<MenuUiModel>(relaxed = true)

        coEvery { getCollectionsByIdUseCase(any()) } returns emptyList()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnFavoriteClick(dummyMenu))
            val state1 = awaitItem()
            assertTrue(state1.isCollectionSheetVisible)
            assertFalse(state1.isAddCollectionSheet)

            viewModel.onEvent(HomeEvent.OnShowAddCollectionSheet)
            val addState = awaitItem()
            assertFalse(addState.isCollectionSheetVisible)
            assertTrue(addState.isAddCollectionSheet)

            viewModel.onEvent(HomeEvent.OnDismissAddCollectionSheet)
            val dismissState = awaitItem()
            assertTrue(dismissState.isCollectionSheetVisible)
            assertFalse(dismissState.isAddCollectionSheet)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when handleCreateNewCollection is successful, sheets should toggle and name reset`() = runTest {
        val newName = "Koleksi Sehat"

        coEvery { createNewCollectionUseCase(newName) } returns Unit
        viewModel.onEvent(HomeEvent.OnNewCollectionNameChange(newName))

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnCreateCollection)

            advanceUntilIdle()

            coVerify(exactly = 1) { createNewCollectionUseCase(newName) }

            val finalState = awaitItem()
            assertFalse(finalState.isAddCollectionSheet)
            assertTrue(finalState.isCollectionSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when createNewCollection is successful, collections list should be updated with new data`() = runTest {
        val newName = "Koleksi Sehat"
        val initialList = listOf(Collection(id = "1", title = "Lama", imageUrl = "", menuCount = 1, isSelected = false),)
        val updatedList = listOf(
            Collection(id = "1", title = "Lama", imageUrl = "", menuCount = 1, isSelected = false),
            Collection(id = "2", title = newName, imageUrl = "", menuCount = 0, isSelected = false)
        )

        val collectionFlow = MutableStateFlow(initialList)
        every { getCollectionsUseCase() } returns collectionFlow
        coEvery { createNewCollectionUseCase(any()) } returns Unit

        val local = HomeViewModel(
            getAuthStatusUseCase, getCollectionsUseCase, getAllCategoriesUseCase,
            getRecommendedRestaurantsUseCase, getNearbyRestaurantsUseCase,
            getRecommendedMenusUseCase, getSuggestedMenusUseCase, getTodayVouchersUseCase,
            getCollectionsByIdUseCase, createNewCollectionUseCase,
            saveMenuCollectionsUseCase, getRefreshTokenUseCase
        )

        local.uiState.test {
            val state1 = awaitState { it.collections != null}
            assertEquals(1, state1.collections?.size)

            local.onEvent(HomeEvent.OnNewCollectionNameChange(newName))
            local.onEvent(HomeEvent.OnCreateCollection)

            collectionFlow.value = updatedList

            val finalState = awaitState { it.collections?.size == 2 }
            assertEquals(2, finalState.collections?.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when viewModel init, categories and menus should show success data`() = runTest {

        every { getAllCategoriesUseCase(any()) } returns flowOf(DataDummy.categoryResponseSuccess)
        every { getNearbyRestaurantsUseCase(any()) } returns flowOf(TasstyResponse.Loading())
        every { getRecommendedRestaurantsUseCase(any()) } returns flowOf(DataDummy.restResponseSuccess)
        every { getTodayVouchersUseCase(any()) } returns flowOf(TasstyResponse.Loading())

        every { getRecommendedMenusUseCase(any()) } returns flowOf(DataDummy.menuResponseSuccess)
        every { getSuggestedMenusUseCase(any()) } returns flowOf(TasstyResponse.Loading())

        every { getAuthStatusUseCase() } returns flowOf(AuthStatus(name = "Luna"))
        every { getCollectionsUseCase() } returns flowOf(emptyList())

        viewModel.uiState.test {
            val state = awaitState { it.allCategories.data != null }

            assertFalse(state.allCategories.isLoading)
            assertEquals(2, state.allCategories.data?.size)
            assertEquals("Martabak", state.allCategories.data?.get(0)?.name)

            assertFalse(state.recommendedMenus.isLoading)
            assertEquals(2, state.recommendedMenus.data?.size)
            assertEquals("Shabu Premium", state.recommendedMenus.data?.get(0)?.name)

            assertFalse(state.recommendedRestaurants.isLoading)
            assertEquals(0, state.recommendedRestaurants.data?.size)

            assertTrue(state.nearbyRestaurants.isLoading)
            assertTrue(state.suggestedMenus.isLoading)
            assertTrue(state.todayVouchers.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should show error message when categories fail`() = runTest {
        every { getAllCategoriesUseCase(any()) } returns flowOf(DataDummy.categoryResponseError)

        viewModel.uiState.test {
            val state = awaitState { it.allCategories.errorMessage != null }

            assertEquals("Network Error", state.allCategories.errorMessage)
            assertFalse(state.allCategories.isLoading)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when refreshToken is success, should emit refresh trigger`() = runTest {
        every { getAllCategoriesUseCase(any()) } returns flowOf(TasstyResponse.Loading())

        val refreshFlow = MutableStateFlow<TasstyResponse<String>>(TasstyResponse.Loading())
        every { getRefreshTokenUseCase() } returns refreshFlow

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(HomeEvent.OnRefreshToken)

            refreshFlow.value = DataDummy.refreshResponseSuccess

            val finalState = awaitState { it.isRefreshing }
            assertTrue(finalState.isRefreshing)

            verify { getAllCategoriesUseCase(true) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when refreshToken success, categories should transition to loading`() = runTest {
        // 1. Setup Awal: Categories sudah Success
        val categoryFlow = MutableStateFlow<TasstyResponse<List<Category>>>(DataDummy.categoryResponseSuccess)
        every { getAllCategoriesUseCase(any()) } returns categoryFlow

        val refreshFlow = MutableStateFlow<TasstyResponse<String>>(TasstyResponse.Loading())
        every { getRefreshTokenUseCase() } returns refreshFlow

        viewModel.uiState.test {
            val initialState = awaitState { it.allCategories.data != null }
            assertFalse(initialState.allCategories.isLoading)

            viewModel.onEvent(HomeEvent.OnRefreshToken)
            categoryFlow.value = TasstyResponse.Loading()

            // 4. Emit Success di Refresh Token
            refreshFlow.value = DataDummy.refreshResponseSuccess

            // Kasih waktu biar coroutine internal ViewModel jalan
            runCurrent()

            // 5. Gunakan awaitState agar Turbine menunggu sampai kondisi terpenuhi
            val finalState = awaitState { it.allCategories.isLoading && it.isRefreshing }

            assertTrue(finalState.allCategories.isLoading)
            assertTrue(finalState.isRefreshing)

            cancelAndIgnoreRemainingEvents()
        }
    }
}