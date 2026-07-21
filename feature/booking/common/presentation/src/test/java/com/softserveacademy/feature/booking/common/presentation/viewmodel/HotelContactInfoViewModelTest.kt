package com.softserveacademy.feature.booking.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.common.domain.model.ContactInfo
import com.softserveacademy.feature.booking.common.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.common.domain.repository.BookingRepository
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateContactInfoUseCase
import com.softserveacademy.feature.booking.common.presentation.events.HotelContactInfoEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HotelContactInfoViewModelTest {

    private lateinit var viewModel: HotelContactInfoViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var bookingRepository: BookingRepository
    private lateinit var validateContactInfoUseCase: ValidateContactInfoUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val hotelId = 123

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))
        bookingRepository = mockk(relaxed = true)
        validateContactInfoUseCase = ValidateContactInfoUseCase()
        
        coEvery { bookingRepository.getHotelBookingDraft(hotelId.toString()) } returns null
        
        viewModel = HotelContactInfoViewModel(
            savedStateHandle = savedStateHandle,
            bookingRepository = bookingRepository,
            validateContactInfoUseCase = validateContactInfoUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty when no draft exists`() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals("", state.firstName)
        assertEquals("", state.lastName)
        assertEquals("", state.email)
        assertEquals("", state.phoneNumber)
        assertNull(state.firstNameError)
    }

    @Test
    fun `loads draft data on initialization`() = runTest {
        val draft = HotelBookingDraft(
            hotelId = hotelId.toString(),
            contactInfo = ContactInfo(
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                phoneNumber = "123456789"
            )
        )
        coEvery { bookingRepository.getHotelBookingDraft(hotelId.toString()) } returns draft

        val newViewModel = HotelContactInfoViewModel(
            savedStateHandle = savedStateHandle,
            bookingRepository = bookingRepository,
            validateContactInfoUseCase = validateContactInfoUseCase
        )

        advanceUntilIdle()

        val state = newViewModel.uiState.value
        assertEquals("John", state.firstName)
        assertEquals("Doe", state.lastName)
        assertEquals("john@example.com", state.email)
        assertEquals("123456789", state.phoneNumber)
    }

    @Test
    fun `onEvent FirstNameChanged updates state and clears error`() = runTest {
        viewModel.onEvent(HotelContactInfoEvent.FirstNameChanged("Jane"))
        assertEquals("Jane", viewModel.uiState.value.firstName)
        assertNull(viewModel.uiState.value.firstNameError)
    }

    @Test
    fun `onEvent CountryCodeChanged updates state`() = runTest {
        viewModel.onEvent(HotelContactInfoEvent.CountryCodeChanged("+380"))
        assertEquals("+380", viewModel.uiState.value.countryCode)
    }

    @Test
    fun `onNextClick with invalid data sets errors and does not set validationSuccess`() = runTest {
        viewModel.onEvent(HotelContactInfoEvent.FirstNameChanged(""))
        viewModel.onEvent(HotelContactInfoEvent.OnNextClick)

        val state = viewModel.uiState.value
        assertEquals("First name is required", state.firstNameError)
        assertFalse(viewModel.validationSuccess.value)
    }

    @Test
    fun `onNextClick with valid data saves draft and sets validationSuccess`() = runTest {
        viewModel.onEvent(HotelContactInfoEvent.FirstNameChanged("John"))
        viewModel.onEvent(HotelContactInfoEvent.LastNameChanged("Doe"))
        viewModel.onEvent(HotelContactInfoEvent.EmailChanged("john.doe@example.com"))
        viewModel.onEvent(HotelContactInfoEvent.PhoneNumberChanged("123456789"))
        
        viewModel.onEvent(HotelContactInfoEvent.OnNextClick)
        
        advanceUntilIdle()
        
        assertTrue(viewModel.validationSuccess.value)
        coVerify { bookingRepository.saveHotelBookingDraft(any()) }
    }

    @Test
    fun `resetValidationStatus sets validationSuccess to false`() = runTest {
        // First set it to true
        viewModel.onEvent(HotelContactInfoEvent.FirstNameChanged("John"))
        viewModel.onEvent(HotelContactInfoEvent.LastNameChanged("Doe"))
        viewModel.onEvent(HotelContactInfoEvent.EmailChanged("john.doe@example.com"))
        viewModel.onEvent(HotelContactInfoEvent.PhoneNumberChanged("123456789"))
        viewModel.onEvent(HotelContactInfoEvent.OnNextClick)
        advanceUntilIdle()
        
        assertTrue(viewModel.validationSuccess.value)
        
        viewModel.resetValidationStatus()
        assertFalse(viewModel.validationSuccess.value)
    }
}
