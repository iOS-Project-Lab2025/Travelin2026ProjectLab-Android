package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.HeartFilledIcon
import com.softserveacademy.core.presentation.design_system.theme.HeartLineIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Simple circle icon button.
 *
 * @param icon The icon to display on the button
 * @param onClick The action to perform when the button is clicked
 * @param modifier The modifier to apply to the button
 * @param enabled Whether the button is enabled or disabled
 * @param contentDescription The content description for the icon
 * @param iconColor The color of the icon
 */
@Composable
fun TravelIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconColor : Color = MaterialTheme.colorScheme.onSurface,
    showBackground: Boolean = true,
    contentDescription: String? = null,
){
    val containerColor = if (showBackground) MaterialTheme.colorScheme.surface else Color.Transparent
    IconButton (
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor,
        )
    ){
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(TravelinDimens.IconSizeSmall),
            tint = iconColor
        )
    }
}

@Preview
@Composable
fun TravelIconButtonPreview() {
    Travelin2026ProjectLabTheme {
        Column(
            modifier = Modifier.padding(TravelinDimens.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
        ) {
            TravelIconButton(
                icon = HeartLineIcon,
                onClick = {},
                modifier = Modifier
            )
            TravelIconButton(
                icon = HeartFilledIcon,
                onClick = {},
                modifier = Modifier,
                iconColor = MaterialTheme.colorScheme.error
            )
            TravelIconButton(
                icon = ArrowLeftIcon,
                onClick = {},
                modifier = Modifier,
                showBackground = false
            )
        }
    }
}