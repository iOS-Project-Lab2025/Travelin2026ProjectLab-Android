package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.softserveacademy.home.presentation.R
import com.softserveacademy.core.presentation.design_system.theme.AngleLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Displays the description section of the destination, with "Read more/less" functionality.
 *
 * @param description The full description text of the destination.
 * @param isExpanded Whether the description is currently expanded.
 * @param onExpandClick Callback when the expand/collapse button is clicked.
 */
@Composable
fun TravelDetailsDescription(
    description: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    var hasOverflow by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(TravelinDimens.PaddingLarge)
    ) {
        Text(
            text = stringResource(id = R.string.about_label),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Column {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (isExpanded) Int.MAX_VALUE else 6,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded) {
                        hasOverflow = textLayoutResult.hasVisualOverflow
                    }
                }
            )
            if (hasOverflow || isExpanded) {
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
                Row(
                    modifier = Modifier
                        .clickable { onExpandClick() }
                        .padding(vertical = TravelinDimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isExpanded) stringResource(id = R.string.read_less_label)
                        else stringResource(id = R.string.read_more_label),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                    Icon(
                        imageVector = if (isExpanded) AngleLeftIcon else AngleRightIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    vertical = TravelinDimens.PaddingLarge
                )
        )
    }
}