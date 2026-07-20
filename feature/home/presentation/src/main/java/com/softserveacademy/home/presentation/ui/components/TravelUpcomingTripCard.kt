package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.Green50
import com.softserveacademy.core.presentation.design_system.theme.PlaneLandIcon
import com.softserveacademy.core.presentation.design_system.theme.PlaneTakeoffIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.model.UpcomingTripUi

/**
 * Boarding-pass style card that shows the user's next upcoming trip.
 *
 * This is a "dumb" / presentational composable: it only renders the data it
 * receives through [trip]. It does not know about ViewModels, repositories or
 * loading/error states — that responsibility belongs to whoever consumes this
 * composable (e.g. a future HomeViewModel exposing HomeUiState.upcomingTrip,
 * the same way HorizontalCardState wraps HorizontalCard).
 *
 * @param trip The upcoming trip data to display.
 * @param modifier Modifier applied to the root Card.
 * @param onClick Optional callback invoked when the card is tapped (e.g. to open
 * the trip detail screen). Pass null to make the card non-interactive.
 */
@Composable
fun TravelUpcomingTripCard(
    trip: UpcomingTripUi,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    val cardShape = remember(density) {
        BoardingPassShape(
            cornerRadius = 20.dp,
            notchRadius = 10.dp,
            notchHeightFraction = 0.78f,
            density = density
        )
    }

    Card(
        shape = cardShape,
        elevation = CardDefaults.cardElevation(TravelinDimens.ElevationMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
    ) {
        Column(
            modifier = Modifier.padding(
                start = TravelinDimens.PaddingMedium,
                end = TravelinDimens.PaddingMedium,
                top = TravelinDimens.PaddingMedium,
                bottom = TravelinDimens.PaddingSmall
            )
        ) {
            // Status badge + date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(text = trip.status)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = trip.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

            // Route: origin -> duration -> destination
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoutePoint(
                    code = trip.originCode,
                    time = trip.originTime,
                    icon = PlaneTakeoffIcon
                )

                DurationConnector(
                    duration = trip.duration,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = TravelinDimens.PaddingSmall)
                )

                RoutePoint(
                    code = trip.destinationCode,
                    time = trip.destinationTime,
                    icon = PlaneLandIcon,
                    alignEnd = true
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            Text(
                text = "${trip.airline} • ${trip.travelClass} • ${trip.flightType}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

            PerforatedDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = TravelinDimens.PaddingNormal),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Booking ID",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = trip.bookingId,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Small pill-shaped label used to highlight the current trip [status]
 * (e.g. "Upcoming", "Completed", "Cancelled").
 *
 * Purely presentational: it does not map status strings to colors, it always
 * renders with the same accent color ([Green50]) at low-alpha background.
 *
 * @param text The status label to display inside the badge.
 */
@Composable
private fun StatusBadge(text: String) {
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

/**
 * Renders one end of the flight route: an airport [code], its departure/arrival
 * [time] and a directional [icon] (take-off or landing).
 *
 * When [alignEnd] is `false` (origin side) the icon is placed after the code
 * and content is left-aligned. When [alignEnd] is `true` (destination side)
 * the icon is placed before the code and content is right-aligned, so the
 * whole route row reads visually as "origin -> destination".
 *
 * @param code The IATA airport code (e.g. "CGK").
 * @param time The formatted departure or arrival time (e.g. "05:30").
 * @param icon The icon representing take-off or landing.
 * @param alignEnd Whether this RoutePoint represents the destination
 * (right-aligned) instead of the origin (left-aligned). Defaults to `false`.
 */
@Composable
private fun RoutePoint(
    code: String,
    time: String,
    icon: ImageVector,
    alignEnd: Boolean = false
) {
    Column(horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!alignEnd) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(TravelinDimens.IconSizeSmall)
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(TravelinDimens.IconSizeSmall)
                )
                Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                Text(
                    text = code,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Middle section of the route row: shows the flight [duration] above a dashed
 * line ending in a right-pointing arrow icon, visually connecting the origin
 * and destination [RoutePoint]s.
 *
 * @param duration The formatted flight duration (e.g. "1h 30m").
 * @param modifier Modifier applied to the root Column, typically used to make
 * this connector take up the remaining horizontal space between the two
 * RoutePoints via `Modifier.weight(1f)`.
 */
@Composable
private fun DurationConnector(duration: String, modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = duration,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(TravelinDimens.PaddingExtraSmall))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
            ) {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                )
            }
            Icon(
                imageVector = AngleRightIcon,
                contentDescription = null,
                tint = lineColor,
                modifier = Modifier.size(TravelinDimens.IconSizeExtraSmall)
            )
        }
    }
}

/**
 * Draws a full-width dashed horizontal line resembling the perforated tear
 * line of a physical boarding pass, used to separate the flight-info section
 * from the booking ID section of [TravelUpcomingTripCard].
 *
 * Takes no parameters: color is derived from [MaterialTheme] so it adapts
 * automatically to light/dark theme.
 */
@Composable
private fun PerforatedDivider() {
    val dividerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = dividerColor,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
        )
    }
}

/**
 * Custom [Shape] that draws a rounded card with two semi-circular notches cut
 * out of the left and right edges, at [notchHeightFraction] of the total
 * height — the classic "boarding pass" cutout.
 *
 * @param cornerRadius Radius applied to the four outer corners of the card.
 * @param notchRadius Radius of each semi-circular notch cut into the left and
 * right edges.
 * @param notchHeightFraction Vertical position of the notches, expressed as a
 * fraction (0f..1f) of the total card height. `0.78f` places them near the
 * bottom, right above the booking-id / perforated-divider section.
 * @param density The [Density] used to convert [cornerRadius] and
 * [notchRadius] from Dp to pixels. Captured at construction time via
 * `LocalDensity.current` rather than relying on the `density` parameter of
 * [createOutline], since the shape is `remember`-ed once per composition.
 */
private class BoardingPassShape(
    private val cornerRadius: Dp,
    private val notchRadius: Dp,
    private val notchHeightFraction: Float,
    private val density: Density
) : Shape {

    /**
     * Builds the outline [Path] for this shape given the final layout [size].
     *
     * The resulting path traces the card's rounded rectangle while carving
     * two inward semi-circles out of the left and right edges at
     * `size.height * notchHeightFraction`, producing the boarding-pass
     * "ticket stub" silhouette.
     *
     * @param size The final size of the shape, in pixels.
     * @param layoutDirection The layout direction (LTR/RTL). Not used, since
     * the notch cutout is symmetric and applies identically to both edges
     * regardless of direction.
     * @param density Ignored in favor of the [density] captured in the
     * constructor; see the class-level KDoc for why.
     * @return An [Outline.Generic] wrapping the computed [Path].
     */
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val corner = with(this.density) { cornerRadius.toPx() }
        val notch = with(this.density) { notchRadius.toPx() }
        val notchY = size.height * notchHeightFraction
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(corner, 0f)
            lineTo(w - corner, 0f)
            arcTo(Rect(w - 2 * corner, 0f, w, 2 * corner), -90f, 90f, false)
            lineTo(w, notchY - notch)
            arcTo(Rect(w - notch, notchY - notch, w + notch, notchY + notch), -90f, -180f, false)
            lineTo(w, h - corner)
            arcTo(Rect(w - 2 * corner, h - 2 * corner, w, h), 0f, 90f, false)
            lineTo(corner, h)
            arcTo(Rect(0f, h - 2 * corner, 2 * corner, h), 90f, 90f, false)
            lineTo(0f, notchY + notch)
            arcTo(Rect(-notch, notchY - notch, notch, notchY + notch), 90f, -180f, false)
            lineTo(0f, corner)
            arcTo(Rect(0f, 0f, 2 * corner, 2 * corner), 180f, 90f, false)
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * Preview of [TravelUpcomingTripCard] with sample data, rendered in light theme.
 */
@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun TravelUpcomingTripCardPreview() {
    val sample = UpcomingTripUi(
        status = "Upcoming",
        date = "24 March 2024",
        originCode = "CGK",
        originTime = "05:30",
        destinationCode = "DPS",
        destinationTime = "06:30",
        duration = "1h 30m",
        airline = "Sentosa Air",
        travelClass = "Economy",
        flightType = "Direct",
        bookingId = "ZEEBAW"
    )
    Travelin2026ProjectLabTheme(darkTheme = false) {
        TravelUpcomingTripCard(
            trip = sample,
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}

/**
 * Preview of [TravelUpcomingTripCard] with sample data, rendered in dark theme.
 */
@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun TravelUpcomingTripCardDarkPreview() {
    val sample = UpcomingTripUi(
        status = "Upcoming",
        date = "24 March 2024",
        originCode = "CGK",
        originTime = "05:30",
        destinationCode = "DPS",
        destinationTime = "06:30",
        duration = "1h 30m",
        airline = "Sentosa Air",
        travelClass = "Economy",
        flightType = "Direct",
        bookingId = "ZEEBAW"
    )
    Travelin2026ProjectLabTheme(darkTheme = true) {
        TravelUpcomingTripCard(
            trip = sample,
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}