package com.softserveacademy.feature.onboarding.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.components.TravelinLogo
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademycore.presentation.ui.util.LockScreenOrientation
import com.softserveacademy.core.presentation.design_system.R as DesignR

/**
 * Onboarding Screen component.
 * Displays the introductory brand identity and a call to action to start the user journey.
 * Supports both Light and Dark themes via MaterialTheme color schemes.
 *
 * @param onGetStarted Callback triggered when the user clicks the "Get Started" button.
 */
@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit
) {
    LockScreenOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background Image
        Image(
            painter = painterResource(id = DesignR.drawable.onboarding_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Language Selector (Top Right)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = TravelinDimens.PaddingLarge, end = TravelinDimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "English ", //  TODO: Implement dynamic language selection
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
            Icon(
                imageVector = AngleRightIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(TravelinDimens.IconSizeExtraSmall)
                    .rotate(90f) // Rotated to point downwards (⌵)
            )
        }

        // 3. Central Brand Identity (Positioned above the boat)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 350.dp),
            contentAlignment = Alignment.Center
        ) {
            TravelinLogo(modifier = Modifier.size(TravelinDimens.ImageSizeExtraLarge))
        }

        // 4. Ambient Gradient Shadow
        // Provides visual separation between the background image and the interactive card

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(290.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = 350f // adjust to fit shadow over the card
                    )
                )
        )

        // 5. Bottom Interactive Card
        // Uses surface color to automatically adapt to Light/Dark modes
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(270.dp)
                .shadow(
                    elevation = TravelinDimens.ElevationMedium,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    clip = true,
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = MaterialTheme.colorScheme.surface //background color based on theming
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = TravelinDimens.PaddingExtraLarge, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = DesignR.string.onboarding_title),
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        lineHeight = 36.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))


                // Action Button
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("onboarding_get_started_button")    ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = DesignR.string.onboarding_button),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = DesignR.drawable.ic_plane),
                            contentDescription = null,
                            modifier = Modifier.size(TravelinDimens.IconSizeLarge)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview for the Onboarding Screen in Light Mode.
 */
@Preview(showBackground = true, name = "Onboarding - Light")
@Composable
fun OnboardingScreenPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        OnboardingScreen(onGetStarted = {})
    }
}

/**
 * Preview for the Onboarding Screen in Dark Mode.
 */
@Preview(showBackground = true, name = "Onboarding - Dark")
@Composable
fun OnboardingScreenDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        OnboardingScreen(onGetStarted = {})
    }
}