package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.softserveacademy.home.presentation.R
import com.softserveacademy.core.presentation.design_system.components.TravelOutlinedButton
import com.softserveacademy.core.presentation.design_system.components.TravelFeatureChip
import com.softserveacademy.core.presentation.design_system.components.util.mapToFeature
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIconButton
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Displays the features of a hotel or tour, showing them in a two-column grid.
 *
 * @param features List of string IDs representing the features.
 * @param onSeeAllClick Callback when the "See all" button is clicked.
 */
@Composable
fun TravelDetailsFeaturesSection(
    features: List<String>,
    onSeeAllClick: () -> Unit
) {
    if (features.isEmpty()) return

    val mappedFeatures = features.mapNotNull { mapToFeature(it) }
    val displayFeatures = mappedFeatures.take(6)
    val hasMore = mappedFeatures.size > 6

    Column(modifier = Modifier
        .padding(
            horizontal = TravelinDimens.PaddingLarge
        )
    ) {
        Text(
            text = stringResource(id = R.string.What_is_included_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        displayFeatures.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                rowItems.forEach { feature ->
                    TravelFeatureChip(
                        feature = feature,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        }

        if (hasMore) {
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))
            TravelOutlinedButton(
                text = stringResource(id = R.string.see_all_about_property),
                onClick = onSeeAllClick,
                contentPadding = PaddingValues(
                    horizontal = TravelinDimens.PaddingLarge,
                    vertical = TravelinDimens.PaddingSmall
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(
                    top = TravelinDimens.PaddingLarge,
                    bottom = TravelinDimens.PaddingExtraLarge
                )
        )
    }
}

/**
 * Full-screen overlay displaying all features of a hotel or tour.
 *
 * @param features Full list of the features string IDs.
 * @param onDismiss Callback to close the overlay.
 */
@Composable
fun FeaturesOverlay(
    features: List<String>,
    onDismiss: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    val mappedFeatures = features.mapNotNull { mapToFeature(it) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = TravelinDimens.PaddingLarge)
                .padding(bottom = TravelinDimens.PaddingLarge)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TravelArrowIconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = stringResource(id = R.string.What_is_included_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
                modifier = Modifier.weight(1f)
            ) {
                items(mappedFeatures.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
                    ) {
                        rowItems.forEach { feature ->
                            TravelFeatureChip(
                                feature = feature,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
