package com.softserveacademy.feature.profile.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.profile.domain.usecases.GetProfileUseCase
import com.softserveacademy.feature.profile.presentation.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    var state by mutableStateOf<ProfileState>(ProfileState.Loading)
        private set

    init {
        loadProfile()
    }

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

    fun onLogoutClick() {
        // Mocked for now
    }

    fun onEditProfileClick() {
        // Mocked for now
    }
}
