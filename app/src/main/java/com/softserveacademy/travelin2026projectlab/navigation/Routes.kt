package com.softserveacademy.travelin2026projectlab.navigation


import com.softserveacademy.core.domain.model.TravelItemType
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
    data object ProfileScreen : Routes

    @Serializable
    data object EditProfileScreen : Routes

    @Serializable
    data class TravelHotelDetailScreen(val id: String, val type: TravelItemType = TravelItemType.HOTEL) : Routes

    @Serializable
    data class HotelGalleryScreen(val id: Int) : Routes

    @Serializable
    data class TravelUpcomingTripScreen(val bookingId: String) : Routes

    @Serializable
    data class TravelListScreen(val type: TravelItemType) : Routes

    // ---------------- BOOKING ----------------

    @Serializable
    data class HotelEnterBookingDetailsScreen(val itemId: String, val type: TravelItemType = TravelItemType.HOTEL) : Routes

    @Serializable
    data class HotelRoomSelectionScreen(val hotelId: Int) : Routes

    @Serializable
    data class HotelContactInfoScreen(val hotelId: Int) : Routes

    @Serializable
    data class HotelBookingConfirmationScreen(val hotelId: Int) : Routes


// ---------------- ONBOARDING ----------------
    /**
     * Routes for the Onboarding flow.
     */
    @Serializable
    data object OnboardingScreen : Routes



}
