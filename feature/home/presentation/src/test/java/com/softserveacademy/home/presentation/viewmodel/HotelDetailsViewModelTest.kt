package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.usecase.GetAiRecommendationsUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyPlacesUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyTransportUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [HotelDetailsViewModel] adhering to US-Testing standards.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HotelDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private val getHotelDetailsUseCase = mockk<GetHotelDetailsUseCase>()
    private val getNearbyPlacesUseCase = mockk<GetNearbyPlacesUseCase>()
    private val getNearbyTransportUseCase = mockk<GetNearbyTransportUseCase>()
    private val getNearbyRestaurantsUseCase = mockk<GetNearbyRestaurantsUseCase>()
    private val getAiRecommendationsUseCase = mockk<GetAiRecommendationsUseCase>()
    private val savedStateHandle = SavedStateHandle()

    private lateinit var viewModel: HotelDetailsViewModel

    private val mockHotel = Hotel(
        id = "1",
        name = "Test Hotel",
        latitude = 10.0,
        longitude = 20.0,
        address = "Test Address",
        pricePerNight = 100.0,
        reviewRating = 4.5,
        numberOfReviews = 100,
        description = "Test Description",
        imageList = listOf("url1")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HotelDetailsViewModel(
            savedStateHandle,
            getHotelDetailsUseCase,
            getNearbyPlacesUseCase,
            getNearbyTransportUseCase,
            getNearbyRestaurantsUseCase,
            getAiRecommendationsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid query when VoiceSearch then updates recommendations in English`() = runTest {
        // GIVEN: A loaded hotel and successful AI response
        val query = "beach bars"
        val recommendations = listOf(
            AiRecommendation("Bar 1", 10.1, 20.1, "Nice bar", "Bar", null)
        )
        
        // Initial state with hotel
        coEvery { getHotelDetailsUseCase("1") } returns AppResult.Success(mockHotel)
        coEvery { getNearbyPlacesUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyTransportUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyRestaurantsUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        
        viewModel.onEvent(HotelDetailsEvent.Load("1"))
        advanceUntilIdle()

        coEvery { getAiRecommendationsUseCase(query, 10.0, 20.0) } coAnswers {
            delay(1000)
            AppResult.Success(recommendations)
        }

        // WHEN: Voice search is triggered
        viewModel.onEvent(HotelDetailsEvent.VoiceSearch(query))
        
        // THEN: State is updated with recommendations and loading finishes
        testDispatcher.scheduler.runCurrent()
        assertTrue(viewModel.hotelDetailsState.value.isAiLoading)
        
        advanceUntilIdle()
        
        assertFalse(viewModel.hotelDetailsState.value.isAiLoading)
        assertEquals(recommendations, viewModel.hotelDetailsState.value.aiRecommendations)
        assertEquals(query, viewModel.hotelDetailsState.value.lastVoiceQuery)
    }

    @Test
    fun `given query with no results when VoiceSearch then shows English empty message`() = runTest {
        // GIVEN: A loaded hotel and empty AI response
        val query = "unknown"
        coEvery { getHotelDetailsUseCase("1") } returns AppResult.Success(mockHotel)
        coEvery { getNearbyPlacesUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyTransportUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyRestaurantsUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        
        viewModel.onEvent(HotelDetailsEvent.Load("1"))
        advanceUntilIdle()

        coEvery { getAiRecommendationsUseCase(query, 10.0, 20.0) } returns AppResult.Success(emptyList())

        // WHEN: Voice search is triggered
        viewModel.onEvent(HotelDetailsEvent.VoiceSearch(query))
        advanceUntilIdle()
        
        // THEN: Shows English empty message
        val expectedMsg = "I couldn't find anything for '$query'. Try something else."
        assertEquals(expectedMsg, viewModel.hotelDetailsState.value.lastVoiceQuery)
        assertTrue(viewModel.hotelDetailsState.value.aiRecommendations.isEmpty())
    }

    @Test
    fun `given network error when VoiceSearch then shows English error message`() = runTest {
        // GIVEN: A loaded hotel and network error
        val query = "beach"
        coEvery { getHotelDetailsUseCase("1") } returns AppResult.Success(mockHotel)
        coEvery { getNearbyPlacesUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyTransportUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        coEvery { getNearbyRestaurantsUseCase(any(), any(), any()) } returns AppResult.Success(emptyList())
        
        viewModel.onEvent(HotelDetailsEvent.Load("1"))
        advanceUntilIdle()

        coEvery { getAiRecommendationsUseCase(query, 10.0, 20.0) } returns AppResult.Failure(
            com.softserveacademy.core.error.model.AppError.Network.NoConnection
        )

        // WHEN: Voice search is triggered
        viewModel.onEvent(HotelDetailsEvent.VoiceSearch(query))
        advanceUntilIdle()
        
        // THEN: Shows English network error message
        val expectedMsg = "No internet connection. Aira cannot respond."
        assertEquals(expectedMsg, viewModel.hotelDetailsState.value.aiErrorMessage)
        assertEquals(expectedMsg, viewModel.hotelDetailsState.value.lastVoiceQuery)
    }
}
