package com.softserveacademy.feature.favorites.tours.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.favorites.tours.domain.model.FavoriteTour
import com.softserveacademy.feature.favorites.tours.domain.usecase.GetFavoriteToursUseCase
import com.softserveacademy.feature.favorites.tours.presentation.events.FavoritesToursEffect
import com.softserveacademy.feature.favorites.tours.presentation.events.FavoritesToursEvent
import com.softserveacademy.feature.favorites.tours.presentation.states.FavoritesToursState
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
class FavoritesToursViewModel @Inject constructor(
    private val getFavoriteToursUseCase: GetFavoriteToursUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesToursState())
    val uiState: StateFlow<FavoritesToursState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<FavoritesToursEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadFavoriteTours()
    }

    fun onEvent(event: FavoritesToursEvent) {
        when (event) {
            is FavoritesToursEvent.OnFilterSelected -> filterTours(event.filter)
            is FavoritesToursEvent.OnTourClick -> {
                viewModelScope.launch {
                    _effect.emit(FavoritesToursEffect.NavigateToTourDetail(event.tour.id))
                }
            }
            FavoritesToursEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effect.emit(FavoritesToursEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadFavoriteTours() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getFavoriteToursUseCase().collect { list ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tours = list,
                        filteredTours = applyFilter(list, it.selectedFilter)
                    )
                }
            }
        }
    }

    private fun filterTours(filter: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilter = filter,
                filteredTours = applyFilter(currentState.tours, filter)
            )
        }
    }

    private fun applyFilter(list: List<FavoriteTour>, filter: String): List<FavoriteTour> {
        return when (filter) {
            "Adventure" -> list.filter { it.category == "Adventure" }
            "Culture" -> list.filter { it.category == "Culture" }
            else -> list
        }
    }
}
