package com.softserveacademy.feature.booking.common.presentation.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.PrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.SuccessIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.R

@Composable
fun TravelBookingSuccessScreen(
    onBackToHome: () -> Unit,
    initialAnimationState: Boolean = false
) {
    var startAnimation by remember { mutableStateOf(initialAnimationState) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "ScaleAnimation"
    )

    LaunchedEffect(Unit) {
        if (!initialAnimationState) {
            startAnimation = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(TravelinDimens.PaddingMedium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-60).dp) // Centers text vertically and moves it slightly up
        ) {
            Icon(
                imageVector = SuccessIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

            Text(
                text = stringResource(R.string.booking_success_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

            Text(
                text = stringResource(R.string.booking_success_subtitle),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }

        TravelPrimaryButton(
            text = stringResource(R.string.booking_success_button),
            onClick = onBackToHome,
            variant = PrimaryButtonVariant.BackToHome,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    TravelinDimens.PaddingExtraLarge,
                    TravelinDimens.Padding2ExtraLarge
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TravelBookingSuccessScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingSuccessScreen(
            onBackToHome = {},
            initialAnimationState = true
        )
    }
}
