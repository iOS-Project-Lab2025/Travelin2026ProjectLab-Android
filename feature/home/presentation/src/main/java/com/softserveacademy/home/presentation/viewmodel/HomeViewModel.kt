package com.softserveacademy.home.presentation.viewmodel

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
 * ViewModel responsible for managing the state and business logic of the Home screen.
 *
 * It coordinates the loading of the primary hotel list displayed on the main dashboard.
 *
 * @param hotelRepo The repository used to fetch the collection of hotels.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hotelRepo: HotelRepo
) : ViewModel() {

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())

    /**
     * Observable state containing the list of [Hotel] objects to be displayed.
     */
    val hotels = _hotels.asStateFlow()

    init {
        loadHotels()
    }

    /**
     * Initiates the loading of all available hotels from the repository.
     */
    private fun loadHotels() {
        viewModelScope.launch {
            _hotels.value = hotelRepo.getHotels()
        }
    }
}
