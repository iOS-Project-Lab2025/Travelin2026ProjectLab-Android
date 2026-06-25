package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.SocialAppleIcon
import com.softserveacademy.core.presentation.design_system.theme.SocialGoogleIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Icon button used to log in with social networks.
 *
 * @param icon The social icon to display on the button
 * @param onClick The action to perform when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param colorByTheme Whether to update the color of the icon based on the theme
 * @param contentDescription The content description for the icon
 */
@Composable
fun TravelSocialButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorByTheme: Boolean = false,
    contentDescription: String? = null,
){
    Button(
        onClick = onClick,
        modifier = modifier
            .requiredWidth(TravelinDimens.SocialButtonWidth)
            .dropShadow(
                shape = MaterialTheme.shapes.small,
                shadow = Shadow(
                    radius = 13.dp,
                    color = Color.Black.copy(0.25f),
                    offset = DpOffset(0.dp, 10.dp)

                )
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(TravelinDimens.PaddingSmall)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(TravelinDimens.IconSizeExtraLarge),
            tint = if (colorByTheme) MaterialTheme.colorScheme.onSurface else Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TravelSocialButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
        ) {
            TravelSocialButton(
                icon = SocialGoogleIcon,
                onClick = {},
                modifier = Modifier
            )
            TravelSocialButton(
                icon = SocialAppleIcon,
                onClick = {},
                modifier = Modifier,
                colorByTheme = true
            )
        }
    }
}