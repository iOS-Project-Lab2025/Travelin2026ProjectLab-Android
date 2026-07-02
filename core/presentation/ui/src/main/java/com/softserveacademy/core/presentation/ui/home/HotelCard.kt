package com.softserveacademy.core.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal

@Composable
fun HotelCardState(
    modifier: Modifier = Modifier,
    viewModel: HotelViewModel = hiltViewModel()
){
    val uiHotelCardState by viewModel.uiHotelCardState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getHotel(1)
    }

    HotelCard(state = uiHotelCardState, modifier = modifier)
}


@Composable
fun HotelCard(state : UIHotelCardState, modifier: Modifier = Modifier) {
    Card(
        shape = shapes.medium,
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        modifier = modifier
            .padding(TravelinDimens.PaddingMedium)
            .height(IntrinsicSize.Min)
            .width(360.dp)
    ) {
        when(state){
            is UIHotelCardState.Data -> {
                TravelCardHorizontal(state.hotel)
            }
            is UIHotelCardState.Error -> {
                Text(text = "Error: ${state.message}")
            }
            is UIHotelCardState.IsLoading -> {
                CircularProgressIndicator()
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HotelCardPreview() {
    val hotelExample = Hotel(
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali , Indonesia",
        star = 4,
        pricePerNight = 50,
        image = R.drawable.test_hotel
    )

    HotelCard(state = UIHotelCardState.Data(hotelExample), modifier = Modifier)

}
