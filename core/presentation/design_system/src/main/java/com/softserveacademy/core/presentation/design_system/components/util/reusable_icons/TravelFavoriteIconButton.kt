package com.softserveacademy.core.presentation.design_system.components.util.reusable_icons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.HeartFilledIcon
import com.softserveacademy.core.presentation.design_system.theme.HeartLineIcon

@Composable
fun TravelFavoriteIconButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {}
){
    TravelIconButton(
        icon = if (isFavorite) HeartFilledIcon else HeartLineIcon,
        onClick = onClick,
        iconColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.primary,
        contentDescription = "favorite button",
        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        modifier = modifier
    )
}
