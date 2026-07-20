package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.Hotel
import androidx.compose.ui.tooling.preview.Preview

/**
 * A horizontal carousel component that displays a list of travel packages (hotels).
 *
 * This composable uses a [LazyRow] to efficiently display cards for each hotel.
 *
 * @param packages The list of [Hotel] objects to be displayed in the carousel.
 * @param modifier The modifier to be applied to the carousel layout.
 * @param onHotelClick Callback invoked when a specific hotel card is clicked.
 */
@Composable
fun TravelCarousel(
    packages: List<Hotel>,
    modifier: Modifier = Modifier,
    onHotelClick: (Hotel) -> Unit = {}
) {
    //This is the function that makes the carrousel
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Since the view if cards is ready here we only call that instance
        items(packages) { hotelItem ->
            TravelCardVertical(
                hotel = hotelItem,
                onClick = onHotelClick
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TravelCarouselPreview() {

    val exampleHotels = listOf(
        Hotel(
            name = "Mount Bromo",
            address = "Volcano in East Java",
            userRating = 4.9,
            pricePerNight = 150,
            //An example picture has been added to Res/drawables
            image = listOf("https://picsum.photos/200")
        ),
        Hotel(
            name = "Swiss-Belhotel Rainforest",
            address = "Kuta, Bali, Indonesia",
            userRating = 4.5,
            pricePerNight = 50,
            image = listOf("https://picsum.photos/200")
        ),
        Hotel(
            name = "Tokyo Adventure Resort",
            address = "Tokyo, Japan",
            userRating = 4.8,
            pricePerNight = 200,
            image = listOf("https://picsum.photos/200")
        )
    )

    // This is the way we call the instance of carrousel
    TravelCarousel(packages = exampleHotels)
}