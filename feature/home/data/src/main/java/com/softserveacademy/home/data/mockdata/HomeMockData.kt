package com.softserveacademy.home.data.mockdata

import com.softserveacademy.core.domain.model.Airline
import com.softserveacademy.core.domain.model.Airport
import com.softserveacademy.core.domain.model.BookingGuests
import com.softserveacademy.core.domain.model.BookingParticipants
import com.softserveacademy.core.domain.model.HotelBookingPrice
import com.softserveacademy.core.domain.model.BookingStatus
import com.softserveacademy.core.domain.model.CabinClass
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Flight
import com.softserveacademy.core.domain.model.FlightBooking
import com.softserveacademy.core.domain.model.FlightPassenger
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelBooking
import com.softserveacademy.core.domain.model.PassengerType
import com.softserveacademy.core.domain.model.Ticket
import com.softserveacademy.core.domain.model.TourBooking
import com.softserveacademy.core.domain.model.TourBookingPrice
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.profile.domain.model.UserProfile
import java.util.Calendar
import kotlin.time.Duration.Companion.hours

/**
 * Provides mock data for the home feature used during development and testing.
 *
 * <p>This object contains pre-defined instances of domain models including user profiles,
 * destinations, hotels, and a complete trip with flights, accommodation, and tours.
 * All date values are represented as epoch milliseconds to ensure compatibility with
 * API levels below 26 without requiring desugaring or external libraries.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * val user = HomeMockData.user
 * val trip = HomeMockData.trip
 * }</pre>
 */
object HomeMockData {

    private fun dateLong(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun dateTimeLong(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    val user = UserProfile(
        firstName = "John",
        lastName = "Doe",
        points = 1280,
        avatarUrl = "https://i.pravatar.cc/300",
        location = "Santiago, Chile"
    )


    val destinations = listOf(
        Destination(
            id = "dest_001",
            imageUrl = "https://picsum.photos/id/1015/800/600",
            name = "Bali",
            location = "Indonesia",
            rating = 4.8,
            pricePerPax = 1200.00,
            currency = "USD",
            durationLabel = "5D4N"
        ),
        Destination(
            id = "dest_002",
            imageUrl = "https://picsum.photos/id/1016/800/600",
            name = "Paris",
            location = "France",
            rating = 4.7,
            pricePerPax = 1500.00,
            currency = "EUR",
            durationLabel = "4D3N"
        ),
        Destination(
            id = "dest_003",
            imageUrl = "https://picsum.photos/id/1018/800/600",
            name = "Tokyo",
            location = "Japan",
            rating = 4.9,
            pricePerPax = 1800.00,
            currency = "USD",
            durationLabel = "7D6N"
        )
    )

    val hotels = listOf(
        Hotel(
            id = "1",
            name = "Koh Rong Samloem Resort",
            address = "Koh Rong Samloem, Cambodia",
            starCategory = 4,
            reviewRating = 4.8,
            imageList = listOf("https://picsum.photos/id/10/800/600")
        ),
        Hotel(
            id = "2",
            name = "Sunset Paradise",
            address = "Phuket, Thailand",
            starCategory = 4,
            reviewRating = 4.6,
            imageList = listOf("https://picsum.photos/id/11/800/600")
        ),
        Hotel(
            id = "3",
            name = "Swiss Mountain Lodge",
            address = "Zermatt, Switzerland",
            starCategory = 5,
            reviewRating = 4.9,
            imageList = listOf("https://picsum.photos/id/12/800/600")
        ),
        Hotel(
            id = "4",
            name = "Ocean Breeze",
            address = "Bali, Indonesia",
            starCategory = 4,
            reviewRating = 4.7,
            imageList = listOf("https://picsum.photos/id/13/800/600")
        ),
        Hotel(
            id = "5",
            name = "The Grand Palace",
            address = "Paris, France",
            starCategory = 5,
            reviewRating = 4.9,
            imageList = listOf("https://picsum.photos/id/14/800/600")
        )
    )

    val trip = Trip(
        id = "trip_001",

        destination = destinations.first(),

        startDate = dateLong(2026, 8, 6),

        endDate = dateLong(2026, 8, 11),

        flights = listOf(
            FlightBooking(
                bookingId = "flight_booking_001",
                userId = "user_123",
                flights = listOf(
                    Flight(
                        id = "flight_001",
                        airline = Airline(
                            code = "LA",
                            name = "LATAM Airlines",
                            logoUrl = "https://picsum.photos/id/1/200"
                        ),
                        flightNumber = "LA500",
                        origin = Airport(
                            code = "SCL",
                            name = "Arturo Merino Benítez International Airport",
                            city = "Santiago",
                            country = "Chile"
                        ),
                        destination = Airport(
                            code = "CDG",
                            name = "Charles de Gaulle Airport",
                            city = "Paris",
                            country = "France"
                        ),
                        departureTime = dateTimeLong(2026, 8, 6, 10, 30),
                        arrivalTime = dateTimeLong(2026, 8, 6, 23, 15),
                        duration = 13.hours,
                        cabinClass = CabinClass.ECONOMY
                    ),
                    Flight(
                        id = "flight_002",
                        airline = Airline(
                            code = "LA",
                            name = "LATAM Airlines",
                            logoUrl = "https://picsum.photos/id/1/200"
                        ),
                        flightNumber = "LA501",
                        origin = Airport(
                            code = "CDG",
                            name = "Charles de Gaulle Airport",
                            city = "Paris",
                            country = "France"
                        ),
                        destination = Airport(
                            code = "SCL",
                            name = "Arturo Merino Benítez International Airport",
                            city = "Santiago",
                            country = "Chile"
                        ),
                        departureTime = dateTimeLong(2026, 8, 11, 14, 0),
                        arrivalTime = dateTimeLong(2026, 8, 12, 5, 30),
                        duration = 13.hours,
                        cabinClass = CabinClass.ECONOMY
                    )
                ),
                passengers = listOf(
                    FlightPassenger(firstName = "John", lastName = "Doe", passengerType = com.softserveacademy.core.domain.model.PassengerType.ADT),
                    FlightPassenger(
                        firstName = "Jane",
                        lastName = "Doe",
                        passengerType = PassengerType.ADT
                    )
                ),
                tickets = listOf(
                    Ticket(
                        ticketNumber = "TK-987654321",
                        passengerName = "John Doe",
                        flightId = "11",
                        flightNumber = "LA500",
                        originCode = "SCL",
                        destinationCode = "CDG",
                        seatNumber = "12A",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        cabinClass = CabinClass.ECONOMY
                    ),
                    Ticket(
                        ticketNumber = "TK-987654322",
                        passengerName = "Jane Doe",
                        flightId = "12",
                        flightNumber = "LA500",
                        originCode = "SCL",
                        destinationCode = "CDG",
                        seatNumber = "12B",
                        gate = "B15",
                        boardingGroup = "Group 2",
                        cabinClass = CabinClass.ECONOMY
                    )
                ),
                confirmationCode = "ABC123",
                status = BookingStatus.COMPLETED,
                totalAmount = 900.0,
                currencyCode = "USD",
                contactInfo = com.softserveacademy.core.domain.model.BookingContactInfo(
                    firstName = "John",
                    lastName = "Doe",
                    email = "john.doe@travelin.com",
                    phoneNumber = "987654321",
                    countryCode = "+56"
                ),
                createdAt = dateTimeLong(2026, 7, 31, 10, 0)
            )
        ),

        hotel = HotelBooking(
            bookingId = "hotel_booking_001",

            hotelId = "1",

            roomId = "11",

            checkIn = dateLong(2026, 8, 6),

            checkOut = dateLong(2026, 8, 11),

            guests = BookingGuests(adults = 2),

            price = HotelBookingPrice(
                ratePerNight = 50,
                roomSubtotal = 250,
                total = 250
            ),

            confirmationCode = "HOTEL456",

            status = BookingStatus.COMPLETED,

            createdAt = dateLong(2026, 7, 31),
            userId = "Prueba"
        ),

        tours = listOf(
            TourBooking(
                bookingId = "tour_booking_001",
                userId = "Prueba",
                tourId = "tour_1",
                startDate = dateLong(2026, 8, 8),
                endDate = dateLong(2026, 8, 10),
                participants = BookingParticipants(
                    adults = 1,
                    children = 0,
                    infants = 0
                ),
                price = TourBookingPrice(
                    ratePerAdult = 120.0,
                    ratePerChildren = 60.0,
                    ratePerInfant = 0.0,
                    subtotal = 250,
                    total = 250
                ),
                confirmationCode = "TOUR789",
                status = BookingStatus.COMPLETED,
                createdAt = dateLong(2026, 7, 31),
            )
        )
    )
}