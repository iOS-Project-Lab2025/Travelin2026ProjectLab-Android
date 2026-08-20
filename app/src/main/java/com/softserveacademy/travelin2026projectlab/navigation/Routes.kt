package com.softserveacademy.travelin2026projectlab.navigation


import com.softserveacademy.home.presentation.model.TravelItemType
import kotlinx.serialization.Serializable
/**
 * Defines all navigation destinations used throughout the application.
 *
 * Routes are organized into navigation graphs that group related screens:
 * - AuthGraph: Authentication flow.
 * - MainGraph: Main application flow.
 * - BookingGraph: Booking-related flow.
 *
 * All routes are marked as @Serializable to support type-safe navigation.
 */
sealed interface Routes {

    // ---------------- GRAPH ROOTS ----------------
    @Serializable
    data object AuthGraph : Routes

    @Serializable
    data object MainGraph : Routes

    @Serializable
    data object BookingGraph : Routes

    @Serializable
    data object OnboardingGraph : Routes

    @Serializable data object FlightBookingGraph : Routes

    // ---------------- AUTH ----------------
    @Serializable
    data object LoginScreen : Routes

    @Serializable
    data object RegisterScreen : Routes

    @Serializable
    data object SuccessScreen : Routes
    @Serializable
    data object ForgotPasswordScreen : Routes

    // ---------------- MAIN ----------------


    @Serializable
    data object TravelHomeScreen : Routes

    @Serializable
    data object TravelFavoritesScreen : Routes

    @Serializable
    data object FavoritesHotelsScreen : Routes

    @Serializable
    data object FavoritesToursScreen : Routes

    @Serializable
    data object ProfileScreen : Routes

    @Serializable
    data object EditProfileScreen : Routes

    @Serializable
    data class TravelHotelDetailsScreen(val id: String) : Routes

    @Serializable
    data class TravelTourDetailsScreen(val id: String) : Routes

    @Serializable
    data class TravelPoiDetailsScreen(val id: String) : Routes

    @Serializable
    data class HotelGalleryScreen(val id: String) : Routes

    @Serializable
    data class TravelUpcomingTripScreen(val bookingId: String) : Routes

    @Serializable
    data class TravelListScreen(val type: TravelItemType) : Routes

    // ---------------- HOTEL BOOKING ----------------

    @Serializable
    data class HotelEnterBookingDetailsScreen(val hotelId: String) : Routes

    @Serializable
    data class HotelRoomSelectionScreen(val hotelId: String) : Routes

    @Serializable
    data class HotelContactInfoScreen(val hotelId: String) : Routes

    @Serializable
    data class HotelBookingConfirmationScreen(val hotelId: String) : Routes

    // ---------------- TOUR BOOKING ----------------

    @Serializable
    data class TourEnterBookingDetailsScreen(val tourId: String) : Routes

    @Serializable
    data class TourContactInfoScreen(val tourId: String) : Routes

    @Serializable
    data class TourBookingConfirmationScreen(val tourId: String) : Routes

    @Serializable
    data object TravelBookingSuccessScreen : Routes


// ---------------- ONBOARDING ----------------
    /**
     * Routes for the Onboarding flow.
     */
    @Serializable
    data object OnboardingScreen : Routes

// ---------------- FLIGHT BOOKING ----------------
    /**
     * Routes for the Flight Booking flow. Shows calendar and search passenger options.
     */
    @Serializable
    data object FlightSearchScreen : Routes

    /**
     * Routes for the Flight Results flow. Show list of available flights.
     */
    @Serializable
    data object FlightResultsScreen : Routes

    /**
     * Routes for the Flight Passenger Info flow. Show passenger information.
     */
    @Serializable
    data object FlightPassengerInfoScreen : Routes

    /**
     * Routes for the Flight Booking Confirmation (Checkout).
     */
    @Serializable
    data object FlightBookingConfirmScreen : Routes
}
