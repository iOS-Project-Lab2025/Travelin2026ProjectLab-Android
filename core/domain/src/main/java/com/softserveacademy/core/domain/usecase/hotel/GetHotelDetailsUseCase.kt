package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving hotel details.
 */
class GetHotelDetailsUseCase @Inject constructor(
    private val repository: HotelRepo
) {
    /**
     * Executes the hotel detail retrieval.
     * @param id The unique identifier of the hotel.
     * @return AppResult containing [Hotel] on success.
     */
    suspend operator fun invoke(id: String): AppResult<Hotel> {
        return repository.getHotelById(id)
    }
}
