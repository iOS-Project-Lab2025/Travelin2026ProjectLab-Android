package com.softserveacademy.home.presentation.ui.components

import com.softserveacademy.core.domain.model.DestinationDetails
import com.softserveacademy.core.domain.model.HotelDetails

/**
 * Maps a [HotelDetails] domain model to a [DestinationDetails] domain model.
 * 
 * @receiver The [HotelDetails] object to be mapped.
 * @return A [DestinationDetails] object with the mapped data.
 */
fun HotelDetails.toDestinationDetails(): DestinationDetails {
    return DestinationDetails(
        id = id,
        minimumPrice = minimumPrice,
        imageList = imageList,
        name = name,
        address = address,
        star = star,
        image = image,
        numberOfReviews = numberOfReviews,
        rating = rating,
        description = description,
        includedItems = includedItems,
        latitude = latitude,
        longitude = longitude
    )
}
