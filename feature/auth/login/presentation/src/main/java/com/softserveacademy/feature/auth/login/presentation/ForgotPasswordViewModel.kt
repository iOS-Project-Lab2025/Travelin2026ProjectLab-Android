package com.softserveacademy.feature.auth.login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.feature.auth.login.domain.RecoverPasswordUseCase
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val recoverPasswordUseCase: RecoverPasswordUseCase
) : ViewModel() {

    var email by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun onRecoverClick() {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = recoverPasswordUseCase(email)
            isLoading = false
            
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                error = it.message ?: "An unknown error occurred"
            }
        }
    }
}
