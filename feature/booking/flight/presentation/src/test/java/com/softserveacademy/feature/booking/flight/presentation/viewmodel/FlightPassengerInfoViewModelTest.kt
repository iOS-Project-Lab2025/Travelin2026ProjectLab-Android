package com.softserveacademy.feature.booking.flight.presentation.viewmodel

import com.softserveacademy.core.domain.model.FlightContactInfo
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.feature.booking.flight.domain.model.FlightBookingDraft
import com.softserveacademy.feature.booking.flight.domain.model.PassengerValidationResult
import com.softserveacademy.feature.booking.flight.domain.repository.FlightBookingDraftRepository
import com.softserveacademy.feature.booking.flight.domain.usecase.ValidateFlightPassengerInfoUseCase
import com.softserveacademy.feature.booking.flight.presentation.events.FlightPassengerInfoEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [FlightPassengerInfoViewModel].
 *
 * Verifies the Passenger Wizard logic (US3):
 * 1. Wizard navigation (Forward/Backward through multiple passengers).
 * 2. Real-time data updates and validation error mapping.
 * 3. Final data sanitization (Camel Case) before persistence.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FlightPassengerInfoViewModelTest {

    private val draftRepository: FlightBookingDraftRepository = mockk(relaxed = true)
    private val validateUseCase: ValidateFlightPassengerInfoUseCase = mockk()
    private lateinit var viewModel: FlightPassengerInfoViewModel

    private val testDispatcher = StandardTestDispatcher()

    // Mock setup for a 2-passenger booking
    private val mockDraft = FlightBookingDraft(
        adults = 2,
        children = 0,
        passengers = emptyList() // Initial empty state
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { draftRepository.getDraft() } returns flowOf(mockDraft)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Integrity Test: Ensures the ViewModel correctly advances from Passenger 1 to Passenger 2
     * when the data is valid.
     */
    @Test
    fun `when next is clicked and current passenger is valid, should increment index`() = runTest {
        // 1. Prepare: Mock valid result
        every { validateUseCase.validate(any(), any()) } returns PassengerValidationResult(isValid = true)

        viewModel = FlightPassengerInfoViewModel(draftRepository, validateUseCase)
        advanceUntilIdle()

        // 2. Action: Click Next on index 0
        viewModel.onEvent(FlightPassengerInfoEvent.OnNextClick)
        advanceUntilIdle()

        // 3. Verify
        assertEquals("Wizard should advance to next passenger", 1, viewModel.uiState.value.currentPassengerIndex)
    }

    /**
     * Logic Test: Ensures that 'Back' from the first passenger triggers a navigation back to results.
     */
    @Test
    fun `when back is clicked on first passenger, should emit navigation back event`() = runTest {
        viewModel = FlightPassengerInfoViewModel(draftRepository, validateUseCase)
        advanceUntilIdle()

        val backEventTriggered = mutableListOf<Unit>()
        val job = launch { viewModel.navigationBackEvent.collect { backEventTriggered.add(it) } }

        viewModel.onEvent(FlightPassengerInfoEvent.OnBackClick)
        advanceUntilIdle()

        assertTrue("Back navigation event should be emitted", backEventTriggered.isNotEmpty())
        job.cancel()
    }

    /**
     * Business Rule Test: Verifies that name sanitization (Camel Case) occurs during final save.
     */
    @Test
    fun `when final passenger is valid, should sanitize names to Title Case before saving`() = runTest {
        // Setup: Last passenger (index 1 for a 2-pax draft)
        every { validateUseCase.validate(any(), any()) } returns PassengerValidationResult(isValid = true)

        viewModel = FlightPassengerInfoViewModel(draftRepository, validateUseCase)
        advanceUntilIdle()

        // Move to last passenger
        viewModel.onEvent(FlightPassengerInfoEvent.OnNextClick)
        advanceUntilIdle()

        // Input messy name
        val messyPassenger = FlightPassenger(firstName = "jOHN", lastName = "dOE pEREZ", passengerType = PassengerType.ADU)
        viewModel.onEvent(FlightPassengerInfoEvent.OnPassengerDataChanged(1, messyPassenger))

        // Final Next click (Save and Finish)
        viewModel.onEvent(FlightPassengerInfoEvent.OnNextClick)
        advanceUntilIdle()

        // Verify Repository was called with SANITIZED data
        coVerify {
            draftRepository.saveDraft(withArg { draft ->
                val savedPax = draft.passengers[1]
                assertEquals("John", savedPax.firstName)
                assertEquals("Doe Perez", savedPax.lastName)
            })
        }
    }

    /**
     * UI State Test: Confirms that validation errors are correctly mapped back to the UI.
     */
    @Test
    fun `when validation fails, should update state with specific passenger errors`() = runTest {
        // Mock a failure specifically for the first passenger
        val mockError = com.softserveacademy.feature.booking.flight.domain.model.PassengerError(firstNameError = com.softserveacademy.feature.booking.flight.domain.model.PassengerFieldError.EMPTY)
        every { validateUseCase.validate(any(), any()) } returns PassengerValidationResult(
            isValid = false,
            passengerErrors = mapOf(0 to mockError)
        )

        viewModel = FlightPassengerInfoViewModel(draftRepository, validateUseCase)
        advanceUntilIdle()

        viewModel.onEvent(FlightPassengerInfoEvent.OnNextClick)

        val state = viewModel.uiState.value
        assertEquals(0, state.currentPassengerIndex) // Should NOT advance
        assertTrue("Error map should contain current index", state.passengerErrors.containsKey(0))
    }
}