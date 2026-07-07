package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.ErrorIcon
import com.softserveacademy.core.presentation.design_system.theme.Green50
import com.softserveacademy.core.presentation.design_system.theme.InfoIcon
import com.softserveacademy.core.presentation.design_system.theme.Red50
import com.softserveacademy.core.presentation.design_system.theme.SuccessIcon
import com.softserveacademy.core.presentation.design_system.theme.Teal40
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.WarningIcon
import com.softserveacademy.core.presentation.design_system.theme.Yellow50

/**
 * Common internal component to ensure consistency across all banner types.
 * @param message The text message to be displayed.
 * @param isVisible Controls visibility with expansion/fade animation.
 * @param icon The icon displayed at the start.
 * @param baseColor Color used for icon, text, and 10% alpha background.

 */
@Composable
private fun TravelBaseInlineBanner(
    message: String,
    isVisible: Boolean,
    icon: ImageVector,
    baseColor: Color,
    modifier: Modifier = Modifier
) {
    /**
     * Allows to display softly
     */
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            // Background with 10% alpha of the base color for better contrast in Light/Dark mode
            color = baseColor.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                modifier = Modifier.padding(TravelinDimens.PaddingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = baseColor,
                    modifier = Modifier.size(TravelinDimens.IconSizeSmall)
                )
                Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))
                Text(
                    text = message,
                    color = baseColor,
                    style = MaterialTheme.typography.bodySmall // Defined as Inter Regular 10sp
                )
            }
        }
    }
}

/**
 * Renders an Error feedback banner using the Red token.
 * Used for critical errors or failed validation.
 */
@Composable
fun InlineErrorBanner(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    TravelBaseInlineBanner(
        message = message,
        isVisible = isVisible,
        icon = ErrorIcon,
        baseColor = Red50,
        modifier = modifier
    )
}

/**
 * Renders an Information feedback banner using the Teal token.
 * Used for providing additional context or guidance.
 */
@Composable
fun InlineInformationBanner(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    TravelBaseInlineBanner(
        message = message,
        isVisible = isVisible,
        icon = InfoIcon,
        baseColor = Teal40,
        modifier = modifier
    )
}

/**
 * Renders a Success feedback banner using the Green token.
 * Used for indicating successful actions or completed tasks.
 */
@Composable
fun InlineSuccessBanner(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    TravelBaseInlineBanner(
        message = message,
        isVisible = isVisible,
        icon = SuccessIcon,
        baseColor = Green50,
        modifier = modifier
    )
}

/**
 * Renders a Warning feedback banner using the Yellow token.
 * Used for indicating potential issues or cautionary information.
 */
@Composable
fun InlineWarningBanner(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    TravelBaseInlineBanner(
        message = message,
        isVisible = isVisible,
        icon = WarningIcon,
        baseColor = Yellow50,
        modifier = modifier
    )
}


// ============================================================================
// Previews
// ============================================================================


@Preview(showBackground = true, name = "Light Mode")
@Composable
fun InlineBannersPreview() {
    Travelin2026ProjectLabTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            InlineErrorBanner(message = "Email or password incorrect", isVisible = true)
            InlineInformationBanner(message = "Your password must be 8 characters long", isVisible = true)
            InlineSuccessBanner(message = "Login successful!", isVisible = true)
            InlineWarningBanner(message = "Account will be locked in 1 attempt", isVisible = true)
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun InlineBannersDarkPreview() {
    Travelin2026ProjectLabTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            InlineErrorBanner(message = "Email or password incorrect", isVisible = true)
            InlineInformationBanner(message = "Your password must be 8 characters long", isVisible = true)
            InlineSuccessBanner(message = "Login successful!", isVisible = true)
            InlineWarningBanner(message = "Account will be locked in 1 attempt", isVisible = true)
        }
    }
}
