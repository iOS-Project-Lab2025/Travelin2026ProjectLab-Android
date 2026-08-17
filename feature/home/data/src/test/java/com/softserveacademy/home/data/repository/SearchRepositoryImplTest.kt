package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {

    private val hotelRepo: HotelRepo = mockk()
    private val tourRepo: TourRepo = mockk()
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setup() {
        repository = SearchRepositoryImpl(hotelRepo, tourRepo)
        
        coEvery { hotelRepo.getHotels() } returns AppResult.Success(listOf(
            Hotel("1", "San Alfonso del Mar", "Algarrobo, Chile", 5, 4.8, 200, listOf("https://picsum.photos/id/164/400/300")),
        ))
        
        coEvery { tourRepo.getTours() } returns AppResult.Success(listOf(
            Tour("t1", "Cajón del Maipo Trekking", "Full day trekking in the Andes", "San José de Maipo", listOf("https://picsum.photos/id/10/400/300"), kotlin.time.Duration.parse("8h"), 45.0, 4.8, com.softserveacademy.core.domain.model.TourCategory.ADVENTURE)
        ))
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
