package com.softserveacademy.feature.profile.presentation.state

import com.softserveacademy.core.domain.model.UserProfile

sealed interface ProfileState {
    data object Loading : ProfileState
    data class Success(val profile: UserProfile) : ProfileState
    data class Error(val message: String) : ProfileState
}
