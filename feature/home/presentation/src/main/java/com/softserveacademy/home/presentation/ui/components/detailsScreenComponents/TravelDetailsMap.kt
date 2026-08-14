package com.softserveacademy.home.presentation.ui.components.detailsScreenComponents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.softserveacademy.core.presentation.design_system.R
import com.softserveacademy.core.presentation.design_system.components.util.reusable_icons.TravelArrowIconButton
import com.softserveacademy.core.presentation.design_system.theme.LocationMarkerIcon
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

/**
 * Displays the location section of the hotel, including the address and a Google Map.
 * The map's theme automatically adjusts to match the system's dark or light mode.
 *
 * @param address The formatted address string of the hotel.
 * @param latitude The latitude coordinate of the hotel.
 * @param longitude The longitude coordinate of the hotel.
 * @param onMapClick Callback when the map preview is clicked.
 */
@Composable
fun TravelDetailsMap(
    address : String,
    latitude : Double,
    longitude : Double,
    isDarkTheme: Boolean,
    onMapClick: () -> Unit
){
    val hotelCoordinates = LatLng(latitude, longitude)
    val context = LocalContext.current
    val mapProperties = remember(isDarkTheme) {
        MapProperties(
            mapStyleOptions = if (isDarkTheme) {
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_dark
                )
            } else {
                null
            }
        )
    }

    Column(modifier = Modifier
        .padding(
            horizontal = TravelinDimens.PaddingLarge
        )
    ) {
        Text(
            text = stringResource(id = R.string.Where_it_is_label),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryFixed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Text(
            text = address,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .shadow(TravelinDimens.ElevationSmall)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                    MaterialTheme.shapes.medium
                )
                .clickable { onMapClick() }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(hotelCoordinates, 16f)
                },
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = false,
                    zoomGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    zoomControlsEnabled = false
                ),
                onMapClick = { onMapClick() }
            ) {
                MarkerComposable(
                    state = rememberUpdatedMarkerState(position = hotelCoordinates)
                ) {
                    Icon(
                        imageVector = LocationMarkerIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(TravelinDimens.IconSizeExtraLarge)
                    )
                }
            }
        }
    }
}

/**
 * Displays a full-screen version of the map where the user can navigate and interact.
 *
 * @param hotelCoordinates The coordinates to center the map on.
 * @param isDarkTheme Whether the map should use the dark theme.
 * @param onDismiss Action to perform to close the full-screen view.
 */
@Composable
fun MapOverlay(
    hotelCoordinates: LatLng,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    // BackHandler is active only when the overlay is visible
    BackHandler(onBack = onDismiss)

    val context = LocalContext.current
    val mapProperties = remember(isDarkTheme) {
        MapProperties(
            mapStyleOptions = if (isDarkTheme) {
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_dark
                )
            } else {
                null
            }
        )
    }

    // Camera position state that persists across rotation
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(hotelCoordinates, 16f)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true
                ),
                contentPadding = PaddingValues(
                    top = TravelinDimens.PaddingMedium,
                    bottom = TravelinDimens.Padding2ExtraLarge
                )
            ) {
                MarkerComposable(
                    state = rememberUpdatedMarkerState(position = hotelCoordinates)
                ) {
                    Icon(
                        imageVector = LocationMarkerIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(TravelinDimens.IconSizeExtraLarge)
                    )
                }
            }

            TravelArrowIconButton(
                modifier = Modifier
                    .padding(TravelinDimens.PaddingMedium),
                onClick = onDismiss
            )
        }
    }
}