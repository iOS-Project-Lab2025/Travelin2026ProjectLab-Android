package com.softserveacademycore.presentation.ui.maingraph
/**
 * Temporary home screen used to validate the main navigation flow.
 *
 * This mock screen is intended to verify that the Main navigation graph
 * and its destinations are correctly configured. It serves as a placeholder
 * until the final Home screen implementation is completed.
 *
 * @param onSearchClick Callback invoked when navigating to the search flow.
 * @param onItemClick Callback invoked when navigating to the next destination
 * within the application flow.
 */

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
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

        Text(text = "Home Screen")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onItemClick() }
        ) {
            Text(text = "Next page")
        }
    }
}