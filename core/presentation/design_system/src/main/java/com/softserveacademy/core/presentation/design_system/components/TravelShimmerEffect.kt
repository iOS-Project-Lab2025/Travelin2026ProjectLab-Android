package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.softserveacademy.core.presentation.design_system.theme.Gray40_Alpha20
import com.softserveacademy.core.presentation.design_system.theme.Gray40_Alpha60

/**
 * A modifier that applies a shimmer effect to a composable.
 * Used for skeleton loading screens.
 */
fun Modifier.shimmerEffect(): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_animation"
    )

    val shimmerColors = listOf(
        Gray40_Alpha60,
        Gray40_Alpha20,
        Gray40_Alpha60
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    background(brush)
}