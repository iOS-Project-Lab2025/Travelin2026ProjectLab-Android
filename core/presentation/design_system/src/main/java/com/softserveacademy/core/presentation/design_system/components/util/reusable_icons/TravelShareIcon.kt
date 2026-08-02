package com.softserveacademy.core.presentation.design_system.components.util.reusable_icons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ShareIcon

@Composable
fun TravelShareIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    TravelIconButton(
        icon = ShareIcon,
        onClick = onClick,
        contentDescription = "share button",
        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        modifier = modifier
    )
}