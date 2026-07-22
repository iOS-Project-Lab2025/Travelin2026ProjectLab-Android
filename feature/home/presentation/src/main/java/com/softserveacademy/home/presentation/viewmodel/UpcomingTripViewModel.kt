package com.softserveacademy.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.home.domain.usecases.GetUpcomingTripUseCase
import com.softserveacademy.home.presentation.model.toTripDetailUi
import com.softserveacademy.home.presentation.state.SectionState
import com.softserveacademy.home.presentation.state.UpcomingTripState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingTripViewModel @Inject constructor(
    private val getUpcomingTripUseCase: GetUpcomingTripUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UpcomingTripState())
    val state = _state.asStateFlow()

    fun loadTrip() {
        viewModelScope.launch {
            _state.update { it.copy(trip = SectionState.Loading) }
            getUpcomingTripUseCase()
                .onSuccess { trip ->
                    if (trip != null) {
                        _state.update { it.copy(trip = SectionState.Success(trip)) }
                    } else {
                        _state.update { it.copy(trip = SectionState.Empty) }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(trip = SectionState.Error(error.message ?: "Failed to load trip")) }
                }
        }
    }
}
