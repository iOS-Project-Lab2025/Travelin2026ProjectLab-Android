package com.softserveacademy.feature.favorites.common.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.HeartFilledIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme

/**
 * Empty or not authenticated favorite screen
 */
@Composable
fun TravelEmptyFavorites(
    title: String,
    subtitle: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = HeartFilledIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(90.dp) // Ajuste idéntico a Figma
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            TravelAuthPrimaryButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
            )
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(showBackground = true, name = "Empty Favorites State")
@Composable
private fun TravelEmptyFavoritesPreview() {
    Travelin2026ProjectLabTheme {
        TravelEmptyFavorites(
            title = "No Favourites Yet",
            subtitle = "Save your favourite items to\nfind them easily later!",
            buttonText = "Go Back",
            onButtonClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Protected State (Not logged in)")
@Composable
private fun TravelProtectedFavoritesPreview() {
    Travelin2026ProjectLabTheme {
        TravelEmptyFavorites(
            title = "Oops! It's seems\nyou are not a user yet",
            subtitle = "Please sign in so you can\nsave your favourite items to\nfound them easily here!",
            buttonText = "Sign in",
            onButtonClick = {}
        )
    }
}