package com.softserveacademy.feature.favorites.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.favorites.common.domain.usecase.GetFavoritesUseCase
import com.softserveacademy.feature.favorites.common.domain.usecase.ToggleFavoriteUseCase
import com.softserveacademy.feature.favorites.common.presentation.events.TravelFavoritesEvent
import com.softserveacademy.feature.favorites.common.presentation.states.TravelFavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the UI state and handling user events
 * for the Favorites feature following the MVI architecture.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelFavoritesState())
    val uiState: StateFlow<TravelFavoritesState> = _uiState.asStateFlow()

    init {
        observeFavorites()
        checkAuthStatus()
    }

    /**
     * Entry point for processing user actions triggered from the UI.
     */
    fun onEvent(event: TravelFavoritesEvent) {
        when (event) {
            is TravelFavoritesEvent.OnCategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.type) }
            }

            is TravelFavoritesEvent.OnRemoveFavorite -> {
                viewModelScope.launch {
                    toggleFavoriteUseCase(event.item)
                }
            }

            is TravelFavoritesEvent.OnFavoriteItemClick -> {
                // Handled in UI via navigation callbacks
            }

            TravelFavoritesEvent.OnSignInClick -> {
                // Handled in UI via navigation callbacks
            }

            TravelFavoritesEvent.OnGoBackClick -> {
                // Handled in UI via navigation callbacks
            }

            TravelFavoritesEvent.OnRefresh -> {
                observeFavorites()
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getFavoritesUseCase().collect { favoritesList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        allFavorites = favoritesList,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun checkAuthStatus() {
        // TODO: Connect with AuthRepository or CheckUserLoggedInUseCase when available
        _uiState.update { it.copy(isAuthenticated = true) }
    }
}