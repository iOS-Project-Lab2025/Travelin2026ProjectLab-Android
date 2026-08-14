package com.softserveacademy.feature.auth.register.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.User
import com.softserveacademy.feature.auth.register.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var countryCode by mutableStateOf("+855")
    var phone by mutableStateOf("")
    var birthDate by mutableStateOf<Long?>(null)
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var termsAccepted by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun onRegisterClick() {
        if (!termsAccepted) {
            error = "You must accept the terms and conditions"
            return
        }

        val selectedBirthDate = birthDate
        if (selectedBirthDate == null) {
            error = "Please select your date of birth"
            return
        }

        val user = User(
            firstName = firstName,
            lastName = lastName,
            phone = "$countryCode $phone",
            birthDate = selectedBirthDate,
            email = email
        )

        viewModelScope.launch {
            isLoading = true
            error = null
            val result = registerUseCase(user, password)
            isLoading = false
            
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                error = it.message ?: "An unknown error occurred"
            }
        }
    }
}
