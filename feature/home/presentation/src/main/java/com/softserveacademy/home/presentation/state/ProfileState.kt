package com.softserveacademy.home.presentation.state

import com.softserveacademy.core.domain.model.UserProfile

/**
 * Represents the different UI states of the profile screen.
 */
sealed interface ProfileState {
    /**
     * State representing the profile data is being fetched.
     */
    data object Loading : ProfileState

    /**
     * State representing the profile data was successfully fetched.
     * @property profile The user's profile information.
     */
    data class Success(val profile: UserProfile) : ProfileState

    /**
     * State representing an error occurred while fetching profile data.
     * @property message The error message to display.
     */
    data class Error(val message: String) : ProfileState
}
