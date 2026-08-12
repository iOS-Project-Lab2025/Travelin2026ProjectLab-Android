package com.softserveacademy.feature.favorites.hotels.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.favorites.hotels.domain.model.FavoriteHotel
import com.softserveacademy.feature.favorites.hotels.domain.usecase.GetFavoriteHotelsUseCase
import com.softserveacademy.feature.favorites.hotels.presentation.events.FavoriteHotelsEvent
import com.softserveacademy.feature.favorites.hotels.presentation.states.FavoriteHotelsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteHotelsViewModel @Inject constructor(
    private val getFavoriteHotelsUseCase: GetFavoriteHotelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteHotelsState())
    val uiState: StateFlow<FavoriteHotelsState> = _uiState.asStateFlow()

    init {
        loadFavoriteHotels()
    }

    fun onEvent(event: FavoriteHotelsEvent) {
        when (event) {
            is FavoriteHotelsEvent.OnFilterSelected -> filterHotels(event.filter)
            is FavoriteHotelsEvent.OnHotelClick -> { /* Lógica de navegación */ }
            FavoriteHotelsEvent.OnBackClick -> { /* Lógica de retorno */ }
        }
    }

    private fun loadFavoriteHotels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getFavoriteHotelsUseCase().collect { list ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hotels = list,
                        filteredHotels = applyFilter(list, it.selectedFilter)
                    )
                }
            }
        }
    }

    private fun filterHotels(filter: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                filteredHotels = applyFilter(currentState.hotels, filter)
            )
        }
    }

    private fun applyFilter(list: List<FavoriteHotel>, filter: String): List<FavoriteHotel> {
        return when (filter) {
            "Available" -> list.filter { it.isAvailable }
            "1 Bed" -> list.filter { it.roomType == "1 Bed" }
            "2 Beds" -> list.filter { it.roomType == "2 Beds" }
            else -> list
        }
    }
}
