package com.example.tassty.screen.register

import app.cash.turbine.test
import com.example.core.domain.usecase.RegisterEmailPasswordUseCase
import com.example.core.domain.usecase.UpdateAuthStatusUseCase
import com.example.tassty.R
import com.example.tassty.util.UiText
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private lateinit var viewModel: RegisterViewModel
    private val registerUseCase = mockk<RegisterEmailPasswordUseCase>()
    private val updateStatusUseCase = mockk<UpdateAuthStatusUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(
            registerEmailPasswordUseCase = registerUseCase,
            updateAuthStatusUseCase = updateStatusUseCase
        )
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
    fun `onNameChange should update name state and clear error`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            val inputName = "atifa"
            viewModel.onFullNameChange(inputName)

            val state = awaitItem()
            assertEquals(inputName, state.fullName)
            assertEquals(null, state.fullNameError)
        }
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

            viewModel.onFullNameChange("Luna Atifa")
            awaitItem()

            viewModel.onEmailChange("luna@tassty.com")
            awaitItem()

            viewModel.onPasswordChange("password123")
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

            viewModel.onFullNameChange("Luna Atifa")
            awaitItem()

            viewModel.onEmailChange("luna@tassty.com")
            awaitItem()

            viewModel.onPasswordChange("password123")

            val finalState = awaitItem()

            assertFalse( finalState.isButtonEnabled)
            assertFalse(finalState.isTermSelected)
        }
    }
}