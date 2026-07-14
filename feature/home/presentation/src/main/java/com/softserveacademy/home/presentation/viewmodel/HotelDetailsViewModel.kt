package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.home.domain.repository.HomeRepository
import com.softserveacademy.home.presentation.state.HotelDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _hotelDetailState = MutableStateFlow(HotelDetailState.IsLoading() as HotelDetailState)
    val hotelDetailState = _hotelDetailState.asStateFlow()

    fun getHotelDetail(id: Int) {
        viewModelScope.launch {
            _hotelDetailState.update{
                HotelDetailState.IsLoading(true)
            }
            try {
                val hotelDetails = homeRepository.getHotelDetailsById(id)
                _hotelDetailState.update {
                    HotelDetailState.Data(hotelDetails)
                }
            } catch (e: Exception) {
                _hotelDetailState.update {
                    HotelDetailState.Error(e.message)
                }
            }
        }
    }
}