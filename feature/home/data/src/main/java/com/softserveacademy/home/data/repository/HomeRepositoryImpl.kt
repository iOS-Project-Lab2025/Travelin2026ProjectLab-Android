package com.softserveacademy.home.data.repository

import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.HomeHotel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.UpcomingTrip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.HomeRepository
import kotlinx.coroutines.delay

class HomeRepositoryImpl : HomeRepository {

    override suspend fun getUserProfile(): Result<UserProfile> {
        delay(500) // simula latencia de red
        return Result.success(
            UserProfile(
                name = "Paul",
                points = 2000,
                avatarUrl = "https://i.pravatar.cc/150?img=12"
            )
        )
    }

    override suspend fun getUpcomingTrip(): Result<UpcomingTrip?> {
        delay(800)
        return Result.success(
            UpcomingTrip(
                status = "Upcoming",
                date = "24 March 2024",
                originCode = "CGK",
                originTime = "05:30",
                destinationCode = "DPS",
                destinationTime = "06:30",
                durationMinutes = 90,
                airline = "Sentosa Air",
                travelClass = "Economy",
                flightType = "Direct",
                bookingId = "ZEEBAW"
            )
        )
    }

    override suspend fun getJourneyTogether(): Result<List<Destination>> {
        delay(1000)
        return Result.success(
            listOf(
                Destination(
                    id = "1",
                    imageUrl = "https://picsum.photos/seed/bromo/400/300",
                    name = "Mount Bromo",
                    location = "Volcano in East Java",
                    rating = 4.9,
                    pricePerPax = 150.0,
                    currency = "USD",
                    durationLabel = "3D2N"
                ),
                Destination(
                    id = "2",
                    imageUrl = "https://picsum.photos/seed/labengki/400/300",
                    name = "Labengki Sombori",
                    location = "Islands in Sulawesi",
                    rating = 4.8,
                    pricePerPax = 250.0,
                    currency = "USD",
                    durationLabel = "3D2N"
                ),
                Destination(
                    id = "3",
                    imageUrl = "https://picsum.photos/seed/sailing/400/300",
                    name = "Sailing Komodo",
                    location = "Labuan Bajo",
                    rating = 4.8,
                    pricePerPax = 200.0,
                    currency = "USD",
                    durationLabel = "3D2N"
                )
            )
        )
    }

    override suspend fun getRecommendedHotels(): Result<List<HomeHotel>> {
        delay(700)
        return Result.success(
            listOf(
                HomeHotel(
                    id = "1",
                    imageUrl = "https://picsum.photos/seed/hotel1/400/300",
                    name = "Swiss-Belhotel Rainforest Kuta",
                    address = "Jl. Sunset Road No. 101, Kuta, Bali, Indonesia",
                    starRating = 4,
                    pricePerNight = 85.0,
                    currency = "USD"
                )
            )
        )
    }
}


