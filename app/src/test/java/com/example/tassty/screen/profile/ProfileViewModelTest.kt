package com.example.tassty.screen.profile

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.LogoutUseCase
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.utils.DataDummy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val updateAuthStatusUseCase: UpdateAuthStatusUseCase = mockk(relaxed = true)
    private val getAuthStatusUseCase: GetAuthStatusUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()

    private lateinit var viewModel: ProfileViewModel


    @Before
    fun setUp() {
        every { getAuthStatusUseCase() } returns flowOf(DataDummy.dummyAuthStatus)

        viewModel = ProfileViewModel(
            updateAuthStatusUseCase = updateAuthStatusUseCase,
            getAuthStatusUseCase = getAuthStatusUseCase,
            logoutUseCase = logoutUseCase
        )
    }

    @Test
    fun `uiState emits correct profile data on init`() = runTest {
        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertEquals("Atifa Fiorenza", state.name)
            assertEquals("atifa@example.com", state.email)
            assertEquals("https://photo.url", state.imageUrl)
            assertTrue(state.isDarkMode)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `uiState should emit Guest when profile data is null`() = runTest {
        val emptyStatus = AuthStatus(
            name = null,
            email = null,
            profileImage = null,
            isDarkMode = false
        )

        every { getAuthStatusUseCase() } returns flowOf(emptyStatus)

        val vm = ProfileViewModel(updateAuthStatusUseCase, getAuthStatusUseCase, logoutUseCase)

        vm.uiState.test {
            val state = expectMostRecentItem()

            assertEquals("Guest", state.name)
            assertEquals("Guest", state.email)
            assertEquals("", state.imageUrl)
            assertFalse(state.isDarkMode)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleShowLogoutSheet updates visibility correctly`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.handleShowLogoutSheet(true)
            assertTrue(awaitItem().isLogoutSheetVisible)

            viewModel.handleShowLogoutSheet(false)
            assertFalse(awaitItem().isLogoutSheetVisible)
        }
    }

    @Test
    fun `onLogout should emit loading then hide loading and navigate on success`() = runTest {
        val logoutFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { logoutUseCase() } returns logoutFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val effectTurbine = viewModel.uiEffect.testIn(backgroundScope)

            stateTurbine.awaitItem()

            viewModel.onLogout()

            logoutFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertFalse(loadingState.isLogoutSheetVisible)
            assertTrue(loadingState.isLoading)

            logoutFlow.emit(DataDummy.logoutResponseSuccess)

            val finalState = stateTurbine.awaitItem()
            assertFalse(finalState.isLoading)

            val effect = effectTurbine.awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToLogin)
        }
    }

    @Test
    fun `onLogout should emit loading then error but still emits NavigateToLogin`() = runTest {
        val logoutFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { logoutUseCase() } returns logoutFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val effectTurbine = viewModel.uiEffect.testIn(backgroundScope)

            stateTurbine.awaitItem()

            viewModel.onLogout()

            logoutFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertFalse(loadingState.isLogoutSheetVisible)
            assertTrue(loadingState.isLoading)

            logoutFlow.emit(DataDummy.logoutResponseFailed)

            val finalState = stateTurbine.awaitItem()
            assertFalse(finalState.isLoading)

            val effect = effectTurbine.awaitItem()
            assertTrue(effect is ProfileEffect.NavigateToLogin)
        }
    }

    @Test
    fun `onDarkMode should update isDarkMode in UI State to false`() = runTest {
        val initialStatus = DataDummy.dummyAuthStatus.copy(isDarkMode = true)
        val authStatusFlow = MutableStateFlow(initialStatus)

        every { getAuthStatusUseCase() } returns authStatusFlow
        coEvery { updateAuthStatusUseCase(any()) } coAnswers {
            val transform = firstArg<(AuthStatus) -> AuthStatus>()
            authStatusFlow.value = transform(authStatusFlow.value)
        }

        val viewModelToTest = ProfileViewModel(
            updateAuthStatusUseCase = updateAuthStatusUseCase,
            getAuthStatusUseCase = getAuthStatusUseCase,
            logoutUseCase = logoutUseCase
        )

        viewModelToTest.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isDarkMode)

            viewModelToTest.onDarkMode(false)

            advanceUntilIdle()

            val finalState = awaitItem()
            assertFalse(finalState.isDarkMode)
            cancelAndIgnoreRemainingEvents()
        }
    }
}