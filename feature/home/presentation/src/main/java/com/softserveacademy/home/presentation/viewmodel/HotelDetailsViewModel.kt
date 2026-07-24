package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.home.presentation.state.HotelDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val hotelRepo: HotelRepo,
    getThemeUseCase: GetThemeUseCase
) : ViewModel() {
    private val _hotelDetailState = MutableStateFlow(HotelDetailState.IsLoading() as HotelDetailState)
    val hotelDetailState = _hotelDetailState.asStateFlow()

    val appTheme = getThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppTheme.SYSTEM)

    fun getHotelDetail(id: Int) {
        viewModelScope.launch {
            _hotelDetailState.update{
                HotelDetailState.IsLoading(true)
            }
            try {

                delay(5000)
                val hotelDetails = hotelRepo.getHotelById(id)
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
