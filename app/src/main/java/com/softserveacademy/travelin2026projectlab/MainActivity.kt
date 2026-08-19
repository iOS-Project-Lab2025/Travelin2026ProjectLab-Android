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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.softserveacademy.core.domain.usecase.splash.GetSplashDestinationUseCase
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.softserveacademycore.presentation.ui.splash.ui.SplashScreen
import com.softserveacademy.feature.auth.common.data.repository.SessionRepositoryImpl
import androidx.navigation.compose.rememberNavController
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.usecase.GetThemeUseCase
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.feature.auth.login.data.repository.LoginRepositoryImpl
import com.softserveacademy.feature.auth.register.data.repository.RegisterRepositoryImpl
import com.softserveacademy.feature.auth.common.domain.usecase.CheckSessionUseCase
import com.softserveacademy.feature.auth.login.domain.usecase.LoginUseCase
import com.softserveacademy.feature.auth.login.domain.usecase.RecoverPasswordUseCase
import com.softserveacademy.feature.auth.register.domain.usecase.RegisterUseCase
import com.softserveacademy.feature.auth.login.presentation.viewmodel.ForgotPasswordViewModel
import com.softserveacademy.feature.auth.login.presentation.viewmodel.LoginViewModel
import com.softserveacademy.feature.auth.register.presentation.viewmodel.RegisterViewModel
import com.softserveacademy.travelin2026projectlab.navigation.NavigationRoot
import com.stripe.android.PaymentConfiguration
import com.softserveacademy.feature.booking.common.data.BuildConfig
import io.github.jan.supabase.SupabaseClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.getValue
import com.softserveacademy.core.data.repository.CorePreferencesRepositoryImpl
import com.softserveacademycore.presentation.ui.splash.viewmodels.SplashViewModel

/**
 * The main entry point of the application.
 *
 * This activity manages the top-level state of the app, including:
 * 1. Theme selection (Light/Dark/System).
 * 2. Session verification (Logged in vs Anonymous).
 * 3. Initial navigation flow (Splash -> Onboarding -> Auth/Main).
 *
 * It uses [AndroidEntryPoint] to enable Hilt dependency injection.
 */
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
    private val getSplashDestinationUseCase by lazy { GetSplashDestinationUseCase(corePreferencesRepository) }
    private val splashViewModel by lazy { SplashViewModel(getSplashDestinationUseCase) }

    @Inject
    lateinit var supabase: SupabaseClient

    private val loginRepository by lazy { LoginRepositoryImpl(supabase) }
    private val registerRepository by lazy { RegisterRepositoryImpl(supabase) }

    private val registerUseCase by lazy { RegisterUseCase(registerRepository) }
    private val loginUseCase by lazy { LoginUseCase(loginRepository) }
    private val recoverPasswordUseCase by lazy { RecoverPasswordUseCase(loginRepository) }
    private val registerViewModel by lazy { RegisterViewModel(registerUseCase) }
    private val loginViewModel by lazy { LoginViewModel(loginUseCase) }
    private val forgotPasswordViewModel by lazy { ForgotPasswordViewModel(recoverPasswordUseCase) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (BuildConfig.STRIPE_PUBLISHABLE_KEY.isNotEmpty()){ // Initialize Stripe
            PaymentConfiguration.init(applicationContext, BuildConfig.STRIPE_PUBLISHABLE_KEY)
        }

        enableEdgeToEdge()
        setContent {

            /**
             * Flag that determines if the user is launching the app for the very first time.
             * Collected from [corePreferencesRepository].
             */
            val isFirstTime by corePreferencesRepository.isFirstTimeUser().collectAsState(initial = true)

            val appTheme by getThemeUseCase().collectAsState(initial = AppTheme.SYSTEM)
            val darkTheme = when (appTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            Travelin2026ProjectLabTheme(darkTheme = darkTheme) {
                val isLoggedIn by checkSessionUseCase().collectAsState(initial = null)

                // creating a local state to control splash visual
                var showSplash by rememberSaveable { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {


                        /**
                         * This guarantees the user watch the logo during the setted time we defined in SplashViewModel
                         * then set the state to false and show the main screen
                         */

                        if (showSplash) {

                            SplashScreen(
                                viewModel = splashViewModel,
                                onNavigateToOnboarding = { showSplash = false },
                                onNavigateToLogin = { showSplash = false }
                            )

                        } else {
                            if (isLoggedIn !== null) {
                                val navController = rememberNavController()

                                NavigationRoot(
                                    navController = navController,
                                    isFirstTime = isFirstTime,
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
}
