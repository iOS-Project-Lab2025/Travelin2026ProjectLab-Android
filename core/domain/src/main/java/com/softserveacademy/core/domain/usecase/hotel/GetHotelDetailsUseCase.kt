package com.softserveacademy.core.domain.usecase.hotel

import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.error.model.AppResult
import javax.inject.Inject

/**
 * Use case for retrieving hotel details.
 */
class GetHotelDetailsUseCase @Inject constructor(
    private val repository: HotelRepo
) {
    suspend operator fun invoke(id: String): AppResult<HotelDetails> {
        return repository.getHotelById(id)
    }
}