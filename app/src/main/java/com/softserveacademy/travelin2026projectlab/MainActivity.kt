package com.softserveacademy.travelin2026projectlab

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.preferencesDataStore
import com.softserveacademy.core.data.repository.AuthRepositoryImpl
import com.softserveacademy.core.domain.usecase.CheckSessionUseCase
import com.softserveacademy.core.domain.usecase.LoginUseCase
import com.softserveacademy.core.domain.usecase.RecoverPasswordUseCase
import com.softserveacademy.core.domain.usecase.RegisterUseCase
import com.softserveacademy.feature.auth.*
import com.softserveacademy.travelin2026projectlab.ui.theme.Travelin2026ProjectLabTheme

private val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private val authRepository by lazy { AuthRepositoryImpl(dataStore) }
    private val registerUseCase by lazy { RegisterUseCase(authRepository) }
    private val loginUseCase by lazy { LoginUseCase(authRepository) }
    private val recoverPasswordUseCase by lazy { RecoverPasswordUseCase(authRepository) }
    private val checkSessionUseCase by lazy { CheckSessionUseCase(authRepository) }
    
    private val registerViewModel by lazy { RegisterViewModel(registerUseCase) }
    private val loginViewModel by lazy { LoginViewModel(loginUseCase) }
    private val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(recoverPasswordUseCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Travelin2026ProjectLabTheme {
                val isLoggedIn by checkSessionUseCase().collectAsState(initial = null)
                var currentScreen by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn != null) {
                        currentScreen = if (isLoggedIn == true) "success" else "login"
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        if (currentScreen == null) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else {
                            when (currentScreen) {
                                "login" -> {
                                    LoginScreen(
                                        viewModel = loginViewModel,
                                        onNavigateToRegister = { currentScreen = "register" },
                                        onNavigateToForgotPassword = { currentScreen = "forgot_password" },
                                        onLoginSuccess = { currentScreen = "success" }
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
}
