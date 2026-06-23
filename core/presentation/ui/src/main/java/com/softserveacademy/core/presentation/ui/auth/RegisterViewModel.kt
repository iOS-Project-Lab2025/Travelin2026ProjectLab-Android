package com.softserveacademy.core.presentation.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.model.User
import com.softserveacademy.core.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var age by mutableStateOf("")
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

        val ageInt = age.toIntOrNull()
        if (ageInt == null) {
            error = "Invalid age"
            return
        }

        val user = User(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            age = ageInt,
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
