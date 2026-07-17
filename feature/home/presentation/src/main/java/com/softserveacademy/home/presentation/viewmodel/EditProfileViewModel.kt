package com.softserveacademy.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.usecases.GetProfileUseCase
import com.softserveacademy.home.domain.usecases.UpdateProfileUseCase
import com.softserveacademy.home.presentation.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Edit Profile screen.
 * Manages the profile editing state and validation.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    var state by mutableStateOf<EditProfileState>(EditProfileState.Loading)
        private set

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var countryCode by mutableStateOf("+855")
    var phone by mutableStateOf("")
    var age by mutableStateOf("")
    var location by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    private var initialProfile: UserProfile? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            state = EditProfileState.Loading
            getProfileUseCase().onSuccess { profile ->
                initialProfile = profile
                firstName = profile.firstName
                lastName = profile.lastName
                
                // Assuming phone is stored as "code number"
                val phoneParts = profile.phone?.split(" ")
                if (phoneParts != null && phoneParts.size >= 2) {
                    countryCode = phoneParts[0]
                    phone = phoneParts.subList(1, phoneParts.size).joinToString(" ")
                } else {
                    phone = profile.phone ?: ""
                }
                
                age = profile.age?.toString() ?: ""
                location = profile.location ?: ""
                state = EditProfileState.Success(profile)
            }.onFailure {
                state = EditProfileState.Error(it.message ?: "Failed to load profile")
            }
        }
    }

    /**
     * Checks if any changes have been made to the profile.
     */
    fun hasChanges(): Boolean {
        val currentProfile = initialProfile ?: return false
        val currentPhone = "$countryCode $phone".trim()
        val initialPhone = currentProfile.phone ?: ""
        
        return firstName != currentProfile.firstName ||
                lastName != currentProfile.lastName ||
                currentPhone != initialPhone ||
                age != (currentProfile.age?.toString() ?: "") ||
                location != (currentProfile.location ?: "") ||
                password.isNotEmpty()
    }

    /**
     * Validates and saves the profile changes.
     */
    fun onSaveChanges() {
        val ageInt = age.toIntOrNull()
        if (ageInt == null) {
            state = EditProfileState.Error("Invalid age")
            return
        }

        if (password.isNotEmpty() && password != confirmPassword) {
            state = EditProfileState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            val updatedProfile = initialProfile?.copy(
                firstName = firstName,
                lastName = lastName,
                phone = "$countryCode $phone".trim(),
                age = ageInt,
                location = location
            ) ?: return@launch

            state = EditProfileState.Loading
            updateProfileUseCase(updatedProfile, password.ifEmpty { null }).onSuccess {
                state = EditProfileState.UpdateSuccess
            }.onFailure {
                state = EditProfileState.Error(it.message ?: "Failed to update profile")
            }
        }
    }
    
    fun clearError() {
        val profile = initialProfile
        if (profile != null) {
            state = EditProfileState.Success(profile)
        }
    }
}
