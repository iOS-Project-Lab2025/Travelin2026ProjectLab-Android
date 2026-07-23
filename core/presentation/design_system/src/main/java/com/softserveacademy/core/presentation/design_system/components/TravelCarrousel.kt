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
 * A horizontal carousel component that displays a list of travel items.
 *
 * @param items The list of items to be displayed in the carousel.
 * @param modifier The modifier to be applied to the carousel layout.
 * @param itemContent Callback to define the UI for each item.
 */
@Composable
fun <T> TravelCarousel(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            itemContent(item)
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
    TravelCarousel(items = exampleHotels) { hotelItem ->
        TravelCardVertical(
            title = hotelItem.name,
            location = hotelItem.address,
            rating = hotelItem.userRating.toString(),
            price = "$ ${hotelItem.pricePerNight}/pax",
            duration = "3D2N",
            imageUrl = hotelItem.image.first()
        )
    }
}