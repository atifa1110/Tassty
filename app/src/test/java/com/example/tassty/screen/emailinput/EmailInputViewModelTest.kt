package com.example.tassty.screen.emailinput

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.usecase.ForgotPasswordUseCase
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.utils.DataDummy
import com.example.tassty.utils.awaitState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EmailInputViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: EmailInputViewModel

    private val forgotPasswordUseCase: ForgotPasswordUseCase = mockk()

    private fun initViewModel() {
        viewModel = EmailInputViewModel(forgotPasswordUseCase)
    }

    @Test
    fun `init should have empty email and button disabled`() = runTest {
        initViewModel()

        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals("", state.email)
            assertFalse(state.isButtonEnabled)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `onEmailChange should update email and clear error`() = runTest {
        initViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEmailChange("test@email.com")

            val state = awaitItem()

            assertEquals("test@email.com", state.email)
            assertNull(state.emailError)
            assertTrue(state.isButtonEnabled)
        }
    }

    @Test
    fun `onSendOtpToEmail with invalid email should set email error`() = runTest {
        initViewModel()

        viewModel.onEmailChange("invalid-email")
        viewModel.onSendOtpToEmail()

        viewModel.uiState.test {
            val state = awaitState { it.emailError != null }

            assertNotNull(state.emailError)
            assertFalse(state.isLoading)
        }

        coVerify(exactly = 0) { forgotPasswordUseCase(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSendOtpToEmail should set loading true when loading`() = runTest {
        initViewModel()

        val flow = MutableSharedFlow<TasstyResponse<OtpTimer>>()
        coEvery { forgotPasswordUseCase(any()) } returns flow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onEmailChange("test@email.com")
            viewModel.onSendOtpToEmail()

            runCurrent()

            flow.emit(TasstyResponse.Loading())

            val state = stateTurbine.awaitState { it.isLoading }

            assertTrue(state.isLoading)
            assertFalse(state.isButtonEnabled)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSendOtpToEmail should handle error response`() = runTest {
        initViewModel()

        val flow = MutableSharedFlow<TasstyResponse<OtpTimer>>(replay = 1)

        coEvery { forgotPasswordUseCase(any()) } returns flow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onEmailChange("test@email.com")

            val initial = stateTurbine.awaitState { it.email.isNotBlank() }
            assertTrue(initial.isButtonEnabled)

            viewModel.onSendOtpToEmail()
            runCurrent()

            flow.emit(TasstyResponse.Loading())
            runCurrent()

            val loadingState = stateTurbine.awaitState { it.isLoading }
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isButtonEnabled)

            flow.emit(DataDummy.resendResponseFailed)
            runCurrent()

            val final = stateTurbine.awaitState { !it.isLoading }

            assertFalse(final.isLoading)
            assertTrue(final.isButtonEnabled)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSendOtpToEmail success should emit navigation event`() = runTest {
        initViewModel()

        val flow = MutableSharedFlow<TasstyResponse<OtpTimer>>()
        coEvery { forgotPasswordUseCase(any()) } returns flow

        turbineScope {
            val eventTurbine = viewModel.events.testIn(backgroundScope)

            viewModel.onEmailChange("test@email.com")
            viewModel.onSendOtpToEmail()

            runCurrent()

            val response = DataDummy.registerResponseSuccess
            flow.emit(response)

            val event = eventTurbine.awaitItem()

            assertTrue(event is EmailInputEvent.NavigateToVerifyReset)

            val navEvent = event as EmailInputEvent.NavigateToVerifyReset
            assertEquals(response.data?.expireIn, navEvent.expireIn)
            assertEquals(response.data?.resendAvailableIn, navEvent.resendAvailableIn)

            eventTurbine.cancelAndIgnoreRemainingEvents()
        }
    }
}