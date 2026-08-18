package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.usecase.GetNearbyPlacesUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyTransportUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.core.error.model.AppError
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHotelDetailsUseCase: GetHotelDetailsUseCase,
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    private val getNearbyTransportUseCase: GetNearbyTransportUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase
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
            HotelDetailsEvent.ViewExploreArea -> {
                updateState { it.copy(showExploreArea = true) }
            }
            HotelDetailsEvent.DismissExploreArea -> {
                updateState { it.copy(showExploreArea = false) }
            }
            HotelDetailsEvent.RetryPois -> {
                _hotelDetailsState.value.hotel?.let {
                    loadNearbyPlaces(it.latitude, it.longitude)
                }
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
                    loadNearbyPlaces(hotel.latitude, hotel.longitude)
                }
                .onFailure { error ->
                    updateState { currentState ->
                        if (currentState.hotel != null) {
                            currentState.copy(isLoading = false)
                        } else {
                            val message = when (error) {
                                is AppError.Unknown -> error.throwable.message
                                else -> "Failed to load hotel details"
                            }
                            currentState.copy(isLoading = false, errorMessage = message)
                        }
                    }
                }

        }
    }

    private fun loadNearbyPlaces(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            updateState { it.copy(isPoiLoading = true, poiErrorMessage = null) }
            
            val placesDeferred = async {
                getNearbyPlacesUseCase(latitude, longitude, 10)
            }
            val transportDeferred = async {
                getNearbyTransportUseCase(latitude, longitude, 3)
            }
            val restaurantsDeferred = async {
                getNearbyRestaurantsUseCase(latitude, longitude, 3)
            }

            val placesResult = placesDeferred.await()
            val transportResult = transportDeferred.await()
            val restaurantsResult = restaurantsDeferred.await()

            var errorMessage: String? = null
            
            placesResult.onSuccess { pois ->
                updateState { currentState ->
                    currentState.copy(
                        hotel = currentState.hotel?.copy(nearbyPlaces = pois)
                    )
                }
            }.onFailure { error ->
                errorMessage = mapPoiError(error)
            }

            transportResult.onSuccess { transport ->
                updateState { it.copy(nearbyTransport = transport) }
            }.onFailure { error ->
                if (errorMessage == null) errorMessage = mapPoiError(error)
            }

            restaurantsResult.onSuccess { restaurants ->
                updateState { it.copy(nearbyRestaurants = restaurants) }
            }.onFailure { error ->
                if (errorMessage == null) errorMessage = mapPoiError(error)
            }

            updateState { it.copy(isPoiLoading = false, poiErrorMessage = errorMessage) }
        }
    }

    private fun mapPoiError(error: AppError): String {
        return when (error) {
            is AppError.Network.NoConnection -> "No internet connection. Please check your network."
            is AppError.Network.Timeout -> "Connection timed out. Please try again."
            else -> "Failed to load nearby places"
        }
    }

    companion object {
        private const val HOTEL_DETAILS_STATE = "hotel_detail_state"
    }
}