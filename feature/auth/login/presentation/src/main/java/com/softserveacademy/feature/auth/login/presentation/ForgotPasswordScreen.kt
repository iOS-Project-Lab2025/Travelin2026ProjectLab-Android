package com.softserveacademy.feature.auth.login.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onNavigateBack: () -> Unit,
    navonRecoverClick: () -> Unit
) {
    ForgotPasswordContent(
        email = viewModel.email,
        onEmailChange = { viewModel.email = it },
        isLoading = viewModel.isLoading,
        error = viewModel.error,
        isSuccess = viewModel.isSuccess,
        onRecoverClick = { viewModel.onRecoverClick() },
        onNavigateBack = onNavigateBack,
        navonRecoverClick = navonRecoverClick
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
    onNavigateBack: () -> Unit,
    navonRecoverClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(TravelinDimens.PaddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall) // Consistent spacing
    ) {
        TravelIconButton(
            icon = ArrowLeftIcon,
            onClick = onNavigateBack,
            contentDescription = "Back"
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = "Forgot password",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter your email account for a password recovery",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        Text(text = "Email", style = MaterialTheme.typography.titleLarge)
        AppTextInput(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (isSuccess) {
            Text(
                text = "Recovery email sent successfully!",
                color = Green50,
                style = MaterialTheme.typography.bodySmall
            )
        }

        TravelAuthPrimaryButton(
            text = "Recover Password",
            onClick = navonRecoverClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    Travelin2026ProjectLabTheme {
        ForgotPasswordContent(
            email = "",
            onEmailChange = {},
            isLoading = false,
            error = null,
            isSuccess = false,
            onRecoverClick = {},
            onNavigateBack = {},
            navonRecoverClick = {}
        )
    }
}
