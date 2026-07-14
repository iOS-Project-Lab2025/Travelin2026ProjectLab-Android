package com.softserveacademy.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.auth.common.domain.usecase.LogoutUseCase
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.presentation.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile screen within the home module.
 * Manages the profile data state and user interactions.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    /**
     * The current UI state of the profile screen.
     */
    var state by mutableStateOf<ProfileState>(ProfileState.Loading)
        private set

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        loadProfile()
    }

    /**
     * Loads the user profile data.
     */
    fun loadProfile() {
        viewModelScope.launch {
            state = ProfileState.Loading
            getProfileUseCase().onSuccess {
                state = ProfileState.Success(it)
            }.onFailure {
                state = ProfileState.Error(it.message ?: "Unknown error")
            }
        }
    }

    /**
     * Handles the logout action.
     */
    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _logoutEvent.emit(Unit)
            }
        }
    }
    
    /**
     * Handles the edit profile action.
     */
    fun onEditProfileClick() {
        // Mocked for now
    }
}
