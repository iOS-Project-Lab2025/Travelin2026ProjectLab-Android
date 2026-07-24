package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.theme.Bag
import com.softserveacademy.core.presentation.design_system.theme.LocationMarkerIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import kotlinx.coroutines.delay

/**
 * An upgraded travel-themed loading screen with a journey-based suitcase animation and brand logo.
 *
 * Features:
 * - Animated suitcase that "hops" between dots.
 * - Dynamic dot highlighting based on suitcase position.
 * - Rotating loading messages.
 * - Final destination marker (Location Icon).
 * - Brand logo at the bottom.
 *
 * @param modifier The modifier to be applied to the loading screen.
 */
@Composable
fun TravelLoadingScreen(
    modifier: Modifier = Modifier
) {
    val totalDots = 7
    val dotSpacing = 28.dp
    val suitcaseSize = 48.dp
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "loading_experience")
    
    // 1. Smoothly cycle through dot indices (0 to 7)
    val journeyProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = totalDots.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "journey_step"
    )

    // 2. Bounce effect (hopping between dots)
    val bounceHeight = 12f
    val bounceY = -kotlin.math.abs(Math.sin(journeyProgress.toDouble() * Math.PI)).toFloat() * bounceHeight

    // 3. Rotation effect (leaning while moving)
    val rotationAngle = kotlin.math.cos(journeyProgress.toDouble() * Math.PI * 2).toFloat() * 10f - 5f

    // 4. Rotating Messages
    val messages = listOf(
        "Preparing your journey...",
        "Finding destinations...",
        "Loading your trip...",
        "Packing the bags..."
    )
    var currentMessageIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while(true) {
            delay(2000)
            currentMessageIndex = (currentMessageIndex + 1) % messages.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.TopCenter
    ) {
        // Main Animation Column
        Column(
            modifier = Modifier
                .padding(top = 180.dp) // Offset to ~40-45% height
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Container for suitcase and dots
            Box(
                modifier = Modifier
                    .width(dotSpacing * (totalDots + 1))
                    .height(100.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                // Track of Dots
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(totalDots) { index ->
                        val isActive = journeyProgress.toInt() == index
                        val dotAlpha by animateColorAsState(
                            targetValue = if (isActive) Color.White else Color.White.copy(alpha = 0.3f),
                            label = "dot_color"
                        )
                        val dotSize = if (isActive) 10.dp else 6.dp

                        Box(
                            modifier = Modifier
                                .size(dotSize)
                                .background(dotAlpha, CircleShape)
                        )
                    }
                    
                    // Final Destination Icon (Location Pin)
                    Icon(
                        imageVector = LocationMarkerIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (journeyProgress >= totalDots - 0.5f) Color.White else Color.White.copy(alpha = 0.3f)
                    )
                }

                // Suitcase Icon
                Icon(
                    imageVector = Bag,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(
                            x = (journeyProgress * dotSpacing.value).dp, 
                            y = (bounceY - 16).dp // Elevation above dots
                        )
                        .size(suitcaseSize)
                        .rotate(rotationAngle),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Dynamic Loading Text
            Text(
                text = messages[currentMessageIndex],
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Logo at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TravelinLogo(
                modifier = Modifier.width(180.dp) // Slightly smaller than default for balance
            )
        }
    }
}

@Preview
@Composable
fun TravelLoadingScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelLoadingScreen()
    }
}
