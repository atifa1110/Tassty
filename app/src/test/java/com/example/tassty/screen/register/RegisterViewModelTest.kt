package com.example.tassty.screen.register

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.model.AuthStatus
import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.usecase.RegisterEmailPasswordUseCase
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.R
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.util.UiText
import com.example.tassty.utils.DataDummy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: RegisterViewModel
    private val registerUseCase = mockk<RegisterEmailPasswordUseCase>()
    private val updateStatusUseCase = mockk<UpdateAuthStatusUseCase>()

    private val name = "user"
    private val email = "user@tassty.com"
    private val password = "password123"

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(
            registerEmailPasswordUseCase = registerUseCase,
            updateAuthStatusUseCase = updateStatusUseCase
        )
    }

    @Test
    fun `onEmailChange should update email state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEmailChange(email)

            val state = awaitItem()
            assertEquals(email, state.email)
            assertEquals(null, state.emailError)
        }
    }

    @Test
    fun `onPasswordChange should update password state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            val inputPassword = password
            viewModel.onPasswordChange(password)

            val state = awaitItem()
            assertEquals(inputPassword, state.password)
            assertEquals(null, state.passwordError)
        }
    }

    @Test
    fun `onNameChange should update name state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onFullNameChange(name)

            val state = awaitItem()
            assertEquals(name, state.fullName)
            assertEquals(null, state.fullNameError)
        }
    }

    @Test
    fun `onTermCheckChanged updates term selection state`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onTermCheckChanged(true)
            val state = awaitItem()
            assertTrue(state.isTermSelected)
        }
    }

    @Test
    fun `onConfirmVerification should update registration step to REGISTERED`() = runTest {
        val transformSlot = slot<(AuthStatus) -> AuthStatus>()
        coEvery { updateStatusUseCase(capture(transformSlot)) } returns Unit

        viewModel.onConfirmVerification()

        val initialStatus = AuthStatus(registrationStep = RegistrationStep.REGISTERING)
        val updatedStatus = transformSlot.captured.invoke(initialStatus)

        assertEquals(RegistrationStep.REGISTERED, updatedStatus.registrationStep)
    }

    @Test
    fun `onRegister should show name error when name is invalid`() = runTest {
        val inputName = ""

        viewModel.onFullNameChange(inputName)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onRegister()

            val stateWithError = awaitItem()

            assertNotNull(stateWithError.fullNameError)
            val expected = UiText.StringResource(R.string.error_field_empty, "Name")
            assertEquals(expected, stateWithError.fullNameError)
            assertFalse(stateWithError.isLoading)

            coVerify(exactly = 0){ registerUseCase(any(), any(),any(),any()) }
        }
    }

    @Test
    fun `onRegister should show email error when email is invalid`() = runTest {
        val inputEmail = "atifa"

        viewModel.onEmailChange(inputEmail)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onRegister()

            val stateWithError = awaitItem()

            assertNotNull(stateWithError.emailError)
            val expected = UiText.StringResource(R.string.email_is_not_valid)
            assertEquals(expected, stateWithError.emailError)
            assertFalse(stateWithError.isLoading)

            coVerify(exactly = 0){ registerUseCase(any(), any(),any(),any()) }
        }
    }

    @Test
    fun `onRegister should show password error when password is invalid`() = runTest {
        val inputPassword = "1234"

        viewModel.onPasswordChange(inputPassword)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onRegister()

            val stateWithError = awaitItem()

            assertNotNull(stateWithError.passwordError)
            val expected = UiText.StringResource(R.string.password_too_short,8)
            assertEquals(expected, stateWithError.passwordError)
            assertFalse(stateWithError.isLoading)

            coVerify(exactly = 0){ registerUseCase(any(), any(),any(),any()) }
        }
    }

    @Test
    fun `isButtonEnabled should be true when all fields are valid and terms are checked`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isButtonEnabled)

            viewModel.onFullNameChange(name)
            awaitItem()

            viewModel.onEmailChange(email)
            awaitItem()

            viewModel.onPasswordChange(password)
            awaitItem()

            viewModel.onTermCheckChanged(true)

            val finalState = awaitItem()
            assertTrue(finalState.isButtonEnabled)
        }
    }

    @Test
    fun `isButtonEnabled should be false when all fields are valid but not term check`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onFullNameChange(name)
            awaitItem()

            viewModel.onEmailChange(email)
            awaitItem()

            viewModel.onPasswordChange(password)

            val finalState = awaitItem()

            assertFalse( finalState.isButtonEnabled)
            assertFalse(finalState.isTermSelected)
        }
    }

    @Test
    fun `onRegister should show success bottom sheet when register success and send expire time`() = runTest {
        val registerFlow = MutableSharedFlow<TasstyResponse<OtpTimer>>(replay = 1)
        coEvery { registerUseCase(any(), any(),any(),any()) } returns registerFlow

        coEvery { updateStatusUseCase(any()) } returns Unit

        viewModel.onFullNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        turbineScope {
            val eventTurbine = viewModel.events.testIn(backgroundScope)
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.expectMostRecentItem()

            viewModel.onRegister()

            registerFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertTrue(loadingState.isLoading)

            registerFlow.emit(DataDummy.registerResponseSuccess)

            val successState = stateTurbine.awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.isBottomSuccessVisible)

            viewModel.onConfirmVerification()

            val event = eventTurbine.awaitItem()
            assertTrue(event is RegisterEvent.NavigateToVerify)

            val navigateEvent = (event as RegisterEvent.NavigateToVerify)
            assertEquals(300, navigateEvent.expireIn)
            assertEquals(60, navigateEvent.resendAvailableIn)

            coVerify { updateStatusUseCase(any()) }

            eventTurbine.cancelAndIgnoreRemainingEvents()
            stateTurbine.cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `onRegister should show error bottom sheet when register failed`() = runTest {
        val registerFlow = MutableSharedFlow<TasstyResponse<OtpTimer>>(replay = 1)
        coEvery { registerUseCase(any(), any(),any(),any()) } returns registerFlow

        viewModel.onFullNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.expectMostRecentItem()

            viewModel.onRegister()

            registerFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitItem()
            assertTrue(loadingState.isLoading)

            registerFlow.emit(DataDummy.registerResponseFailed)

            val failedState = stateTurbine.awaitItem()
            assertFalse(failedState.isLoading)
            assertNotNull(failedState.bottomSheetMessage)
            assertTrue(failedState.isBottomFailedVisible)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDismissBottomSheet should hide failed bottom sheet and update state`() = runTest {
        val registerFlow = MutableSharedFlow<TasstyResponse<OtpTimer>>(replay = 1)
        coEvery { registerUseCase(any(), any(), any(), any()) } returns registerFlow

        viewModel.onFullNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onTermCheckChanged(true)
        viewModel.onRegister()

        registerFlow.emit(DataDummy.registerResponseFailed)

        viewModel.uiState.test {
            val errorState = expectMostRecentItem()
            assertTrue(errorState.isBottomFailedVisible)

            viewModel.onDismissBottomSheet()

            val finalState = awaitItem()
            assertFalse(finalState.isBottomFailedVisible)
        }
    }
}