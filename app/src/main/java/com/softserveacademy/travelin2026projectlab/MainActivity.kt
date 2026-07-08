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
import androidx.navigation.compose.rememberNavController
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.auth.common.data.SessionRepositoryImpl
import com.softserveacademy.feature.auth.login.data.LoginRepositoryImpl
import com.softserveacademy.feature.auth.register.data.RegisterRepositoryImpl
import com.softserveacademy.feature.auth.common.domain.CheckSessionUseCase
import com.softserveacademy.feature.auth.login.domain.LoginUseCase
import com.softserveacademy.feature.auth.login.domain.RecoverPasswordUseCase
import com.softserveacademy.feature.auth.register.domain.RegisterUseCase
import com.softserveacademy.feature.auth.login.presentation.*
import com.softserveacademy.feature.auth.register.presentation.*
import com.softserveacademy.feature.auth.common.presentation.*
import com.softserveacademy.feature.profile.presentation.viewmodel.ProfileViewModel
import com.softserveacademy.travelin2026projectlab.navigation.NavigationRoot
import com.softserveacademy.travelin2026projectlab.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

private val Context.dataStore by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sessionRepository by lazy { SessionRepositoryImpl(dataStore) }
    private val loginRepository by lazy { LoginRepositoryImpl(dataStore) }
    private val registerRepository by lazy { RegisterRepositoryImpl(dataStore) }

    private val registerUseCase by lazy { RegisterUseCase(registerRepository) }
    private val loginUseCase by lazy { LoginUseCase(loginRepository) }
    private val recoverPasswordUseCase by lazy { RecoverPasswordUseCase(loginRepository) }
    private val checkSessionUseCase by lazy { CheckSessionUseCase(sessionRepository) }

    private val registerViewModel by lazy { RegisterViewModel(registerUseCase) }
    private val loginViewModel by lazy { LoginViewModel(loginUseCase) }
    private val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(recoverPasswordUseCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Travelin2026ProjectLabTheme {
                val isLoggedIn by checkSessionUseCase().collectAsState(initial = null)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {


                        if (isLoggedIn == null) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else {

                            val navController = rememberNavController()



                            NavigationRoot(
                                navController = navController,
                                // TODO: Replace the hardcoded value with the actual session state
                                // once the authentication flow is fully integrated.
                                isLoggedIn = true,
                                loginViewModel = loginViewModel,
                                registerViewModel = registerViewModel,
                                forgotPasswordViewModel = forgotPasswordViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
