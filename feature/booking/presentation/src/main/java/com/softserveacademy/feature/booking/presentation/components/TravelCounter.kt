package com.softserveacademy.feature.booking.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.AddIcon
import com.softserveacademy.core.presentation.design_system.theme.MinusIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@Composable
fun TravelCounter(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minCount: Int = 0,
    maxCount: Int = 10
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
    ) {
        CounterButton(
            icon = MinusIcon,
            onClick = { if (count > minCount) onCountChange(count - 1) },
            enabled = count > minCount
        )

        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 32.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.extraSmall
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        CounterButton(
            icon = AddIcon,
            onClick = { if (count < maxCount) onCountChange(count + 1) },
            enabled = count < maxCount
        )
    }
}

@Composable
private fun CounterButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
    val contentColor = Color.White

    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview
@Composable
fun TravelCounterPreview(){
    Travelin2026ProjectLabTheme {
        TravelCounter(
            count = 1,
            onCountChange = {},
            modifier = Modifier
        )
    }
}