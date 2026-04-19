package com.example.tassty.screen.setupcuisine

import app.cash.turbine.test
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import com.example.core.domain.usecase.GetAllCategoriesUseCase
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.utils.DataDummy
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetUpCuisineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: SetupCuisineViewModel
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase = mockk()

    @Before
    fun setup() {
        // Default behavior: return sukses supaya init block tidak crash
        every { getAllCategoriesUseCase() } returns flowOf(DataDummy.categoryResponseSuccess)
        viewModel = SetupCuisineViewModel(getAllCategoriesUseCase)
    }

    @Test
    fun `when loadCategories success, uiState should be updated with data`() = runTest {
        // Kita re-init di dalam test untuk menangkap emisi dari awal init {}
        every { getAllCategoriesUseCase() } returns flowOf(DataDummy.categoryResponseSuccess)
        viewModel = SetupCuisineViewModel(getAllCategoriesUseCase)

        viewModel.uiState.test {
            awaitItem() // Initial State
            val successState = awaitItem()
            assertEquals(2, successState.categories.size)
            assertEquals("Martabak", successState.categories[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when loadCategories error, should emit error event`() = runTest {
        val errorResponse = DataDummy.categoryResponseError
        every { getAllCategoriesUseCase() } returns flowOf(errorResponse)

        viewModel = SetupCuisineViewModel(getAllCategoriesUseCase)

        viewModel.event.test {
            val event = awaitItem()
            assertTrue(event is SetupCuisineEvent.ShowError)
            assertEquals("Network Error", (event as SetupCuisineEvent.ShowError).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when onSearchTextChanged is called, uiState should update query and filter results`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchTextChanged("Mart")

            val state = awaitItem()
            assertEquals("Mart", state.currentSearchQuery)
            assertEquals(1, state.filteredCategories.size)
            assertEquals("Martabak", state.filteredCategories[0].name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleCategorySelection should update selected ids correctly`() = runTest {
        val targetId = "CAT-001"

        viewModel.uiState.test {
            awaitItem()

            // Action: Select
            viewModel.toggleCategorySelection(targetId)
            val stateSelected = awaitItem()
            assertTrue(stateSelected.selectedCategoryIds.contains(targetId))

            // Action: Unselect
            viewModel.toggleCategorySelection(targetId)
            val stateUnselected = awaitItem()
            assertFalse(stateUnselected.selectedCategoryIds.contains(targetId))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNextClick should emit NavigateToSetUpLocation event with selected ids`() = runTest {
        val id1 = "CAT-001"
        val id2 = "CAT-002"

        viewModel.event.test {
            viewModel.toggleCategorySelection(id1)
            viewModel.toggleCategorySelection(id2)

            // Trigger action
            viewModel.onNextClick()

            val event = awaitItem()
            assertTrue(event is SetupCuisineEvent.NavigateToSetUpLocation)
            val actualIds = (event as SetupCuisineEvent.NavigateToSetUpLocation).cuisines
            assertEquals(listOf(id1, id2), actualIds)

            cancelAndIgnoreRemainingEvents()
        }
    }
}