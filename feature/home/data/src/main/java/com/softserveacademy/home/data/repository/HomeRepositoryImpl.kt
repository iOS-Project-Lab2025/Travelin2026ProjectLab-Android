package com.softserveacademy.home.data.repository

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.model.Trip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.data.mockdata.HomeMockData
import com.softserveacademy.home.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/**
 * Implementation of [HomeRepository] providing home screen data.
 * Currently uses mock data.
 */
class HomeRepositoryImpl @Inject constructor() : HomeRepository {

    val hotelDetailExampleData = listOf(
        HotelDetails(
        id = 1,
        minimumPrice = 400,
        imageList = listOf(
            "https://picsum.photos/id/237/200/300",
            "https://picsum.photos/id/238/200/300",
            "https://picsum.photos/id/1020/800/600",
            "https://picsum.photos/id/537/200/300",
            "https://picsum.photos/id/134/200/300"
        ),
        name = "Koh Rong Samloem",
        numberOfReviews = 30,
        rating = 3.6,
        description = LoremIpsum(words = 80).values.first(),
        includedItems = listOf(
            IncludedItem.BuffetBreakfast,
            IncludedItem.FreeWifi,
            IncludedItem.Pool
            )
        ),
        HotelDetails(
            id = 2,
            minimumPrice = 800,
            imageList = listOf(
                "https://picsum.photos/id/137/200/300",
                "https://picsum.photos/id/20/800/600",
                "https://picsum.photos/id/937/200/300",
                "https://picsum.photos/id/324/200/300",
                "https://picsum.photos/id/643/200/300",
                "https://picsum.photos/id/682/200/300",
                "https://picsum.photos/id/111/200/300",
                "https://picsum.photos/id/152/200/300",
                "https://picsum.photos/id/137/200/300",
                "https://picsum.photos/id/20/800/600",
                "https://picsum.photos/id/937/200/300",
                "https://picsum.photos/id/324/200/300",
                "https://picsum.photos/id/643/200/300",
                "https://picsum.photos/id/682/200/300",
                "https://picsum.photos/id/111/200/300",
                "https://picsum.photos/id/152/200/300"
            ),
            name = "Testing number 2",
            numberOfReviews = 13,
            rating = 1.6,
            description = LoremIpsum(words = 80).values.first(),
            includedItems = listOf(
                IncludedItem.BuffetBreakfast,
                IncludedItem.FreeWifi,
                IncludedItem.Pool
            )
        )
    )

    override suspend fun getUserProfile(): Result<UserProfile> {

        delay(500)

        return Result.success(
            HomeMockData.user
        )
    }

    override suspend fun getUpcomingTrip(): Result<Trip?> {

        delay(700)

        return Result.success(
            HomeMockData.trip
        )
    }

    override suspend fun getJourneyTogether(): Result<List<Destination>> {

        delay(700)

        return Result.success(
            HomeMockData.destinations
        )
    }

    override suspend fun getRecommendedHotels(): Result<List<Hotel>> {
        delay(700.milliseconds)

        return Result.success(
            listOf(
                Hotel(
                    id = 1,
                    name = "Koh Rong Samloem Resort",
                    address = "Koh Rong Samloem, Cambodia",
                    star = 5,
                    userRating = 4.8,
                    pricePerNight = 400,
                    image = listOf(
                        "https://picsum.photos/id/10/800/600",
                        "https://picsum.photos/id/20/800/600"
                    )
                ),
                Hotel(
                    id = 2,
                    name = "Sunset Paradise",
                    address = "Phuket, Thailand",
                    star = 4,
                    userRating = 4.6,
                    pricePerNight = 280,
                    image = listOf(
                        "https://picsum.photos/id/11/800/600",
                        "https://picsum.photos/id/21/800/600"
                    )
                ),
                Hotel(
                    id = 3,
                    name = "Swiss Mountain Lodge",
                    address = "Zermatt, Switzerland",
                    star = 5,
                    userRating = 4.9,
                    pricePerNight = 650,
                    image = listOf(
                        "https://picsum.photos/id/12/800/600",
                        "https://picsum.photos/id/22/800/600"
                    )
                ),
                Hotel(
                    id = 4,
                    name = "Ocean Breeze",
                    address = "Bali, Indonesia",
                    star = 4,
                    userRating = 4.7,
                    pricePerNight = 320,
                    image = listOf(
                        "https://picsum.photos/id/13/800/600",
                        "https://picsum.photos/id/23/800/600"
                    )
                ),
                Hotel(
                    id = 5,
                    name = "The Grand Palace",
                    address = "Paris, France",
                    star = 5,
                    userRating = 4.9,
                    pricePerNight = 720,
                    image = listOf(
                        "https://picsum.photos/id/14/800/600",
                        "https://picsum.photos/id/24/800/600"
                    )
                )
            )
        )
    }

    override suspend fun getHotelDetailsById(hotelId: Int): HotelDetails {
        //TODO("Remove when finish")
        delay(1000.milliseconds)
        return  hotelDetailExampleData.find{ it.id == hotelId } ?:  hotelDetailExampleData.first()
    }
}