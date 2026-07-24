package com.softserveacademy.feature.booking.hotel.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.feature.booking.hotel.domain.model.ContactInfo
import com.softserveacademy.feature.booking.hotel.domain.model.HotelBookingDraft
import com.softserveacademy.feature.booking.hotel.domain.repository.HotelBookingDraftRepository
import com.softserveacademy.feature.booking.common.domain.usecase.ValidateContactInfoUseCase
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
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
    private lateinit var hotelBookingDraftRepository: HotelBookingDraftRepository
    private lateinit var validateContactInfoUseCase: ValidateContactInfoUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val hotelId = 123

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))
        hotelBookingDraftRepository = mockk(relaxed = true)
        validateContactInfoUseCase = ValidateContactInfoUseCase()
        
        coEvery { hotelBookingDraftRepository.getDraft(hotelId.toString()) } returns null
        
        viewModel = HotelContactInfoViewModel(
            savedStateHandle = savedStateHandle,
            hotelBookingDraftRepository = hotelBookingDraftRepository,
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
        coEvery { hotelBookingDraftRepository.getDraft(hotelId.toString()) } returns draft

        val freshSavedStateHandle = SavedStateHandle(mapOf("hotelId" to hotelId))
        val newViewModel = HotelContactInfoViewModel(
            savedStateHandle = freshSavedStateHandle,
            hotelBookingDraftRepository = hotelBookingDraftRepository,
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
        viewModel.onEvent(TravelBookingContactInfoEvent.FirstNameChanged("Jane"))
        assertEquals("Jane", viewModel.uiState.value.firstName)
        assertNull(viewModel.uiState.value.firstNameError)
    }

    @Test
    fun `onEvent CountryCodeChanged updates state`() = runTest {
        viewModel.onEvent(TravelBookingContactInfoEvent.CountryCodeChanged("+380"))
        assertEquals("+380", viewModel.uiState.value.countryCode)
    }

    @Test
    fun `onNextClick with invalid data sets errors and does not set validationSuccess`() = runTest {
        viewModel.onEvent(TravelBookingContactInfoEvent.FirstNameChanged(""))
        viewModel.onEvent(TravelBookingContactInfoEvent.OnNextClick)

        val state = viewModel.uiState.value
        assertEquals("First name is required", state.firstNameError)
        assertFalse(viewModel.validationSuccess.value)
    }

    @Test
    fun `onNextClick with valid data saves draft and sets validationSuccess`() = runTest {
        viewModel.onEvent(TravelBookingContactInfoEvent.FirstNameChanged("John"))
        viewModel.onEvent(TravelBookingContactInfoEvent.LastNameChanged("Doe"))
        viewModel.onEvent(TravelBookingContactInfoEvent.EmailChanged("john.doe@example.com"))
        viewModel.onEvent(TravelBookingContactInfoEvent.PhoneNumberChanged("123456789"))
        
        viewModel.onEvent(TravelBookingContactInfoEvent.OnNextClick)
        
        advanceUntilIdle()
        
        assertTrue(viewModel.validationSuccess.value)
        coVerify { hotelBookingDraftRepository.saveDraft(any()) }
    }

    @Test
    fun `resetValidationStatus sets validationSuccess to false`() = runTest {
        // First set it to true
        viewModel.onEvent(TravelBookingContactInfoEvent.FirstNameChanged("John"))
        viewModel.onEvent(TravelBookingContactInfoEvent.LastNameChanged("Doe"))
        viewModel.onEvent(TravelBookingContactInfoEvent.EmailChanged("john.doe@example.com"))
        viewModel.onEvent(TravelBookingContactInfoEvent.PhoneNumberChanged("123456789"))
        viewModel.onEvent(TravelBookingContactInfoEvent.OnNextClick)
        advanceUntilIdle()
        
        assertTrue(viewModel.validationSuccess.value)
        
        viewModel.resetValidationStatus()
        assertFalse(viewModel.validationSuccess.value)
    }
}
