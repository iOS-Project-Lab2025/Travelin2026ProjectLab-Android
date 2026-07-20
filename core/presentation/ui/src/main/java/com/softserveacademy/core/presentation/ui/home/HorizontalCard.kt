package com.softserveacademy.core.presentation.ui.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.shapes
import com.softserveacademy.core.presentation.design_system.components.TravelCardHorizontal
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

@Composable
fun HorizontalCardState(
    modifier: Modifier = Modifier,
    viewModel: HorizontalCardViewModel = hiltViewModel()
){
    val horizontalCardState by viewModel.horizontalCardState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getHotel(1)
    }

    HorizontalCard(state = horizontalCardState, modifier = modifier)
}


@Composable
fun HorizontalCard(state : HorizontalCardState, modifier: Modifier = Modifier) {
    Card(
        shape = shapes.medium,
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        modifier = modifier
    ) {
        when(state){
            is HorizontalCardState.Data -> {
                TravelCardHorizontal(state.hotel)
            }
            is HorizontalCardState.Error -> {
                Text(text = "Error: ${state.message}")
            }
            is HorizontalCardState.IsLoading -> {
                CircularProgressIndicator()
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HorizontalCardPreview() {
    val hotelExample = Hotel(
        name = "Swiss-Belhotel Rainforest Kuta",
        address = "Jl. Sunset Road No. 101, Kuta, Bali , Indonesia",
        star = 4,
        pricePerNight = 50,
        image = listOf("https://picsum.photos/200")
    )

    Travelin2026ProjectLabTheme(darkTheme = true) {
        HorizontalCard(state = HorizontalCardState.Data(hotelExample), modifier = Modifier)
    }
}
