package com.softserveacademy.travelin2026projectlab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute

//Onboarding Screen.
import com.softserveacademy.feature.onboarding.presentation.ui.OnboardingScreen
import com.softserveacademy.feature.onboarding.presentation.viewmodels.OnboardingViewModel

//Screens of Authgraph
import com.softserveacademy.feature.auth.common.presentation.ui.SuccessScreen
import com.softserveacademy.feature.auth.login.presentation.ui.ForgotPasswordScreen
import com.softserveacademy.feature.auth.login.presentation.viewmodel.ForgotPasswordViewModel
import com.softserveacademy.feature.auth.login.presentation.ui.LoginScreen
import com.softserveacademy.feature.auth.login.presentation.viewmodel.LoginViewModel
import com.softserveacademy.feature.auth.register.presentation.ui.RegisterScreen
import com.softserveacademy.feature.auth.register.presentation.viewmodel.RegisterViewModel
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightResultsViewModel
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightSearchViewModel
import com.softserveacademy.home.presentation.navigation.HomeNavigationActions

// Profile screens.
import com.softserveacademy.profile.presentation.ui.screens.ProfileScreen
import com.softserveacademy.profile.presentation.ui.screens.EditProfileScreen
import com.softserveacademy.profile.presentation.viewmodel.ProfileViewModel
import com.softserveacademy.profile.presentation.viewmodel.EditProfileViewModel

// Home screens.
import com.softserveacademy.home.presentation.ui.screens.RootHomeScreen
import com.softserveacademy.home.presentation.ui.screens.RootUpcomingTripScreen
import com.softserveacademy.home.presentation.ui.screens.TravelHotelGalleryScreen
import com.softserveacademy.home.presentation.ui.screens.TravelListScreen
import com.softserveacademy.home.presentation.model.TravelItemType
import com.softserveacademy.home.presentation.ui.screens.TravelHotelDetailScreen
import com.softserveacademy.home.presentation.ui.screens.TravelTourDetailsScreen

// Booking screens.
import com.softserveacademy.feature.booking.hotel.presentation.ui.screens.HotelEnterBookingDetailsScreen
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelEnterBookingDetailsViewModel
import com.softserveacademy.feature.booking.hotel.presentation.ui.screens.HotelRoomSelectionScreen
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelRoomSelectionViewModel
import com.softserveacademy.feature.booking.hotel.presentation.ui.screens.HotelContactInfoScreen
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelContactInfoViewModel
import com.softserveacademy.feature.booking.hotel.presentation.ui.screens.HotelBookingConfirmScreen
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelBookingConfirmViewModel
import com.softserveacademy.feature.booking.common.presentation.ui.screens.TravelBookingSuccessScreen
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightBookingConfirmScreen
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightPassengerInfoScreen

// FlightBooking screens.
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightSearchScreen
import com.softserveacademy.feature.booking.flight.presentation.ui.screens.FlightResultsScreen
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightBookingConfirmViewModel
import com.softserveacademy.feature.booking.flight.presentation.viewmodel.FlightPassengerInfoViewModel

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
    isFirstTime: Boolean,
    isLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel
) {

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn && !isFirstTime) {
            navController.navigate(Routes.AuthGraph) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = when {
            isFirstTime -> Routes.OnboardingGraph // Changed from OnboardingScreen
            isLoggedIn -> Routes.MainGraph
            else -> Routes.AuthGraph
        }
    ) {
        // Replace the existing composable<Routes.OnboardingScreen> block with this:
        onboardingGraph(navController, isLoggedIn)

        authGraph(
            navController,
            loginViewModel = loginViewModel,
            registerViewModel = registerViewModel,
            forgotPasswordViewModel = forgotPasswordViewModel
        )

        mainGraph(navController, loginViewModel)
        bookingGraph(navController)
        flightGraph(navController)
    }
}


/**
 * Navigation graph that contains the onboarding flow.
 *
 * @param navController The navigation controller used for screen navigation.
 * @param isLoggedIn Determines the destination after onboarding is completed.
 */
fun NavGraphBuilder.onboardingGraph(navController: NavHostController, isLoggedIn: Boolean) {
    navigation<Routes.OnboardingGraph>(
        startDestination = Routes.OnboardingScreen
    ) {
        composable<Routes.OnboardingScreen> {
            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onFinished = {
                    navController.navigate(if (isLoggedIn) Routes.MainGraph else Routes.AuthGraph) {
                        popUpTo(Routes.OnboardingGraph) {
                            inclusive = true
                        }
                    }

                }
            )
        }
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
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {

    navigation<Routes.MainGraph>(
        startDestination = Routes.TravelHomeScreen
    ) {

        composable<Routes.TravelHomeScreen> {
            RootHomeScreen(
                actions = HomeNavigationActions(
                    onHotelClick = { id ->
                        navController.navigate(Routes.TravelHotelDetailsScreen(id = id))
                    },
                    onTourClick = { id ->
                        navController.navigate(Routes.TravelTourDetailsScreen(id = id))
                    },
                    onFlightsClick = {
                        navController.navigate(Routes.FlightSearchScreen)
                    },
                    onAccountClick = {
                        navController.navigate(Routes.ProfileScreen) {
                            popUpTo(Routes.TravelHomeScreen)
                            launchSingleTop = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Routes.ProfileScreen) {
                            popUpTo(Routes.TravelHomeScreen)
                            launchSingleTop = true
                        }
                    },
                    onUpcomingTripClick = { bookingId ->
                        navController.navigate(Routes.TravelUpcomingTripScreen(bookingId = bookingId))
                    },
                    onHotelsSeeAllClick = {
                        navController.navigate(Routes.TravelListScreen(type = TravelItemType.HOTEL))
                    },
                    onJourneySeeAllClick = {
                        navController.navigate(Routes.TravelListScreen(type = TravelItemType.TOUR))
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
                    loginViewModel.resetState()
                    navController.navigate(Routes.AuthGraph) {
                        popUpTo(0) { inclusive = true }
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

        composable<Routes.TravelHotelDetailsScreen>(
            deepLinks = listOf(
                navDeepLink<Routes.TravelHotelDetailsScreen>(
                    basePath = "https://travelin.softserveacademy.com/hotel"
                )
            )
        ) { backStackEntry ->
            val route: Routes.TravelHotelDetailsScreen = backStackEntry.toRoute()
            TravelHotelDetailScreen(
                itemId = route.id,
                onBackClick = { navController.popBackStack() },
                onSeeAllPhotosClick = { navController.navigate(Routes.HotelGalleryScreen(id = route.id)) },
                onBookClick = { navController.navigate(Routes.HotelEnterBookingDetailsScreen(hotelId = route.id)) }
            )
        }

        composable<Routes.TravelTourDetailsScreen>(
            deepLinks = listOf(
                navDeepLink<Routes.TravelTourDetailsScreen>(
                    basePath = "https://travelin.softserveacademy.com/tour"
                )
            )
        ) { backStackEntry ->
            val route: Routes.TravelTourDetailsScreen = backStackEntry.toRoute()
            TravelTourDetailsScreen(
                itemId = route.id,
                onBackClick = { navController.popBackStack() },
                onSeeAllPhotosClick = { navController.navigate(Routes.HotelGalleryScreen(id = route.id)) }
            )
        }

        composable<Routes.HotelGalleryScreen> { backStackEntry ->
            val route: Routes.HotelGalleryScreen = backStackEntry.toRoute()
            TravelHotelGalleryScreen(
                hotelId = route.id,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Routes.TravelUpcomingTripScreen> { backStackEntry ->
            val route: Routes.TravelUpcomingTripScreen = backStackEntry.toRoute()
            RootUpcomingTripScreen(
                onBackClick = { navController.popBackStack() },
                onTabClick = { index ->
                    when (index) {
                        0 -> navController.popBackStack()
                        3 -> navController.navigate(Routes.ProfileScreen) {
                            popUpTo(Routes.TravelHomeScreen)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable<Routes.TravelListScreen> { backStackEntry ->
            val route: Routes.TravelListScreen = backStackEntry.toRoute()
            TravelListScreen(
                type = route.type,
                onBackClick = { navController.popBackStack() },
                onItemClick = { id ->
                    val destination = when (route.type) {
                        TravelItemType.HOTEL -> Routes.TravelHotelDetailsScreen(id = id)
                        TravelItemType.TOUR -> Routes.TravelTourDetailsScreen(id = id)
                    }
                    navController.navigate(destination)
                }
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
        startDestination = Routes.HotelEnterBookingDetailsScreen(hotelId = "0")
    ) {
        composable<Routes.HotelEnterBookingDetailsScreen> { backStackEntry ->
            val route: Routes.HotelEnterBookingDetailsScreen = backStackEntry.toRoute()
            val viewModel: HotelEnterBookingDetailsViewModel = hiltViewModel()

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
            val viewModel: HotelRoomSelectionViewModel = hiltViewModel()
            HotelRoomSelectionScreen(
                onBackClick = { navController.popBackStack() },
                onRoomSelected = {
                    navController.navigate(Routes.HotelContactInfoScreen(hotelId = route.hotelId))
                },
                viewModel = viewModel
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
            val viewModel: HotelBookingConfirmViewModel = hiltViewModel()
            HotelBookingConfirmScreen(
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = {
                    navController.navigate(Routes.TravelBookingSuccessScreen)
                },
                viewModel = viewModel
            )
        }

        composable<Routes.TravelBookingSuccessScreen> {
            TravelBookingSuccessScreen(
                onBackToHome = {
                    navController.navigate(Routes.MainGraph) {
                        popUpTo(Routes.BookingGraph) { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Navigation graph for Flight Booking.
 */
fun NavGraphBuilder.flightGraph(navController: NavHostController) {
    navigation<Routes.FlightBookingGraph>(
        startDestination = Routes.FlightSearchScreen
    ) {
        composable<Routes.FlightSearchScreen> {
            val viewModel: FlightSearchViewModel = hiltViewModel()

            FlightSearchScreen(
                onBack = { navController.popBackStack() },
                onSearchExecuted = {
                    navController.navigate(Routes.FlightResultsScreen)
                },
                viewModel = viewModel
            )
        }

        composable<Routes.FlightResultsScreen> {
            val viewModel: FlightResultsViewModel = hiltViewModel()

            FlightResultsScreen(
                onNext = { navController.navigate(Routes.FlightPassengerInfoScreen) },
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable<Routes.FlightPassengerInfoScreen> {
            val viewModel: FlightPassengerInfoViewModel = hiltViewModel()
            FlightPassengerInfoScreen(
                viewModel = viewModel,
                onNext = {
                    navController.navigate(Routes.FlightBookingConfirmScreen)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<Routes.FlightBookingConfirmScreen> {
            val viewModel: FlightBookingConfirmViewModel = hiltViewModel()
            FlightBookingConfirmScreen(
                onBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Routes.TravelBookingSuccessScreen) {
                        popUpTo(Routes.FlightBookingGraph) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}
