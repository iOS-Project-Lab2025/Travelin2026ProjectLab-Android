package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeRepositoryImplTest {

    private val hotelRepo = mockk<HotelRepo>()
    private val tourRepo = mockk<TourRepo>()
    private val repository = HomeRepositoryImpl(hotelRepo, tourRepo)

    @Test
    fun `given getUserProfile when called then returns mock user profile`() = runTest {
        val result = repository.getUserProfile()

        assertTrue(result.isSuccess)
        val profile = result.getOrNull()
        assertEquals("John Doe", profile?.name)
        assertEquals("Santiago, Chile", profile?.location)
    }

    @Test
    fun `given getUpcomingTrip when called then returns mock trip`() = runTest {
        val result = repository.getUpcomingTrip()

        assertTrue(result.isSuccess)
        val trip = result.getOrNull()
        assertEquals("trip_001", trip?.id)
        assertEquals("Bali", trip?.destination?.name)
    }

    @Test
    fun `given getJourneyTogether when called then returns tours from tourRepo`() = runTest {
        val tours = listOf(
            mockk<Tour>(),
            mockk<Tour>()
        )
        coEvery { tourRepo.getTours() } returns tours

        val result = repository.getJourneyTogether()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `given getRecommendedHotels when called then returns hotels from hotelRepo`() = runTest {
        val hotels = listOf(
            mockk<Hotel>(),
            mockk<Hotel>(),
            mockk<Hotel>()
        )
        coEvery { hotelRepo.getHotels() } returns hotels

        val result = repository.getRecommendedHotels()

        assertTrue(result.isSuccess)
        assertEquals(3, result.getOrNull()?.size)
    }
}
