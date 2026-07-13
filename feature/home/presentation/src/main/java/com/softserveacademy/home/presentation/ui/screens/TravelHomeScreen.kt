package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softserveacademy.core.domain.model.Hotel
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.TravelCarousel
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.ui.components.TravelBackground
import com.softserveacademy.home.presentation.ui.components.TravelCard
import com.softserveacademy.home.presentation.ui.components.TravelIconsCard
import com.softserveacademy.home.presentation.ui.components.TravelNavigationBar
import com.softserveacademy.home.presentation.ui.components.TravelTextField

@Composable
fun TravelHomeScreen(
    onHotelClick: (Hotel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val hotels by viewModel.hotels.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
       // floatingActionButton = { TravelFab() },
        bottomBar = { TravelNavigationBar() }) { innerPadding ->
        Box(modifier = modifier.fillMaxSize().padding(innerPadding)) {
            TravelBackground()
            Column(
                modifier = Modifier
                    .padding(20.dp),
            ) {
                Spacer(Modifier.height(16.dp))
                TravelTextField()
                Spacer(Modifier.height(16.dp))
                TravelCard()
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.height(16.dp))
                TravelIconsCard()
                Spacer(Modifier.height(16.dp))
                TravelCarousel(
                    packages = hotels,
                    onHotelClick = onHotelClick
                )
                Spacer(Modifier.height(16.dp))
                //Button(onClick =// TODO ) { }

            }
        }

    }
}


@Preview
@Composable
private fun TravelHomeScreenPreview() {
    Travelin2026ProjectLabTheme() {
        TravelHomeScreen(onHotelClick = {})
    }
}
