package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.softserveacademy.core.domain.model.Poi
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelCloseIconButton
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelIcon
import com.softserveacademy.core.presentation.design_system.theme.LocationMarkerIcon
import com.softserveacademy.core.presentation.design_system.theme.TimeIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * A dialog component that displays all information about a Point of Interest (POI).
 *
 * @param poi The [Poi] object containing name, type, travel time, image, and description.
 * @param onDismiss Callback invoked when the dialog is dismissed or the close button is clicked.
 * @param modifier Modifier for the dialog's content surface.
 */
@Composable
fun TravelPoiDialog(
    poi: Poi,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .padding(TravelinDimens.PaddingMedium)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(TravelinDimens.PaddingMedium)
                ) {
                    poi.imageUrl?.let {
                        TravelImageHandler(
                            image = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = MaterialTheme.shapes.large
                        )

                        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                    }

                    Text(
                        text = poi.name,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
                    ) {
                        PoiIconText(
                            icon = LocationMarkerIcon,
                            label = stringResource(R.string.feat_hotel_poi_type_label),
                            value = poi.type
                        )
                        PoiIconText(
                            icon = TimeIcon,
                            label = stringResource(R.string.feat_hotel_poi_travel_time_label),
                            value = poi.travelTime
                        )
                    }

                    Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                    poi.description?.let {
                        Text(
                            text = stringResource(R.string.feat_hotel_about_label),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TravelCloseIconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(TravelinDimens.PaddingMedium)
                )
            }
        }
    }
}

/**
 * A helper component to display a label-value pair with an icon.
 */
@Composable
private fun PoiIconText(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TravelIcon(icon)

        Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun TravelPoiDialogPreview() {
    Travelin2026ProjectLabTheme {
        TravelPoiDialog(
            poi = Poi(
                name = "Eiffel Tower",
                type = "Monument",
                travelTime = "15 min",
                description = "The Eiffel Tower is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower.",
                imageUrl = null
            ),
            onDismiss = {}
        )
    }
}
