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
     * @return Result containing [Hotel] on success.
     */
    suspend operator fun invoke(id: String): Result<Hotel> {
        return when (val result = repository.getHotelById(id)) {
            is AppResult.Success -> Result.success(result.data)
            is AppResult.Failure -> Result.failure(Exception("Failed to load hotel details"))
        }
    }
}