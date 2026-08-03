package com.softserveacademy.home.domain.usecases

import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case to retrieve the detailed information of a specific hotel.
 */
class GetHotelDetailUseCase @Inject constructor(
    private val repository: HotelRepo
) {
    /**
     * Executes the hotel detail retrieval.
     * @param id The unique identifier of the hotel.
     * @return Result containing [HotelDetails] on success.
     */
    suspend operator fun invoke(id: Int): Result<HotelDetails> {
        return when (val result = repository.getHotelById(id)) {
            is AppResult.Success -> Result.success(result.data)
            is AppResult.Failure -> Result.failure(Exception("Failed to load hotel details"))
        }
    }
}
