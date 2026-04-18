package com.example.tassty.screen.login

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.LoginEmailPasswordUseCase
import com.example.tassty.util.UiText
import com.example.tassty.utils.DataDummy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.example.tassty.R

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val loginUseCase = mockk<LoginEmailPasswordUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange should update email state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            val inputEmail = "luna@tassty.com"
            viewModel.onEmailChange(inputEmail)

            val state = awaitItem()
            assertEquals(inputEmail, state.email)
            assertEquals(null, state.emailError)
        }
    }

    @Test
    fun `onPasswordChange should update password state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            val inputPassword = "password123"
            viewModel.onPasswordChange(inputPassword)

            val state = awaitItem()
            assertEquals(inputPassword, state.password)
            assertEquals(null, state.passwordError)
        }
    }

    @Test
    fun `isButtonEnabled should be true only when email and password are not blank and not loading`() = runTest {
        viewModel.uiState.test {
            var state = awaitItem()
            assertFalse(state.isButtonEnabled)

            viewModel.onEmailChange("luna@example.com")
            state = awaitItem()
            assertFalse(state.isButtonEnabled)

            viewModel.onPasswordChange("password123")
            state = awaitItem()
            assertTrue(state.isButtonEnabled)

            coEvery { loginUseCase(any(), any()) } returns flowOf(TasstyResponse.Loading())

            viewModel.onLogin()
            state = awaitItem()
            assertFalse(state.isButtonEnabled)
        }
    }

    @Test
    fun `onLogin should show email error when email is invalid`() = runTest {
        val invalidEmail = "luna.salah"
        val password = "password123"

        viewModel.onEmailChange(invalidEmail)
        viewModel.onPasswordChange(password)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onLogin()

            val stateWithError = awaitItem()

            assertNotNull(stateWithError.emailError)
            val expectedError = UiText.StringResource(R.string.email_is_not_valid)
            assertEquals(expectedError, stateWithError.emailError)
            assertFalse(stateWithError.isLoading)

            coVerify(exactly = 0){ loginUseCase(any(), any()) }
        }
    }

    @Test
    fun `onLogin should show password error when password is invalid`() = runTest {
        val inputEmail = "luna@tassty.com"
        val invalidPassword = "1234"

        viewModel.onEmailChange(inputEmail)
        viewModel.onPasswordChange(invalidPassword)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onLogin()

            val stateWithError = awaitItem()

            val expectedError = UiText.StringResource(R.string.password_too_short,8)
            assertEquals(expectedError, stateWithError.passwordError)
            assertFalse(stateWithError.isLoading)

            coVerify(exactly = 0){ loginUseCase(any(), any()) }
        }
    }

    @Test
    fun `onLogin should emit NavigateToHome event and update state correctly`() = runTest {
        val email = "luna@tassty.com"
        val password = "password123"

        val loginFlow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { loginUseCase(any(), any()) } returns loginFlow

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        turbineScope {
            val eventTurbine = viewModel.events.testIn(backgroundScope)
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.expectMostRecentItem()

            viewModel.onLogin()

            loginFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isTextEditable)

            loginFlow.emit(DataDummy.loginResponseSuccess)

            val event = eventTurbine.awaitItem()
            assertTrue(event is LoginEvent.NavigateToHome)

            val finalState = stateTurbine.awaitItem()
            assertFalse(finalState.isLoading)
            assertTrue(finalState.isTextEditable)

            eventTurbine.cancelAndIgnoreRemainingEvents()
            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLogin should show bottom sheet when login fails`() = runTest {
        val email = "luna@tassty.com"
        val password = "password123"

        val loginFlow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { loginUseCase(any(), any()) } returns loginFlow

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.expectMostRecentItem()

            viewModel.onLogin()

            loginFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isTextEditable)

            loginFlow.emit(DataDummy.loginResponseFailed)

            val errorState = stateTurbine.awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.isBottomSheetVisible)
            assertTrue(errorState.isTextEditable)
            assertEquals("Login Failed", errorState.bottomSheetMessage)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDismissBottomSheet should hide bottom sheet`() = runTest {
        val loginFlow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { loginUseCase(any(), any()) } returns loginFlow

        viewModel.onEmailChange("luna@tassty.com")
        viewModel.onPasswordChange("password123")

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.expectMostRecentItem()

            viewModel.onLogin()
            loginFlow.emit(DataDummy.loginResponseFailed)

            val errorState = stateTurbine.awaitItem()
            assertTrue(errorState.isBottomSheetVisible)

            viewModel.onDismissBottomSheet()

            val finalState = stateTurbine.awaitItem()
            assertFalse(finalState.isBottomSheetVisible)
            assertEquals(null,finalState.bottomSheetMessage)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }
}