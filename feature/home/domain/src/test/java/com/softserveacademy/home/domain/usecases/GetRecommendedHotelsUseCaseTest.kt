package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetRecommendedHotelsUseCaseTest {

    private val repository = mockk<HomeRepository>()
    private val useCase = GetRecommendedHotelsUseCase(repository)

    @Test
    fun `given success when invoke then returns hotel list`() = runTest {
        val hotels = listOf(
            Hotel(name = "Hotel A", address = "Addr A", imageList = emptyList()),
            Hotel(name = "Hotel B", address = "Addr B", imageList = emptyList())
        )
        coEvery { repository.getRecommendedHotels() } returns Result.success(hotels)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(hotels, result.getOrNull())
    }

    @Test
    fun `given empty list when invoke then returns success with empty list`() = runTest {
        coEvery { repository.getRecommendedHotels() } returns Result.success(emptyList())

        val result = useCase()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        coEvery { repository.getRecommendedHotels() } returns Result.failure(Exception("Error"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
