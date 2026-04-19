package com.example.tassty.screen.verification

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.usecase.ForgotPasswordUseCase
import com.example.core.domain.usecase.GetAuthStatusUseCase
import com.example.core.domain.usecase.ResendEmailOtpUseCase
import com.example.core.domain.usecase.VerifyEmailOtpUseCase
import com.example.core.domain.usecase.VerifyResetOtpUseCase
import com.example.tassty.VerificationType
import com.example.tassty.dispatcher.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import com.example.tassty.R
import com.example.tassty.utils.DataDummy
import com.example.tassty.utils.awaitState
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent

class VerificationViewModelTest {

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var viewModel: VerificationViewModel

    private val getAuthStatusUseCase: GetAuthStatusUseCase = mockk()
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase = mockk()
    private val verifyResetOtpUseCase: VerifyResetOtpUseCase = mockk()
    private val resendEmailOtpUseCase: ResendEmailOtpUseCase = mockk()
    private val forgotPasswordUseCase: ForgotPasswordUseCase = mockk()

    private val email = "user@tassty.com"

    private fun createSavedStateHandle(type: VerificationType, delay: Int = 60): SavedStateHandle {
        return SavedStateHandle(
            mapOf(
                "type" to type.name,
                "resendDelay" to delay
            )
        )
    }

    private fun initViewModel(
        type: VerificationType = VerificationType.REGISTRATION,
        delay: Int = 60
    ) {
        val savedStateHandle = createSavedStateHandle(type, delay)
        viewModel = VerificationViewModel(
            savedStateHandle,
            getAuthStatusUseCase,
            verifyEmailOtpUseCase,
            verifyResetOtpUseCase,
            resendEmailOtpUseCase,
            forgotPasswordUseCase
        )
    }

    @Before
    fun setup() {
        coEvery { getAuthStatusUseCase() } returns flowOf(AuthStatus(email = email))
    }

    @Test
    fun `init should set correct email`() = runTest {
        coEvery { getAuthStatusUseCase() } returns flowOf(DataDummy.dummyAuthStatus)
        initViewModel(VerificationType.REGISTRATION)

        viewModel.uiState.test {
            val state = awaitState { it.email.isNotEmpty() }
            assertEquals(DataDummy.dummyAuthStatus.email, state.email)
        }
    }

    @Test
    fun `init should set correct title and instructions for registration type`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(R.string.verify_email_title, state.title)
            assertEquals(R.string.verify_email_instruction, state.instruction)
        }
    }

    @Test
    fun `init should set correct title and instructions for forgot password type`() = runTest {
        initViewModel(VerificationType.FORGOT_PASSWORD)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(R.string.reset_password_title, state.title)
            assertEquals(R.string.reset_password_instruction, state.instruction)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `startTimer should decrease timerSeconds every second`() = runTest {
        initViewModel(delay = 60)

        viewModel.uiState.test {
            // Tunggu state awal stabil di 60
            awaitState { it.timerSeconds == 60 }

            // Maju 1 detik di jam virtual
            mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(1000)

            // Cek apakah jadi 59
            val state = awaitState { it.timerSeconds == 59 }
            assertEquals(59, state.timerSeconds)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `startTimer should reset countdown`() = runTest {
        initViewModel(delay = 60)

        viewModel.uiState.test {
            // Biarkan timer jalan sampai ke detik 55
            mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(5000)
            awaitState { it.timerSeconds == 55 }

            viewModel.startTimer()
            runCurrent()

            // Pastikan balik ke 60
            val state = awaitState { it.timerSeconds == 60 }
            assertEquals(60, state.timerSeconds)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `resend button should only be enabled when timer reaches zero`() = runTest {
        // Set delay pendek saja, misal 5 detik biar ngetesnya cepet
        initViewModel(delay = 5)

        viewModel.uiState.test {
            // Cek saat awal (Timer 5) -> Tombol harus mati
            val initialState = awaitState { it.timerSeconds == 5 }
            assertFalse("Button should be disabled when timer > 0", initialState.isResendEnabled)

            // Maju 5 detik ke masa depan
            mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(5000)

            // ek saat Timer jadi 0 -> Tombol harus nyala
            val finalState = awaitState { it.timerSeconds == 0 }
            assertTrue("Button should be enabled when timer is 0", finalState.isResendEnabled)
        }
    }

    @Test
    fun `onOtpChange should filter non-digit characters and limit to 6 digits`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        viewModel.uiState.test {
            awaitItem()

            val inputInput = "12a3b456789"
            viewModel.onOtpChange(inputInput)

            val state = awaitItem()

            assertEquals("123456", state.otp)

            assertFalse(state.isError)
            assertTrue(state.isButtonEnabled)
        }
    }

    @Test
    fun `onOtpChange with empty string should update state correctly`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onOtpChange("")

            val state = awaitItem()
            assertEquals("", state.otp)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onErrorDismiss should clear error message and isError flag`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        val verifyFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { verifyEmailOtpUseCase(any(), any()) } returns verifyFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            viewModel.onOtpChange("123456")
            stateTurbine.awaitState { it.otp == "123456" && it.email.isNotEmpty() }
            viewModel.onVerificationCode()
            runCurrent()

            verifyFlow.emit(DataDummy.verificationResponseFailed)

            val errorState = stateTurbine.awaitState { it.isError }
            assertTrue(errorState.isError)
            assertEquals(DataDummy.verificationResponseFailed.meta.message, errorState.errorMessage)

            viewModel.onErrorDismiss()
            runCurrent()

            val clearedState = stateTurbine.awaitState { !it.isError }
            assertFalse(clearedState.isError)
            assertEquals("", clearedState.errorMessage)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `button should be enabled when otp valid, email exists, and not loading`() = runTest {
        initViewModel()

        viewModel.uiState.test {
            // tunggu email ada
            awaitState { it.email.isNotEmpty() }

            // input OTP valid
            viewModel.onOtpChange("123456")

            val state = awaitState { it.otp == "123456" }

            assertTrue(state.isButtonEnabled)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode with incomplete OTP should show error when type is REGISTRATION`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        viewModel.onOtpChange("123")
        viewModel.onVerificationCode()

        runCurrent()

        viewModel.uiState.test {
            val state = awaitState { it.isError }
            assertEquals("OTP harus 6 digit", state.errorMessage)
        }

        coVerify (exactly = 0) { verifyEmailOtpUseCase(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode with incomplete OTP should show error when type is FORGOT PASSWORD`() = runTest {
        initViewModel(VerificationType.FORGOT_PASSWORD)

        viewModel.onOtpChange("123")
        viewModel.onVerificationCode()

        runCurrent()

        viewModel.uiState.test {
            val state = awaitState { it.isError }
            assertEquals("OTP harus 6 digit", state.errorMessage)
        }

        coVerify (exactly = 0) { verifyResetOtpUseCase(any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode should emit NavigateToSetUp when type is REGISTRATION`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        val verifyFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { verifyEmailOtpUseCase(any(), any()) } returns verifyFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val eventTurbine = viewModel.event.testIn(backgroundScope)

            viewModel.onOtpChange("123456")

            stateTurbine.awaitState { it.otp == "123456" && it.email.isNotEmpty() }

            viewModel.onVerificationCode()

            runCurrent()

            verifyFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitState { it.isLoading }
            assertTrue(loadingState.isLoading)

            verifyFlow.emit(DataDummy.verificationResponseSuccess)
            val successState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(successState.isLoading)

            val eventState = eventTurbine.awaitItem()
            assertTrue(eventState is VerificationEvent.NavigateToSetUp)

            eventTurbine.cancelAndIgnoreRemainingEvents()
            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode should emit error message when verification failed and type is REGISTRATION`() = runTest {
        initViewModel(VerificationType.REGISTRATION)

        val verifyFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { verifyEmailOtpUseCase(any(), any()) } returns verifyFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            viewModel.onOtpChange("123456")

            stateTurbine.awaitState { it.otp == "123456" && it.email.isNotEmpty() }

            viewModel.onVerificationCode()

            runCurrent()

            verifyFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitState { it.isLoading }
            assertTrue(loadingState.isLoading)

            verifyFlow.emit(DataDummy.verificationResponseFailed)
            val successState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(successState.isLoading)

            val errorState = stateTurbine.awaitState { !it.isLoading && it.isError }

            assertEquals(DataDummy.verificationResponseFailed.meta.message, errorState.errorMessage)
            assertTrue(errorState.isError)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode should emit NavigateToNewPassword when type is FORGOT PASSWORD`() = runTest {
        initViewModel(VerificationType.FORGOT_PASSWORD)

        val verifyFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { verifyResetOtpUseCase(any(), any()) } returns verifyFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val eventTurbine = viewModel.event.testIn(backgroundScope)

            viewModel.onOtpChange("123456")

            stateTurbine.awaitState { it.otp == "123456" && it.email.isNotEmpty() }

            viewModel.onVerificationCode()

            runCurrent()

            verifyFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitState { it.isLoading }
            assertTrue(loadingState.isLoading)

            verifyFlow.emit(DataDummy.verificationResponseSuccess)
            val successState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(successState.isLoading)

            val eventState = eventTurbine.awaitItem()
            assertTrue(eventState is VerificationEvent.NavigateToNewPassword)

            eventTurbine.cancelAndIgnoreRemainingEvents()
            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVerificationCode should emit error message when verification failed and type is FORGOT PASSWORD`() = runTest {
        initViewModel(VerificationType.FORGOT_PASSWORD)

        val verifyFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { verifyResetOtpUseCase(any(), any()) } returns verifyFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            viewModel.onOtpChange("123456")

            stateTurbine.awaitState { it.otp == "123456" && it.email.isNotEmpty() }

            viewModel.onVerificationCode()

            runCurrent()

            verifyFlow.emit(TasstyResponse.Loading())

            val loadingState = stateTurbine.awaitState { it.isLoading }
            assertTrue(loadingState.isLoading)

            verifyFlow.emit(DataDummy.verificationResponseFailed)
            val successState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(successState.isLoading)

            val errorState = stateTurbine.awaitState { !it.isLoading && it.isError }

            assertEquals(DataDummy.verificationResponseFailed.meta.message, errorState.errorMessage)
            assertTrue(errorState.isError)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onResendVerification success should emit ShowMessage event when resend email`() = runTest {
        initViewModel(delay = 60)

        val resendFlow = MutableSharedFlow<TasstyResponse<String>>()
        coEvery { resendEmailOtpUseCase(any()) } returns resendFlow

        turbineScope {
            val eventTurbine = viewModel.event.testIn(backgroundScope)
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.awaitState { it.email.isNotEmpty() }
            viewModel.onResendVerification()
            runCurrent()

            val successResponse = DataDummy.resendResponseSuccess
            resendFlow.emit(successResponse)
            runCurrent()

            val finalState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(finalState.isLoading)
            assertTrue(finalState.timerSeconds > 0)

            val event = eventTurbine.awaitItem()
            assertTrue(event is VerificationEvent.ShowMessage)
            assertEquals(successResponse.meta.message, (event as VerificationEvent.ShowMessage).message)


            stateTurbine.cancelAndIgnoreRemainingEvents()
            eventTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onResendVerification failed should stop timer and enable resend button`() = runTest {
        coEvery { getAuthStatusUseCase() } returns flowOf(AuthStatus(email = "luna@tassty.com"))

        initViewModel(delay = 60)

        val resendFlow = MutableSharedFlow<TasstyResponse<String>>(replay = 1)
        coEvery { resendEmailOtpUseCase(any()) } returns resendFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.awaitState { it.email.isNotEmpty() }

            // Trigger resend
            viewModel.onResendVerification()
            runCurrent()

            // Emit error
            resendFlow.emit(DataDummy.resendResponseFailed)

            advanceUntilIdle()

            val finalState = stateTurbine.expectMostRecentItem()

            assertEquals(0, finalState.timerSeconds)
            assertTrue(finalState.isError)
            assertTrue(finalState.isResendEnabled)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onResendVerification success should emit ShowMessage event when forgot password`() = runTest {
        initViewModel(VerificationType.FORGOT_PASSWORD, delay = 60)

        val resendFlow = MutableSharedFlow<TasstyResponse<OtpTimer>>()
        coEvery { forgotPasswordUseCase(any()) } returns resendFlow

        turbineScope {
            val eventTurbine = viewModel.event.testIn(backgroundScope)
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.awaitState { it.email.isNotEmpty() }
            viewModel.onResendVerification()
            runCurrent()

            val successResponse = DataDummy.registerResponseSuccess
            resendFlow.emit(successResponse)
            runCurrent()

            val finalState = stateTurbine.awaitState { !it.isLoading }
            assertFalse(finalState.isLoading)
            assertTrue(finalState.timerSeconds > 0)

            val event = eventTurbine.awaitItem()
            assertTrue(event is VerificationEvent.ShowMessage)
            assertEquals(successResponse.meta.message, (event as VerificationEvent.ShowMessage).message)

            stateTurbine.cancelAndIgnoreRemainingEvents()
            eventTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onResendVerification failed should stop timer and enable resend button when forgot password `() = runTest {
        coEvery { getAuthStatusUseCase() } returns flowOf(AuthStatus(email = "luna@tassty.com"))

        initViewModel(VerificationType.FORGOT_PASSWORD, delay = 60)

        val resendFlow = MutableSharedFlow<TasstyResponse<OtpTimer>>(replay = 1)
        coEvery { forgotPasswordUseCase(any()) } returns resendFlow

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)

            stateTurbine.awaitState { it.email.isNotEmpty() }

            // Trigger resend
            viewModel.onResendVerification()
            runCurrent()

            // Emit error
            resendFlow.emit(DataDummy.resendResponseFailed)

            advanceUntilIdle()

            val finalState = stateTurbine.expectMostRecentItem()

            assertEquals(0, finalState.timerSeconds)
            assertTrue(finalState.isError)
            assertTrue(finalState.isResendEnabled)

            stateTurbine.cancelAndIgnoreRemainingEvents()
        }
    }
}

