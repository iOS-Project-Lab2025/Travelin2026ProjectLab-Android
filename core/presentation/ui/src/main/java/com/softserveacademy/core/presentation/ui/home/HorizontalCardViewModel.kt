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
 * ViewModel class responsible for managing the state of the horizontal card.
 *
 * @param hotelRepo The repository responsible for fetching hotel data.
 * @property _horizontalCardState The mutable state flow representing the state of the hotel card.
 *
 * @see HorizontalCardState
 */
@HiltViewModel
class HorizontalCardViewModel @Inject constructor(
    private val hotelRepo: HotelRepo
) : ViewModel() {
    private val _horizontalCardState = MutableStateFlow(HorizontalCardState.IsLoading() as HorizontalCardState)
    val horizontalCardState = _horizontalCardState.asStateFlow()

    fun getHotel(id: Int?) {
        viewModelScope.launch {
            _horizontalCardState.update{
                HorizontalCardState.IsLoading(true)
            }
            try {
                val hotelDetails = hotelRepo.getHotelById(id ?: 1)
                _horizontalCardState.update {
                    HorizontalCardState.Data(hotelDetails.toSummary())
                }
            } catch (e: Exception) {
                _horizontalCardState.update {
                    HorizontalCardState.Error(e.message)
                }
            }
        }
    }
}
