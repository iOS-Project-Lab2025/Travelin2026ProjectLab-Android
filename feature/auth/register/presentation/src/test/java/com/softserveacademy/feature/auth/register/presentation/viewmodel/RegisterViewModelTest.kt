package com.softserveacademy.feature.auth.register.presentation.viewmodel

import com.softserveacademy.feature.auth.register.domain.usecase.RegisterUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar

/**
 * Unit tests for [RegisterViewModel] following the project's testing policy.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val registerUseCase = mockk<RegisterUseCase>()
    private lateinit var viewModel: RegisterViewModel

    private fun getBirthDate(age: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -age)
        return calendar.timeInMillis
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(registerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid data and terms accepted when register clicked then sets success state`() = runTest {
        // GIVEN
        viewModel.firstName = "John"
        viewModel.lastName = "Doe"
        viewModel.birthDate = getBirthDate(25)
        viewModel.email = "john@example.com"
        viewModel.password = "password"
        viewModel.termsAccepted = true
        coEvery { registerUseCase.invoke(any(), any()) } returns Result.success(Unit)

        // WHEN
        viewModel.onRegisterClick()

        // THEN
        assertTrue(viewModel.isSuccess)
    }

    @Test
    fun `given terms not accepted when register clicked then sets error message`() = runTest {
        // GIVEN
        viewModel.termsAccepted = false

        // WHEN
        viewModel.onRegisterClick()

        // THEN
        assertEquals("You must accept the terms and conditions", viewModel.error)
    }
}
