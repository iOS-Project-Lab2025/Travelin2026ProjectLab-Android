package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.Tour
import com.softserveacademy.core.domain.model.TourCategory
import com.softserveacademy.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.hours
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetJourneyTogetherUseCaseTest {

    private val repository = mockk<HomeRepository>()
    private val useCase = GetJourneyTogetherUseCase(repository)

    @Test
    fun `given success when invoke then returns tour list`() = runTest {
        val tours = listOf(
            Tour(id = "t1", title = "Tour 1", description = "Desc", location = "Loc", imageList = listOf("url"), duration = 2.hours, price = 50.0, rating = 4.5, category = TourCategory.CULTURE)
        )
        coEvery { repository.getJourneyTogether() } returns Result.success(tours)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(tours, result.getOrNull())
    }

    @Test
    fun `given empty list when invoke then returns success with empty list`() = runTest {
        coEvery { repository.getJourneyTogether() } returns Result.success(emptyList())

        val result = useCase()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())
    }

    @Test
    fun `given error when invoke then returns failure`() = runTest {
        coEvery { repository.getJourneyTogether() } returns Result.failure(Exception("Error"))

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
