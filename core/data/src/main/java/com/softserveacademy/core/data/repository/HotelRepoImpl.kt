package com.softserveacademy.core.data.repository

import com.softserveacademy.core.data.api.HotelApiService
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelRoom
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.mapper.ExceptionMapper
import com.softserveacademy.core.error.model.AppResult
import com.softserveacademy.core.error.util.safeCall
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the HotelRepo interface that fetches data from a real API.
 */
@Singleton
class HotelRepoImpl @Inject constructor(
    private val hotelApiService: HotelApiService,
    private val mapper: ExceptionMapper
) : HotelRepo {

    override suspend fun getHotelById(id: String): AppResult<Hotel> = safeCall(mapper) {
        hotelApiService.getHotelById(id)
    }

    override suspend fun getHotels(): AppResult<List<Hotel>> = safeCall(mapper) {
        hotelApiService.getHotels()
    }

    override suspend fun getHotelRooms(hotelId: String): AppResult<List<HotelRoom>> = safeCall(mapper) {
        val hotel = hotelApiService.getHotelById(hotelId)
        hotel.rooms
    }
}
