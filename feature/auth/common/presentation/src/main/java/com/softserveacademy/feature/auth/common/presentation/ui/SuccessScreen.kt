package com.softserveacademy.feature.auth.common.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import com.softserveacademy.feature.auth.common.presentation.R
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.TravelAuthPrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun SuccessScreen(
    onExploreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(TravelinDimens.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        // White Logo/Icon
        DiscoverLogoWhite(modifier = Modifier.size(120.dp))
        
        Spacer(modifier = Modifier.height(TravelinDimens.Space2ExtraLarge))
        
        Text(
            text = stringResource(R.string.auth_success_title),
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        
        Text(
            text = stringResource(R.string.auth_success_description),
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        TravelAuthPrimaryButton(
            text = stringResource(R.string.auth_explore_button),
            onClick = onExploreClick,
            variant = TravelAuthPrimaryButtonVariant.ColorContent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    SuccessScreen(onExploreClick = {}
    )
}

@Composable
fun DiscoverLogoWhite(modifier: Modifier = Modifier) {
    // Reusing the logic but with white color
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.15f
        val color = Color.White
        
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.7f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.2f),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.45f, size.height * 0.8f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.75f, size.height * 0.35f),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        
        drawCircle(
            color = color,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.75f, size.height * 0.75f),
            radius = strokeWidth * 0.7f
        )
    }
}
