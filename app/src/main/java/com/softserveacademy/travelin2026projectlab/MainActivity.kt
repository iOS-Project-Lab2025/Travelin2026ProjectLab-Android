package com.softserveacademy.travelin2026projectlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.softserveacademy.core.data.repository.AuthRepositoryImpl
import com.softserveacademy.core.domain.usecase.LoginUseCase
import com.softserveacademy.core.domain.usecase.RecoverPasswordUseCase
import com.softserveacademy.core.domain.usecase.RegisterUseCase
import com.softserveacademy.feature.auth.*
import com.softserveacademy.travelin2026projectlab.ui.theme.Travelin2026ProjectLabTheme

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepositoryImpl()
    private val registerUseCase = RegisterUseCase(authRepository)
    private val loginUseCase = LoginUseCase(authRepository)
    private val recoverPasswordUseCase = RecoverPasswordUseCase(authRepository)
    
    private val registerViewModel = RegisterViewModel(registerUseCase)
    private val loginViewModel = LoginViewModel(loginUseCase)
    private val forgotPasswordViewModel = ForgotPasswordViewModel(recoverPasswordUseCase)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Travelin2026ProjectLabTheme {
                var currentScreen by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "login" -> {
                                LoginScreen(
                                    viewModel = loginViewModel,
                                    onNavigateToRegister = { currentScreen = "register" },
                                    onNavigateToForgotPassword = { currentScreen = "forgot_password" },
                                    onLoginSuccess = { /* Navigate to home */ }
                                )
                            }
                            "forgot_password" -> {
                                ForgotPasswordScreen(
                                    viewModel = forgotPasswordViewModel,
                                    onNavigateBack = { currentScreen = "login" }
                                )
                            }
                            "register" -> {
                                RegisterScreen(
                                    viewModel = registerViewModel,
                                    onNavigateBack = { currentScreen = "login" },
                                    onRegisterSuccess = { currentScreen = "success" }
                                )
                            }
                            "success" -> {
                                SuccessScreen(
                                    onExploreClick = { /* Navigate to home */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
