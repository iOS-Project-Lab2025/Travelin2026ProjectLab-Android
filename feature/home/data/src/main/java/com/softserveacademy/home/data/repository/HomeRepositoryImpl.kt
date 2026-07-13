package com.softserveacademy.home.data.repository

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.softserveacademy.core.domain.model.Destination
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.domain.model.HotelDetails
import com.softserveacademy.core.domain.model.IncludedItem
import com.softserveacademy.core.domain.model.UpcomingTrip
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.home.domain.repository.HomeRepository
import javax.inject.Inject

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
        numberOfImages = 200,
        description = LoremIpsum(words = 80).values.first(),
        includedItems = listOf(
            IncludedItem.BuffetBreakfast,
            IncludedItem.FreeWifi,
            IncludedItem.Pool
            )
        )
    )

    override suspend fun getUserProfile(): Result<UserProfile> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingTrip(): Result<UpcomingTrip?> {
        TODO("Not yet implemented")
    }

    override suspend fun getJourneyTogether(): Result<List<Destination>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecommendedHotels(): Result<List<Hotel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHotelDetailsById(hotelId: Int): HotelDetails {
        return  hotelDetailExampleData.find{ it.id == hotelId } ?:  hotelDetailExampleData.first()
    }
}