package com.softserveacademycore.presentation.ui.bookinggraph



import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
/**
 * Temporary booking details screen used to validate the navigation flow.
 *
 * This mock screen exists to verify that the Booking navigation graph and
 * destination transitions are working correctly. It will be replaced by the
 * final booking details UI during feature implementation.
 *
 * @param onSearchClick Callback invoked when navigating to the search flow. (not used)
 * @param onItemClick Callback invoked when the user requests to navigate to
 * the next destination in the booking flow.
 */
@Composable
fun DetailScreen(
    onSearchClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Booking Screen")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onItemClick() }
        ) {
            Text(text = "Next page")
        }
    }
}