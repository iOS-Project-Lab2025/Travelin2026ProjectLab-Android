package com.softserveacademy.home.data.repository

import android.util.Log
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.RatePerParticipant
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.PoiRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import com.softserveacademy.home.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.hours

class SearchRepositoryImplTest {

    private val hotelRepo: HotelRepo = mockk()
    private val tourRepo: TourRepo = mockk()
    private val poiRepo: PoiRepo = mockk()
    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        // Mock static Android Log to avoid "Method d in android.util.Log not mocked"
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        repository = SearchRepositoryImpl(hotelRepo, tourRepo, poiRepo)

        // Stubs for API responses
        coEvery { hotelRepo.getHotels() } returns AppResult.Success(listOf(
            Hotel(
                id = "1",
                name = "San Alfonso del Mar",
                address = "Algarrobo, Chile",
                starCategory = 5,
                reviewRating = 4.8,
                numberOfReviews = 200,
                imageList = listOf("https://picsum.photos/id/164/400/300")
            ),
        ))

        coEvery { tourRepo.getTours() } returns AppResult.Success(listOf(
            Tour(
                id = "t1",
                title = "Cajón del Maipo Trekking",
                description = "Full day trekking in the Andes",
                location = "San José de Maipo",
                imageList = listOf("https://picsum.photos/id/10/400/300"),
                duration = 8.hours,
                rates = RatePerParticipant(adults = 45.0),
                rating = 4.9
            )
        ))

        // Stub for the updated 4-parameter method in PoiRepo
        coEvery { poiRepo.getNearbyPlaces(any(), any(), any(), any()) } returns AppResult.Success(emptyList())
    }

    @Test
    fun `given empty query and all filter when search then returns all items`() = runTest {
        val result = repository.search("", SearchFilter.ALL, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isNotEmpty())
    }

    @Test
    fun `given query matching hotel when search then returns matching hotels`() = runTest {
        val result = repository.search("San Alfonso", SearchFilter.ALL, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.any { it is SearchItem.HotelItem && it.hotel.name == "San Alfonso del Mar" })
    }

    @Test
    fun `given hotels filter when search then returns only hotels`() = runTest {
        val result = repository.search("", SearchFilter.HOTELS, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isNotEmpty())
        items.forEach { assertTrue(it is SearchItem.HotelItem) }
    }

    @Test
    fun `given tours filter when search then returns only tours`() = runTest {
        val result = repository.search("", SearchFilter.TOURS, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isNotEmpty())
        items.forEach { assertTrue(it is SearchItem.TourItem) }
    }

    @Test
    fun `given destinations filter when search then returns only destinations`() = runTest {
        val result = repository.search("", SearchFilter.DESTINATIONS, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isNotEmpty())
        items.forEach { assertTrue(it is SearchItem.DestinationItem) }
    }

    @Test
    fun `given query matching nothing when search then returns empty list`() = runTest {
        val result = repository.search("NonExistentItemXYZ", SearchFilter.ALL, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isEmpty())
    }
}