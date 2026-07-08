package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.ui.components.TravelBackground
import com.softserveacademy.home.presentation.ui.components.TravelCard
import com.softserveacademy.home.presentation.ui.components.TravelIconsCard
import com.softserveacademy.home.presentation.ui.components.TravelNavigationBar
import com.softserveacademy.home.presentation.ui.components.TravelTextField

@Composable
fun TravelHomeScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

            }
        }

    }
}


@Preview
@Composable
private fun TravelHomeScreenPreview() {
    Travelin2026ProjectLabTheme() {
        TravelHomeScreen()
    }
}
