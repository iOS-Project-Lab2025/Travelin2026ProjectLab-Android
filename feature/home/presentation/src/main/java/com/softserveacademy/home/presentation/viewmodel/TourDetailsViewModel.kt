package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.repository.TourRepo
import com.softserveacademy.home.presentation.events.TourDetailsEvent
import com.softserveacademy.home.presentation.events.TourDetailsEventEffect
import com.softserveacademy.home.presentation.state.TourDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.softserveacademy.core.error.model.AppResult

@HiltViewModel
class TourDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tourRepo: TourRepo
) : ViewModel() {

    private val _tourDetailsState = MutableStateFlow(
        savedStateHandle.get<TourDetailsState>(TOUR_DETAILS_STATE) ?: TourDetailsState()
    )
    val tourDetailsState: StateFlow<TourDetailsState> = _tourDetailsState.asStateFlow()

    private val _effect = MutableSharedFlow<TourDetailsEventEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: TourDetailsEvent) {
        when (event) {
            is TourDetailsEvent.Load -> {
                val currentState = _tourDetailsState.value
                if (currentState.tourDetails == null || currentState.tourDetails.id != event.id) {
                    loadDetail(event.id)
                }
            }
            TourDetailsEvent.NavigateBack -> {
                sendEffect(TourDetailsEventEffect.NavigateBack)
            }
            TourDetailsEvent.Share -> {
                _tourDetailsState.value.tourDetails?.let {
                    sendEffect(TourDetailsEventEffect.ShareTour(it))
                }
            }
            TourDetailsEvent.ToggleFavorite -> {
                updateState { it.copy(isFavorite = !it.isFavorite) }
            }

            TourDetailsEvent.ViewGallery -> {
                _tourDetailsState.value.tourDetails?.let {
                    sendEffect(TourDetailsEventEffect.NavigateToGallery(it.imageList))
                }
            }
            TourDetailsEvent.ViewFullMap -> {
                updateState { it.copy(showFullMap = true) }
            }
            TourDetailsEvent.DismissMap -> {
                updateState { it.copy(showFullMap = false) }
            }
            TourDetailsEvent.ToggleDescription -> {
                updateState { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
            }
            TourDetailsEvent.ViewAllAmenities -> {
                updateState { it.copy(showAllAmenities = true) }
            }
            TourDetailsEvent.DismissAmenities -> {
                updateState { it.copy(showAllAmenities = false) }
            }
            TourDetailsEvent.Book -> {
                _tourDetailsState.value.tourDetails?.let {
                    sendEffect(TourDetailsEventEffect.NavigateToBooking(it.id))
                }
            }
        }
    }

    private fun sendEffect(effect: TourDetailsEventEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun updateState(update: (TourDetailsState) -> TourDetailsState) {
        _tourDetailsState.update(update)
        savedStateHandle[TOUR_DETAILS_STATE] = _tourDetailsState.value
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            when (val result = tourRepo.getTourById(id)) {
                is AppResult.Success -> {
                    updateState { it.copy(isLoading = false, tourDetails = result.data) }
                }
                is AppResult.Failure -> {
                    updateState { it.copy(isLoading = false, errorMessage = "Failed to load tour details") }
                }
            }

        }
    }

    companion object {
        private const val TOUR_DETAILS_STATE = "tour_detail_state"
    }
}