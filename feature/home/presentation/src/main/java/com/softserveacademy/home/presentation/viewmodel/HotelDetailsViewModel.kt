package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.model.TravelItemType
import com.softserveacademy.core.domain.repository.HotelRepo
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.error.extension.map
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.home.domain.usecases.GetHotelDetailUseCase
import com.softserveacademy.home.presentation.events.HotelDetailsEventEffect
import com.softserveacademy.home.presentation.events.HotelDetailsEvent
import com.softserveacademy.home.presentation.state.HotelDetailState
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
    private val hotelRepo: HotelRepo,
    private val tourRepo: TourRepo,
    getThemeUseCase: GetThemeUseCase
    private val savedStateHandle: SavedStateHandle,
    private val getHotelDetailUseCase: GetHotelDetailUseCase
) : ViewModel() {

    private val _hotelDetailState = MutableStateFlow(
        savedStateHandle.get<HotelDetailState>(HOTEL_DETAILS_STATE) ?: HotelDetailState()
    )
    val hotelDetailState: StateFlow<HotelDetailState> = _hotelDetailState.asStateFlow()

    fun getHotelDetail(id: String, type: TravelItemType = TravelItemType.HOTEL) {
        viewModelScope.launch {
            _hotelDetailState.update{
                HotelDetailState.IsLoading(true)
            }
            delay(1000)
            
            val result = when (type) {
                TravelItemType.HOTEL -> hotelRepo.getHotelById(id.toInt())
                TravelItemType.TOUR -> tourRepo.getTourById(id).map { tour ->
                    HotelDetails(
                        id = tour.id.hashCode(),
                        minimumPrice = tour.price.toInt(),
                        imageList = listOf(tour.imageUrl),
                        name = tour.title,
                        description = tour.description,
                        rating = tour.rating.toDouble(),
                        address = tour.location,
                        numberOfReviews = (tour.rating * 10).toInt(),
                        includedItems = emptyList(),
                        latitude = 0.0,
                        longitude = 0.0
                    )
                }
            }

            result.onSuccess { hotelDetails ->
                    _hotelDetailState.update {
                        HotelDetailState.Data(hotelDetails)
                    }
    private val _effect = MutableSharedFlow<HotelDetailsEventEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: HotelDetailsEvent) {
        when (event) {
            is HotelDetailsEvent.Load -> {
                val currentState = _hotelDetailState.value
                // Only load if no data is present OR if the hotel ID has changed
                if (currentState.hotelDetails == null || currentState.hotelDetails.id != event.hotelId) {
                    loadHotelDetail(event.hotelId)
                }
            }
            HotelDetailsEvent.NavigateBack -> {
                sendEffect(HotelDetailsEventEffect.NavigateBack)
            }
            HotelDetailsEvent.Share -> {
                _hotelDetailState.value.hotelDetails?.let {
                    sendEffect(HotelDetailsEventEffect.ShareHotel(it))
                }
            }
            HotelDetailsEvent.ToggleFavorite -> {
                updateState { it.copy(isFavorite = !it.isFavorite) }
            }
            HotelDetailsEvent.BookNow -> {
                _hotelDetailState.value.hotelDetails?.let {
                    sendEffect(HotelDetailsEventEffect.NavigateToBooking(it.id))
                }
            }
            HotelDetailsEvent.ViewGallery -> {
                _hotelDetailState.value.hotelDetails?.let {
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

    private fun updateState(update: (HotelDetailState) -> HotelDetailState) {
        _hotelDetailState.update(update)
        savedStateHandle[HOTEL_DETAILS_STATE] = _hotelDetailState.value
    }

    private fun loadHotelDetail(id: Int) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            getHotelDetailUseCase(id)
                .onSuccess { details ->
                    updateState { it.copy(isLoading = false, hotelDetails = details) }
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
