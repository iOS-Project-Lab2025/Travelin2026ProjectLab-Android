package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.softserveacademy.core.presentation.design_system.R as CoreR
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.core.presentation.design_system.theme.Yellow50
import com.softserveacademy.home.presentation.model.UserProfileUi
import java.text.NumberFormat

/**
 * Displays the user's profile summary at the top of the home screen.
 *
 * <p>Shows the user's name, a points badge (yellow circle with diamond), the
 * points value, and a circular avatar image aligned to the right.</p>
 *
 * @param userProfile The user profile data to display.
 * @param onClick Callback invoked when the card is tapped.
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
fun TravelUserProfileCard(
    userProfile: UserProfileUi,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pointsFormatted = NumberFormat.getIntegerInstance().format(userProfile.points)

    Row(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = userProfile.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Yellow50, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .rotate(45f)
                            .background(Yellow50)
                    )
                }
                Spacer(modifier = Modifier.width(TravelinDimens.PaddingExtraSmall))
                Text(
                    text = pointsFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Yellow50
                )
                Spacer(modifier = Modifier.width(TravelinDimens.SpaceExtraSmall))
                Text(
                    text = "points",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Yellow50
                )
            }
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = userProfile.avatarUrl,
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(CoreR.drawable.test_place),
                placeholder = painterResource(CoreR.drawable.test_place)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelUserProfileCardPreview() {
    Travelin2026ProjectLabTheme {
        TravelUserProfileCard(
            userProfile = UserProfileUi(
                name = "John Doe",
                points = 2000,
                avatarUrl = "https://i.pravatar.cc/300"
            ),
            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
        )
    }
}
