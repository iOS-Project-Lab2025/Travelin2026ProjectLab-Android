package com.softserveacademy.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onNavigateBack: () -> Unit
) {
    ForgotPasswordContent(
        email = viewModel.email,
        onEmailChange = { viewModel.email = it },
        isLoading = viewModel.isLoading,
        error = viewModel.error,
        isSuccess = viewModel.isSuccess,
        onRecoverClick = { viewModel.onRecoverClick() },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    isSuccess: Boolean,
    onRecoverClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(TravelinDimens.PaddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = AngleLeftIcon, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
        ) {
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Enter your email account for a password recovery",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
        ) {
            Text(text = "Email", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = { Text("Enter your email address") },
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.small
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
            )
        }

        if (isSuccess) {
            Text(
                text = "Recovery email sent successfully!",
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
            )
        }

        Button(
            onClick = onRecoverClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ButtonHeightLarge),
            shape = shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4)
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Recover Password", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordContent(
        email = "",
        onEmailChange = {},
        isLoading = false,
        error = null,
        isSuccess = false,
        onRecoverClick = {},
        onNavigateBack = {}
    )
}
