package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.UserProfile

/**
 * Represents the UI state for the Edit Profile screen.
 */
sealed interface EditProfileState {
    /**
     * Initial loading state while fetching the current profile.
     */
    data object Loading : EditProfileState

    /**
     * Success state when the profile has been loaded.
     */
    data class Success(val profile: UserProfile) : EditProfileState

    /**
     * Error state when loading or updating the profile fails.
     */
    data class Error(val message: String) : EditProfileState

    /**
     * Success state after the profile has been updated.
     */
    data object UpdateSuccess : EditProfileState
}
