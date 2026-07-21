package com.softserveacademy.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.home.presentation.R.string

/**
 * Displays an error message and a retry button when data fetching fails.
 *
 * @param message The error message to display.
 * @param onRetry The action to perform when the retry button is clicked.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun TravelHotelDetailError(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message ?: stringResource(id = string.unexpected_error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
        TravelPrimaryButton(
            onClick = onRetry,
            text = stringResource(id = string.retry_label),
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HotelDetailErrorPreview() {
    Travelin2026ProjectLabTheme {
        TravelHotelDetailError(
            message = "Unable to load hotel details. Please try again.",
            onRetry = {}
        )
    }
}