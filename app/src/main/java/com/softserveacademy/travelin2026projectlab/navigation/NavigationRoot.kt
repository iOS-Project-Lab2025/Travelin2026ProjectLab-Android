package com.softserveacademy.travelin2026projectlab.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
//Screens of Authgraph
import com.softserveacademy.feature.auth.common.presentation.SuccessScreen
import com.softserveacademy.feature.auth.login.presentation.ForgotPasswordScreen
import com.softserveacademy.feature.auth.login.presentation.ForgotPasswordViewModel
import com.softserveacademy.feature.auth.login.presentation.LoginScreen
import com.softserveacademy.feature.auth.login.presentation.LoginViewModel
import com.softserveacademy.feature.auth.register.presentation.RegisterScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.feature.auth.register.presentation.RegisterViewModel
import com.softserveacademy.feature.profile.presentation.ui.screens.ProfileScreen
import com.softserveacademy.feature.profile.presentation.viewmodel.ProfileViewModel

import com.softserveacademycore.presentation.ui.bookinggraph.DetailScreen
import com.softserveacademycore.presentation.ui.maingraph.HomeScreen

/**
 * Root navigation host for the application.
 *
 * Sets up the main navigation graphs and determines the initial destination
 * based on the user's authentication state(if it's logged in or not.)
 *
 * @param navController The navigation controller used to navigate between screens.
 * @param isLoggedIn Indicates whether the user is authenticated.
 * @param loginViewModel ViewModel that manages the login screen state.
 * @param registerViewModel ViewModel that manages the registration screen state.
 * @param forgotPasswordViewModel ViewModel that manages the forgot password screen state.
 */

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel
) {

    NavHost(
        navController = navController,
        //this line is commented since we haven´t discussed the isloggenin variable
        //startDestination = if (isLoggedIn) Routes.MainGraph else Routes.AuthGraph
        startDestination = Routes.AuthGraph
    ) {

        authGraph(navController, loginViewModel = loginViewModel,
            registerViewModel=registerViewModel,
            forgotPasswordViewModel=forgotPasswordViewModel)


        mainGraph(navController)
        bookingGraph(navController)
    }
}

/**
 * Navigation graph that contains all authentication-related screens.
 *
 * This graph includes:
 * - Login
 * - Register
 * - Forgot Password
 * - Success
 *
 * It also defines the navigation flow between these destinations.
 *
 * @param navController The navigation controller used for screen navigation.
 * @param loginViewModel ViewModel used by the login screen.
 * @param registerViewModel ViewModel used by the registration screen.
 * @param forgotPasswordViewModel ViewModel used by the forgot password screen.
 */
fun NavGraphBuilder.authGraph(navController: NavHostController,
                              loginViewModel: LoginViewModel,
                              registerViewModel:RegisterViewModel,
                              forgotPasswordViewModel:ForgotPasswordViewModel
) {

    navigation<Routes.AuthGraph>(
        startDestination = Routes.LoginScreen
    ) {

        composable<Routes.LoginScreen> {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.MainGraph) {
                        popUpTo(Routes.AuthGraph) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = { navController.navigate(Routes.ForgotPasswordScreen) },
                onNavigateToRegister = { navController.navigate(Routes.RegisterScreen) },
            )


        }

        composable<Routes.RegisterScreen> {
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.MainGraph) {
                        popUpTo(Routes.AuthGraph) { inclusive = true }
                    }
                },
                onNavigateBack={
                    // Clear the authentication flow from the back stack
                    // so users cannot return to Login after signing in.
                    navController.popBackStack()
                },
            )

        }

        composable<Routes.SuccessScreen> {
            SuccessScreen(
                onExploreClick={navController.navigate(Routes.HomeScreen)
                {
                    popUpTo(Routes.AuthGraph) { inclusive = true }
                }
                }
            )


        }

        composable<Routes.ForgotPasswordScreen> {
            ForgotPasswordScreen(
                viewModel= forgotPasswordViewModel,
                onNavigateBack= {navController.navigate(Routes.AuthGraph)},
                navonRecoverClick = {navController.navigate(Routes.AuthGraph)}
            )

        }
    }
}
/**
 * Navigation graph that contains the application's main screens.
 * The screens inside this graph is work in progress.
 *
 * This graph becomes the primary navigation flow after the user
 * has successfully authenticated.
 *
 * @param navController The navigation controller used for screen navigation.
 */
fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {

    navigation<Routes.MainGraph>(
        startDestination = Routes.HomeScreen
    ) {

        composable<Routes.HomeScreen> {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(Routes.BookingGraph)
                },
                onItemClick = {
                    navController.navigate(Routes.ProfileScreen)
                }
            )
        }

        composable<Routes.ProfileScreen> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}
/**
 * Navigation graph that contains the booking flow.
 *
 * This graph handles hotel details and booking-related screens.
 *
 * @param navController The navigation controller used for screen navigation.
 */
fun NavGraphBuilder.bookingGraph(navController: NavHostController) {

    navigation<Routes.BookingGraph>(
        startDestination = Routes.DetailScreen
    ) {

        composable<Routes.DetailScreen> {
            DetailScreen(
                onSearchClick = {
                    navController.navigate(Routes.AuthGraph)
                },
                onItemClick = {
                    navController.navigate(Routes.AuthGraph)
                }
            )
        }
    }
}