package com.softserveacademycore.presentation.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelinLogo
import com.softserveacademy.core.presentation.design_system.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Splash Screen UI with enter/exit animations, a glowing aura, and staggered travel decorations.
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val destination by viewModel.destination.collectAsState()
    var startAnimation by remember { mutableStateOf(false) }

    // 1. Main alpha animation
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800),
        label = "alpha"
    )

    // 2. Infinite transition for staggered "one after another" effect
    val infiniteTransition = rememberInfiniteTransition(label = "staggeredEffect")

    // Slow Flicker for Bus
    val busAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.6f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Reverse),
        label = "busAlpha"
    )

    // Medium Flicker for Flight
    val flightAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "flightAlpha"
    )

    // Fast Flicker for Bed
    val bedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 0.5f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "bedAlpha"
    )

    // Breathing glow scale
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1.2f, targetValue = 1.6f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glowScale"
    )

    LaunchedEffect(Unit) { startAnimation = true }

    LaunchedEffect(destination) {
        if (destination != null) {
            startAnimation = false
            delay(400.milliseconds)
            when (destination) {
                is SplashDestination.Onboarding -> onNavigateToOnboarding()
                is SplashDestination.Home -> onNavigateToHome()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        // --- AMBIENT LIGHTING (SOFT GLOW) ---
        // Increased size and blur to avoid the "isolated circle" look
        Box(
            modifier = Modifier
                .size(350.dp)
                .graphicsLayer(scaleX = glowScale, scaleY = glowScale, alpha = alphaAnim * 0.3f)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
                .blur(80.dp)
        )

        // --- STAGGERED TRAVEL DECORATIONS ---
        if (startAnimation) {
            Box(Modifier.size(280.dp)) {
                // Top-Left: Bus (Slow appearance)
                Icon(
                    painter = painterResource(id = R.drawable.ic_bus),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(40.dp)
                        .alpha(busAlpha),
                    tint = Color.White
                )
                // Top-Right: Flight (Fastest appearance)
                Icon(
                    painter = painterResource(id = R.drawable.ic_flight),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(55.dp)
                        .alpha(flightAlpha),
                    tint = Color.White
                )
                // Bottom-Left: Bed (Medium appearance)
                Icon(
                    painter = painterResource(id = R.drawable.ic_bed),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(45.dp)
                        .alpha(bedAlpha),
                    tint = Color.White
                )
            }
        }

        // --- MAIN CONTENT (LOGO + TAGLINE) ---
        // We use a Column to put the Tagline below the Logo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer(alpha = alphaAnim)
        ) {
            TravelinLogo()

            Spacer(modifier = Modifier.height((-50).dp))

            Text(
                text = stringResource(id = R.string.splash_tagline),
                style = MaterialTheme.typography.bodyLarge, // Inter Regular 14sp
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
