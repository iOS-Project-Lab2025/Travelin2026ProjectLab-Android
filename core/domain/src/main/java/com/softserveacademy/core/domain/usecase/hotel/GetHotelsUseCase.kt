package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving all available hotels.
 */
class GetHotelsUseCase @Inject constructor(
    private val repository: HotelRepo,
) {
    suspend operator fun invoke(): AppResult<List<Hotel>> {
        return repository.getHotels()
    }
}
