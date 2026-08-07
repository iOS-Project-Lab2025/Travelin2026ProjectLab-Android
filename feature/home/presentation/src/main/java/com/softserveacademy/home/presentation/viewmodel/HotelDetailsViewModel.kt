package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.home.presentation.events.HotelDetailsEventEffect
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.home.presentation.state.HotelDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHotelDetailsUseCase: GetHotelDetailsUseCase,
) : ViewModel() {

    private val _hotelDetailsState = MutableStateFlow(
        savedStateHandle.get<HotelDetailsState>(HOTEL_DETAILS_STATE) ?: HotelDetailsState()
    )
    val hotelDetailsState: StateFlow<HotelDetailsState> = _hotelDetailsState.asStateFlow()

    private val _effect = MutableSharedFlow<HotelDetailsEventEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: HotelDetailsEvent) {
        when (event) {
            is HotelDetailsEvent.Load -> {
                val currentState = _hotelDetailsState.value
                if (currentState.hotel == null || currentState.hotel.id != event.id) {
                    loadDetail(event.id)
                }
            }
            HotelDetailsEvent.NavigateBack -> {
                sendEffect(HotelDetailsEventEffect.NavigateBack)
            }
            HotelDetailsEvent.Share -> {
                _hotelDetailsState.value.hotel?.let {
                    sendEffect(HotelDetailsEventEffect.ShareHotel(it))
                }
            }
            HotelDetailsEvent.ToggleFavorite -> {
                updateState { it.copy(isFavorite = !it.isFavorite) }
            }
            HotelDetailsEvent.BookNow -> {
                _hotelDetailsState.value.hotel?.let {
                    sendEffect(HotelDetailsEventEffect.NavigateToBooking(it.id))
                }
            }
            HotelDetailsEvent.ViewGallery -> {
                _hotelDetailsState.value.hotel?.let {
                    sendEffect(HotelDetailsEventEffect.NavigateToGallery(it.imageList))
                }
            }
            HotelDetailsEvent.ViewFullMap -> {
                updateState { it.copy(showFullMap = true) }
            }
            HotelDetailsEvent.DismissMap -> {
                updateState { it.copy(showFullMap = false) }
            }
            HotelDetailsEvent.ToggleDescription -> {
                updateState { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
            }
            HotelDetailsEvent.ViewAllAmenities -> {
                updateState { it.copy(showAmenitiesDialog = true) }
            }
            HotelDetailsEvent.DismissAmenities -> {
                updateState { it.copy(showAmenitiesDialog = false) }
            }
        }
    }

    private fun sendEffect(effect: HotelDetailsEventEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun updateState(update: (HotelDetailsState) -> HotelDetailsState) {
        _hotelDetailsState.update(update)
        savedStateHandle[HOTEL_DETAILS_STATE] = _hotelDetailsState.value
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            getHotelDetailsUseCase(id)
                .onSuccess { hotel ->
                    updateState { it.copy(isLoading = false, hotel = hotel) }
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false, errorMessage = error.message) }
                }

        }
    }

    companion object {
        private const val HOTEL_DETAILS_STATE = "hotel_detail_state"
    }
}