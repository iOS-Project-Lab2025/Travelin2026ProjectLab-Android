package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.BookingContactInfo
import com.softserveacademy.core.domain.model.BookingGuests
import com.softserveacademy.core.domain.model.HotelBookingPrice
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.repository.HotelBookingRepository
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.core.error.model.AppResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetHotelBookingsUseCaseTest {

    private lateinit var useCase: GetHotelBookingsUseCase
    private val repository: HotelBookingRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetHotelBookingsUseCase(repository)
    }

    @Test
    fun `invoke should return success with list of bookings from repository`() = runTest {
        // Given
        val bookings = listOf(
            HotelBooking(
                bookingId = "BOOKING-TEST",
                userId = "user1",
                hotelId = "1",
                roomId = "13",
                checkIn = 1000L,
                checkOut = 2000L,
                guests = BookingGuests(2, 0, false),
                price = HotelBookingPrice(100.0, 200.0, 0.0, 0.0, 200.0, "USD"),
                status = BookingStatus.COMPLETED,
                confirmationCode = "HB-500",
                createdAt = 500L,
                contactInfo = BookingContactInfo("John", "Doe", "john@example.com", "+1", "123456789")
            )
        )
        coEvery { repository.getBookings() } returns AppResult.Success(bookings)

        // When
        val result = useCase()

        // Then
        assertTrue(result is AppResult.Success)
        assertEquals(bookings, (result as AppResult.Success).data)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        // Given
        val error = AppError.Network.NoConnection
        coEvery { repository.getBookings() } returns AppResult.Failure(error)

        // When
        val result = useCase()

        // Then
        assertTrue(result is AppResult.Failure)
        assertEquals(error, (result as AppResult.Failure).error)
    }
}
