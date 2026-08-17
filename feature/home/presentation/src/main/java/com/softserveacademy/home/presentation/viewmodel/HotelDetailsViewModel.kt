package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.usecase.GetAreaDescriptionUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyPlacesUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyRestaurantsUseCase
import com.softserveacademy.core.domain.usecase.GetNearbyTransportUseCase
import com.softserveacademy.core.domain.usecase.hotel.GetHotelDetailsUseCase
import com.softserveacademy.core.error.extension.onFailure
import com.softserveacademy.core.error.extension.onSuccess
import com.softserveacademy.core.error.model.AppError
import com.softserveacademy.feature.favorites.common.domain.model.FavoriteItem
import com.softserveacademy.feature.favorites.common.domain.repository.FavoritesRepository
import com.softserveacademy.feature.favorites.common.domain.usecase.ToggleFavoriteUseCase
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
import kotlin.onFailure
import kotlin.onSuccess

@HiltViewModel
class HotelDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHotelDetailsUseCase: GetHotelDetailsUseCase,
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    private val getAreaDescriptionUseCase: GetAreaDescriptionUseCase,
    private val getNearbyTransportUseCase: GetNearbyTransportUseCase,
    private val getNearbyRestaurantsUseCase: GetNearbyRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoritesRepository: FavoritesRepository
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
                _hotelDetailsState.value.hotel?.let { hotel ->
                    viewModelScope.launch {
                        val favoriteItem = FavoriteItem(
                            id = hotel.id,
                            title = hotel.name,
                            location = hotel.address,
                            rating = hotel.reviewRating,
                            type = "HOTEL",
                            price = hotel.rooms.firstOrNull()?.pricePerNight ?: 0,
                            imageUrl = hotel.imageList.firstOrNull() ?: "",
                            addedAt = System.currentTimeMillis()
                        )
                        toggleFavoriteUseCase(favoriteItem)
                        updateState { it.copy(isFavorite = !it.isFavorite) }
                    }
                }
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
            
            // Check if it's favorite
            val isFavorite = favoritesRepository.isFavorite(id)
            updateState { it.copy(isFavorite = isFavorite) }

            getHotelDetailsUseCase(id)
                .onSuccess { hotel ->
                    updateState { it.copy(isLoading = false, hotel = hotel) }
                    loadNearbyPlaces(hotel.latitude, hotel.longitude)
                }
                .onFailure { error ->
                    val message = when (error) {
                        is AppError.Unknown -> error.throwable.message
                        else -> "Failed to load hotel details"
                    }
                    updateState { it.copy(isLoading = false, errorMessage = message) }
                }

        }
    }

    private fun loadNearbyPlaces(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            getNearbyPlacesUseCase(latitude, longitude)
                .onSuccess { pois ->
                    updateState { currentState ->
                        currentState.copy(
                            hotel = currentState.hotel?.copy(nearbyPlaces = pois)
                        )
                    }
                }
                .onFailure { error ->
                    android.util.Log.e("HotelDetailsViewModel", "Error loading nearby places", error)
                }
        }


        viewModelScope.launch {
            getAreaDescriptionUseCase(latitude, longitude)
                .onSuccess { description ->
                    updateState { it.copy(areaDescription = description ?: "This area is known for its beautiful scenery and historical landmarks.") }
                }
                .onFailure { error ->
                    android.util.Log.e("HotelDetailsViewModel", "Error loading area description", error)
                }
        }


        viewModelScope.launch {
            getNearbyTransportUseCase(latitude, longitude)
                .onSuccess { transport ->
                    updateState { it.copy(nearbyTransport = transport) }
                }
        }


        viewModelScope.launch {
            getNearbyRestaurantsUseCase(latitude, longitude)
                .onSuccess { restaurants ->
                    updateState { it.copy(nearbyRestaurants = restaurants) }
                }
        }
    }

    companion object {
        private const val HOTEL_DETAILS_STATE = "hotel_detail_state"
    }
}