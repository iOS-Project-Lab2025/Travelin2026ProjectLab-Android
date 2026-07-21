package com.softserveacademy.travelin2026projectlab.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute

//Onboarding Screen.
import com.softserveacademy.feature.onboarding.presentation.OnboardingScreen
import com.softserveacademy.feature.onboarding.presentation.OnboardingViewModel

//Screens of Authgraph
import com.softserveacademy.feature.auth.common.presentation.SuccessScreen
import com.softserveacademy.feature.auth.login.presentation.ForgotPasswordScreen
import com.softserveacademy.feature.auth.login.presentation.ForgotPasswordViewModel
import com.softserveacademy.feature.auth.login.presentation.LoginScreen
import com.softserveacademy.feature.auth.login.presentation.LoginViewModel
import com.softserveacademy.feature.auth.register.presentation.RegisterScreen
import com.softserveacademy.feature.auth.register.presentation.RegisterViewModel
import com.softserveacademy.home.presentation.navigation.HomeNavigationActions
// Profile screens.
import com.softserveacademy.home.presentation.ui.screens.ProfileScreen
import com.softserveacademy.home.presentation.ui.screens.EditProfileScreen
import com.softserveacademy.home.presentation.viewmodel.ProfileViewModel
import com.softserveacademy.home.presentation.viewmodel.EditProfileViewModel

// Home screens.
import com.softserveacademy.home.presentation.ui.screens.HotelDetailState
import com.softserveacademy.home.presentation.ui.screens.RootHomeScreen
import com.softserveacademy.home.presentation.ui.screens.TravelHotelGalleryScreen
// Booking screens.
import com.softserveacademy.feature.booking.presentation.HotelEnterBookingDetailsScreen
import com.softserveacademy.feature.booking.presentation.HotelEnterBookingDetailsViewModel
import com.softserveacademy.feature.booking.presentation.HotelRoomSelectionScreen
import com.softserveacademy.feature.booking.presentation.HotelContactInfoScreen
import com.softserveacademy.feature.booking.presentation.HotelContactInfoViewModel
import com.softserveacademy.feature.booking.presentation.HotelBookingConfirmationScreen


/**
 * Root navigation host for the application.
 *
 * It defines the conditional start destination based on two primary factors:
 * @param isFirstTime If true, the user is redirected to the [Routes.OnboardingScreen].
 * @param isLoggedIn If [isFirstTime] is false, this flag determines if the user goes to [Routes.MainGraph]
 * or [Routes.AuthGraph].
 *
 * This component acts as the main router after the Splash screen is dismissed.
 */
@Composable
fun NavigationRoot(
    navController: NavHostController,
    isFirstTime: Boolean, // Priority check: Is it the user's first time?
    isLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel
) {

    NavHost(
        navController = navController,

        // If it's the first time, go to Onboarding. Then checks if the user is logged in, if yes goes to mainGraph, otherwise go to Auth (AuthGraph).
        startDestination = when {
            isFirstTime -> Routes.OnboardingScreen
            isLoggedIn -> Routes.MainGraph
            else -> Routes.AuthGraph
        }
    ) {

        composable<Routes.OnboardingScreen> {
            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                onGetStarted = {
                    onboardingViewModel.onGetStartedClick {
                        navController.navigate(if (isLoggedIn) Routes.MainGraph else Routes.AuthGraph) {

                            popUpTo(Routes.OnboardingScreen) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

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
                    navController.popBackStack()
                },
            )

        }

        composable<Routes.SuccessScreen> {
            SuccessScreen(
                onExploreClick={navController.navigate(Routes.MainGraph)
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
        startDestination = Routes.TravelHomeScreen
    ) {

        composable<Routes.TravelHomeScreen> {
            RootHomeScreen(
                actions = HomeNavigationActions(
                    onHotelClick = { hotel ->
                        navController.navigate(Routes.TravelHotelDetailScreen(id = hotel.id ?: 1))
                    },
                    onAccountClick = {
                        navController.navigate(Routes.ProfileScreen) {
                            popUpTo(Routes.TravelHomeScreen)
                            launchSingleTop = true
                        }
                    }
                )
            )
        }

        composable<Routes.ProfileScreen> {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogoutSuccess = {
                    navController.navigate(Routes.AuthGraph) {
                        popUpTo(Routes.MainGraph) { inclusive = true }
                    }
                },
                onEditProfileClick = {
                    navController.navigate(Routes.EditProfileScreen)
                },
                onHomeClick = {
                    navController.navigate(Routes.TravelHomeScreen) {
                        popUpTo(Routes.TravelHomeScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Routes.EditProfileScreen> {
            val viewModel: EditProfileViewModel = hiltViewModel()
            EditProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable<Routes.TravelHotelDetailScreen>(
            deepLinks = listOf(
                navDeepLink<Routes.TravelHotelDetailScreen>(
                    basePath = "https://travelin.softserveacademy.com/hotel"
                )
            )
        ) { backStackEntry ->
            val route: Routes.TravelHotelDetailScreen = backStackEntry.toRoute()
            HotelDetailState(
                hotelId = route.id, // Receive the ID
                onBackClick = { navController.navigate(Routes.TravelHomeScreen) },
                onSeeAllPhotosClick = { navController.navigate(Routes.HotelGalleryScreen(id = route.id)) },
                onBookClick = { navController.navigate(Routes.HotelEnterBookingDetailsScreen(hotelId = route.id)) }
            )
        }

        composable<Routes.HotelGalleryScreen> { backStackEntry ->
            val route: Routes.HotelGalleryScreen = backStackEntry.toRoute()
            TravelHotelGalleryScreen(
                hotelId = route.id, // Receive the ID
                onBackClick = { navController.popBackStack() }
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
        startDestination = Routes.HotelEnterBookingDetailsScreen(hotelId = 0)
    ) {
        composable<Routes.HotelEnterBookingDetailsScreen> { backStackEntry ->
            val route: Routes.HotelEnterBookingDetailsScreen = backStackEntry.toRoute()
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.BookingGraph)
            }
            val viewModel: HotelEnterBookingDetailsViewModel = hiltViewModel(parentEntry)

            HotelEnterBookingDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToRoomSelection = {
                    navController.navigate(Routes.HotelRoomSelectionScreen(hotelId = route.hotelId)) 
                },
                viewModel = viewModel
            )
        }

        composable<Routes.HotelRoomSelectionScreen> { backStackEntry ->
            val route: Routes.HotelRoomSelectionScreen = backStackEntry.toRoute()
            HotelRoomSelectionScreen(
                onBackClick = { navController.popBackStack() },
                onRoomSelected = {
                    navController.navigate(Routes.HotelContactInfoScreen(hotelId = route.hotelId))
                }
            )
        }

        composable<Routes.HotelContactInfoScreen> { backStackEntry ->
            val route: Routes.HotelContactInfoScreen = backStackEntry.toRoute()
            val viewModel: HotelContactInfoViewModel = hiltViewModel()

            HotelContactInfoScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = {
                    navController.navigate(Routes.HotelBookingConfirmationScreen(hotelId = route.hotelId))
                },
                viewModel = viewModel
            )
        }

        composable<Routes.HotelBookingConfirmationScreen> {
            HotelBookingConfirmationScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
