package com.softserveacademy.travelin2026projectlab.test.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.travelin2026projectlab.ui.theme.Travelin2026ProjectLabTheme
import kotlinx.coroutines.launch
import java.nio.file.Files.size

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
