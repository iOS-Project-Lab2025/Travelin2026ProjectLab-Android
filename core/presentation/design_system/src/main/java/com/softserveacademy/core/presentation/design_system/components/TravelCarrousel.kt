package com.softserveacademy.core.presentation.design_system.components

import com.softserveacademy.core.presentation.design_system.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.Hotel
import androidx.compose.ui.tooling.preview.Preview

//Here we create a composable of the carrousel
//As entry variables we need to pass the list of hotels available
//at line 37-ish there is a mocklist of hotels as an example
@Composable
fun TravelCarousel(
    packages: List<Hotel>,
    modifier: Modifier = Modifier
) {
    //This is the function that makes the carrousel
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Since the view if cards is ready here we only call that instance
        items(packages) { hotelItem ->
            TravelCardVertical(hotel = hotelItem)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TravelCarouselPreview() {

    val ExampleHotels = listOf(
        Hotel(
            name = "Mount Bromo",
            address = "Volcano in East Java",
            userRating = 4.9,
            pricePerNight = 150,
            //An example picture has been added to Res/drawables
            image = R.drawable.mount_bromo
        ),
        Hotel(
            name = "Swiss-Belhotel Rainforest",
            address = "Kuta, Bali, Indonesia",
            userRating = 4.5,
            pricePerNight = 50,
            image = R.drawable.mount_bromo
        ),
        Hotel(
            name = "Tokyo Adventure Resort",
            address = "Tokyo, Japan",
            userRating = 4.8,
            pricePerNight = 200,
            image = R.drawable.mount_bromo
        )
    )

    // This is the way we call the instance of carrousel
    TravelCarousel(packages = ExampleHotels)
}