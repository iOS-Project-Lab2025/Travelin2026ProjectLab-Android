package com.softserveacademy.feature.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softserveacademy.core.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun onLoginClick() {
        viewModelScope.launch {
            isLoading = true
            error = null
            val result = loginUseCase(email, password)
            isLoading = false
            
            result.onSuccess {
                isSuccess = true
            }.onFailure {
                error = it.message ?: "An unknown error occurred"
            }
        }
    }
}
