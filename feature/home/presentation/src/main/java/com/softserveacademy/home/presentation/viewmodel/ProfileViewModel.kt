package com.softserveacademy.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.domain.usecase.SetThemeUseCase
import com.softserveacademy.feature.auth.common.domain.usecase.LogoutUseCase
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.presentation.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile screen within the home module.
 * Manages the profile data state and user interactions.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    /**
     * The current UI state of the profile screen.
     */
    var state by mutableStateOf<ProfileState>(ProfileState.Loading)
        private set

    /**
     * The current application theme preference.
     */
    var currentTheme by mutableStateOf(AppTheme.SYSTEM)
        private set

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        loadProfile()
        observeTheme()
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

    /**
     * Updates the application theme preference.
     */
    fun onThemeSelected(theme: AppTheme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }

    private fun observeTheme() {
        viewModelScope.launch {
            getThemeUseCase().collectLatest {
                currentTheme = it
            }
        }
    }
}
