package com.softserveacademy.home.data.repository

import com.softserveacademy.home.domain.repository.SearchFilter
import com.softserveacademy.home.domain.repository.SearchItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchRepositoryImplTest {

    private val repository = SearchRepositoryImpl()

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
        assertEquals(1, items!!.size)
        assertTrue(items[0] is SearchItem.HotelItem)
        assertEquals("San Alfonso del Mar", (items[0] as SearchItem.HotelItem).hotel.name)
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
    fun `given flights filter when search then returns only flights`() = runTest {
        val result = repository.search("", SearchFilter.FLIGHTS, null)

        assertTrue(result.isSuccess)
        val items = result.getOrNull()
        assertTrue(items!!.isNotEmpty())
        items.forEach { assertTrue(it is SearchItem.FlightItem) }
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
