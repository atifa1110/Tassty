package com.example.tassty.screen.restpassword

import app.cash.turbine.turbineScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.ResetPasswordUseCase
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.screen.resetpassword.ResetPasswordEvent
import com.example.tassty.screen.resetpassword.ResetPasswordViewModel
import com.example.tassty.utils.DataDummy
import com.example.tassty.utils.awaitState
import io.mockk.coEvery
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: ResetPasswordViewModel

    private val resetPasswordUseCase: ResetPasswordUseCase = mockk()

    @Before
    fun setup() {
        viewModel = ResetPasswordViewModel(resetPasswordUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onPasswordChange should update password and clear error`() = runTest {
        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            // kasih error dulu biar bisa dites ke-clear
            viewModel.onPasswordChange("123")
            runCurrent()

            val first = stateTurbine.awaitItem()

            // trigger perubahan baru
            viewModel.onPasswordChange("NewPassword123")
            runCurrent()

            val state = stateTurbine.awaitState { it.password == "NewPassword123" }

            assertEquals("NewPassword123", state.password)
            assertNull(state.passwordError)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onConfirmChange should update confirm password and clear error`() = runTest {
        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onConfirmChange("123")
            runCurrent()

            val first = stateTurbine.awaitItem()

            viewModel.onConfirmChange("NewPassword123")
            runCurrent()

            val state = stateTurbine.awaitState { it.confirmPassword == "NewPassword123" }

            assertEquals("NewPassword123", state.confirmPassword)
            assertNull(state.confirmPasswordError)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `button should be enabled when password and confirm are filled`() = runTest {
        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onPasswordChange("Password123")
            viewModel.onConfirmChange("Password123")
            runCurrent()

            val state = stateTurbine.awaitState {
                it.password.isNotBlank() && it.confirmPassword.isNotBlank()
            }

            assertTrue(state.isButtonEnabled)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `button should be disabled when loading`() = runTest {
        val flow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { resetPasswordUseCase(any()) } returns flow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onPasswordChange("Password123")
            viewModel.onConfirmChange("Password123")

            viewModel.onValidateReset()
            runCurrent()

            flow.emit(TasstyResponse.Loading())
            runCurrent()

            val state = stateTurbine.awaitState { it.isLoading }

            assertFalse(state.isButtonEnabled)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onValidateReset should show confirm password error when not match`() = runTest {
        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onPasswordChange("Password123")
            viewModel.onConfirmChange("Different123")

            viewModel.onValidateReset()
            runCurrent()

            val state = stateTurbine.awaitState { it.confirmPasswordError != null }

            assertNotNull(state.confirmPasswordError)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onValidateReset should handle error response`() = runTest {
        val flow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)

        coEvery { resetPasswordUseCase(any()) } returns flow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val eventTurbine = viewModel.events.testIn(backgroundScope)

            viewModel.onPasswordChange("Password123")
            viewModel.onConfirmChange("Password123")

            val initial = stateTurbine.awaitState { it.password.isNotBlank() }
            assertTrue(initial.isButtonEnabled)

            viewModel.onValidateReset()
            runCurrent()

            flow.emit(TasstyResponse.Loading())
            runCurrent()

            val loading = stateTurbine.awaitState { it.isLoading }
            assertTrue(loading.isLoading)

            val error = DataDummy.resetResponseFailed

            flow.emit(error)
            runCurrent()

            val final = stateTurbine.awaitState { !it.isLoading }

            assertFalse(final.isLoading)
            assertTrue(final.isButtonEnabled)

            val event = eventTurbine.awaitItem()
            assertTrue(event is ResetPasswordEvent.ShowMessage)

            stateTurbine.cancelAndIgnoreRemainingEvents()
            eventTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onValidateReset should handle success response`() = runTest {
        val flow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { resetPasswordUseCase(any()) } returns flow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onPasswordChange("Password123")
            viewModel.onConfirmChange("Password123")

            viewModel.onValidateReset()
            runCurrent()

            flow.emit(TasstyResponse.Loading())
            runCurrent()

            stateTurbine.awaitState { it.isLoading }

            flow.emit(DataDummy.resetResponseSuccess)
            runCurrent()

            val final = stateTurbine.awaitState { !it.isLoading }

            assertTrue(final.isBottomSheetVisible)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onValidateReset should show validation error`() = runTest {
        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onPasswordChange("123")
            viewModel.onConfirmChange("123")

            viewModel.onValidateReset()
            runCurrent()

            val state = stateTurbine.awaitState { it.passwordError != null }

            assertNotNull(state.passwordError)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }
}