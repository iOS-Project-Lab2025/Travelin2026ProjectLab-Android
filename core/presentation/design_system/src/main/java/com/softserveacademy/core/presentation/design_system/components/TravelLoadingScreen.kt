package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

/**
 * A simple loading screen with a circular progress indicator in the center.
 *
 * @param modifier The modifier to be applied to the loading screen.
 */
@Composable
fun TravelLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun TravelLoadingScreenPreview(){
    Travelin2026ProjectLabTheme () {
        TravelLoadingScreen()
    }
}