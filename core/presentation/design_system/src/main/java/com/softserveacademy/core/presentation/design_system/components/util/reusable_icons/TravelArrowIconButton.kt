package com.softserveacademy.core.presentation.design_system.components.util.reusable_icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelArrowIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    TravelIconButton(
        icon = ArrowLeftIcon,
        onClick = onClick,
        contentDescription = "Back button",
        backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        modifier = modifier.size(TravelinDimens.ButtonHeightSmall),
        iconSize = TravelinDimens.PaddingMedium
    )
}