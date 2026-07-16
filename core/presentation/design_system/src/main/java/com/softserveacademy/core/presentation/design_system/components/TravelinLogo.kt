package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

/**
 * Official Travelin Logo.
 * Renders the brand identity using the Vector Drawable logo_travelin (SVG/XML).
 */
@Composable
fun TravelinLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.logo_travelin),
        contentDescription = "Travelin Official Logo",
        modifier = modifier.width(280.dp), // Can modify sizing
        contentScale = ContentScale.FillWidth
    )
}

// Preview
@Preview(showBackground = true, name = "Splash Logo View")
@Composable
fun TravelinLogoPreview() {
    Travelin2026ProjectLabTheme {
        Surface(color = MaterialTheme.colorScheme.primary) {
            Box(
                modifier = Modifier.padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                TravelinLogo()
            }
        }
    }
}
