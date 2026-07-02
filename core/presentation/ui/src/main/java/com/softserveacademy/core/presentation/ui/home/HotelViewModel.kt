package com.softserveacademy.core.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.HotelRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing the state of a hotel card.
 *
 * @param hotelRepo The repository responsible for fetching hotel data.
 * @property _uiHotelCardState The mutable state flow representing the state of the hotel card.
 *
 * @see UIHotelCardState
 */
@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepo: HotelRepo
) : ViewModel() {
    private val _uiHotelCardState = MutableStateFlow(UIHotelCardState.IsLoading() as UIHotelCardState)
    val uiHotelCardState = _uiHotelCardState.asStateFlow()

    fun getHotel(id: Int?) {
        viewModelScope.launch {
            _uiHotelCardState.update{
                UIHotelCardState.IsLoading(true)
            }
            try {
                val hotel = hotelRepo.getHotelById(id)
                _uiHotelCardState.update {
                    UIHotelCardState.Data(hotel)
                }
            } catch (e: Exception) {
                _uiHotelCardState.update {
                    UIHotelCardState.Error(e.message)
                }
            }
        }
    }
}
