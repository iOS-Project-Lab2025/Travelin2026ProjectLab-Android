package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softserveacademy.core.presentation.design_system.theme.Green50
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A small green pill-shaped badge used to display a status label.
 *
 * Styled with green text on a semi-transparent green background
 * and fully rounded corners. Used throughout trip detail screens
 * for status indicators such as "Confirmed" or "Upcoming".
 *
 * @param text The status text to display inside the badge.
 */
@Composable
fun TravelStatusBadge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = Green50,
        modifier = Modifier
            .background(
                color = Green50.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = TravelinDimens.PaddingNormal,
                vertical = TravelinDimens.Padding2ExtraSmall
            )
    )
}
