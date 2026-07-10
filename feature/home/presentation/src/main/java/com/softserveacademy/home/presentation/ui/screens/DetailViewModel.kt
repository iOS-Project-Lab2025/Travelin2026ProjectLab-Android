package com.softserveacademy.home.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.repository.HotelRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the Hotel Detail screen.
 *
 * It handles fetching detailed information for a specific hotel from the [HotelRepo].
 *
 * @param hotelRepo The repository used to fetch hotel data.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val hotelRepo: HotelRepo
) : ViewModel() {

    private val _hotel = MutableStateFlow<Hotel?>(null)

    /**
     * Observable state containing the [Hotel] details.
     */
    val hotel = _hotel.asStateFlow()

    /**
     * Fetches the details of a hotel by its unique identifier.
     *
     * @param id The unique identifier of the hotel to load.
     */
    fun loadHotel(id: Int) {
        viewModelScope.launch {
            _hotel.value = hotelRepo.getHotelById(id)
        }
    }
}
