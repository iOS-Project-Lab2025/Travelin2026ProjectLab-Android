package com.softserveacademy.travelin2026projectlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softserveacademycore.presentation.ui.splash.SplashScreen
import com.softserveacademy.feature.auth.common.data.SessionRepositoryImpl
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.auth.login.data.LoginRepositoryImpl
import com.softserveacademy.feature.auth.register.data.RegisterRepositoryImpl
import com.softserveacademy.feature.auth.common.domain.CheckSessionUseCase
import com.softserveacademy.feature.auth.login.domain.LoginUseCase
import com.softserveacademy.feature.auth.login.domain.RecoverPasswordUseCase
import com.softserveacademy.feature.auth.register.domain.RegisterUseCase
import com.softserveacademy.feature.auth.login.presentation.*
import com.softserveacademy.feature.auth.register.presentation.*
import com.softserveacademy.feature.auth.common.presentation.*
import com.softserveacademy.travelin2026projectlab.navigation.NavigationRoot
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue
import com.softserveacademy.core.data.repository.CorePreferencesRepositoryImpl
import com.softserveacademycore.presentation.ui.splash.SplashViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    lateinit var checkSessionUseCase: CheckSessionUseCase

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase


   //for splash screen in core state preferences
    private val corePreferencesRepository by lazy { CorePreferencesRepositoryImpl(dataStore) }

    // Create the "splash screen brain" and decides where to go
    private val splashViewModel by lazy { SplashViewModel(corePreferencesRepository) }
    private val sessionRepository by lazy { SessionRepositoryImpl(dataStore) }
    private val loginRepository by lazy { LoginRepositoryImpl(dataStore) }
    private val registerRepository by lazy { RegisterRepositoryImpl(dataStore) }

    private val registerUseCase by lazy { RegisterUseCase(registerRepository) }
    private val loginUseCase by lazy { LoginUseCase(loginRepository) }
    private val recoverPasswordUseCase by lazy { RecoverPasswordUseCase(loginRepository) }

    private val registerViewModel by lazy { RegisterViewModel(registerUseCase) }
    private val loginViewModel by lazy { LoginViewModel(loginUseCase) }
    private val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(recoverPasswordUseCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appTheme by getThemeUseCase().collectAsState(initial = AppTheme.SYSTEM)
            val darkTheme = when (appTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            Travelin2026ProjectLabTheme(darkTheme = darkTheme) {
                val isLoggedIn by checkSessionUseCase().collectAsState(initial = null)

                // creating a local state to control splash visual
                var showSplash by remember { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {


                        /**
                         * This guarantees the user watch the logo during the setted time we defined in SplashViewModel
                         * and do not desapear until we know if user is logged or not
                         */

                        if (showSplash || isLoggedIn == null) {
                            // MIENTRAS estemos en tiempo de Splash O cargando sesión,
                            // mostramos la pantalla azul con tu logo.
                            SplashScreen(
                                viewModel = splashViewModel,
                                onNavigateToOnboarding = { showSplash = false },
                                onNavigateToHome = { showSplash = false }
                            )


                        /*if (isLoggedIn == null) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }*/

                        } else {
                            val navController = rememberNavController()

                            NavigationRoot(
                                navController = navController,
                                isLoggedIn = isLoggedIn == true,
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
