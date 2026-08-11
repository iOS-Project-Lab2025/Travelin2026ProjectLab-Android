package com.softserveacademy.core.presentation.design_system.components.util.reusable_icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelIcon(
    icon : ImageVector,
    modifier: Modifier = Modifier,
    description : String = "icon",
    iconColor : Color = MaterialTheme.colorScheme.primary,
    size : Dp = TravelinDimens.IconSizeSmall
){
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = iconColor,
        modifier = modifier
            .size(size)
    )
}