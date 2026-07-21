package com.softserveacademy.feature.auth.register.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.AppPasswordInput
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.AppNumberInput
import com.softserveacademy.core.presentation.design_system.components.TravelPhoneNumberInput
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    if (viewModel.isSuccess) {
        LaunchedEffect(Unit) {
            onRegisterSuccess()
        }
    }

    RegisterContent(
        firstName = viewModel.firstName,
        onFirstNameChange = { viewModel.firstName = it },
        lastName = viewModel.lastName,
        onLastNameChange = { viewModel.lastName = it },
        countryCode = viewModel.countryCode,
        onCountryCodeChange = { viewModel.countryCode = it },
        phone = viewModel.phone,
        onPhoneChange = { viewModel.phone = it },
        age = viewModel.age,
        onAgeChange = { viewModel.age = it },
        email = viewModel.email,
        onEmailChange = { viewModel.email = it },
        password = viewModel.password,
        onPasswordChange = { viewModel.password = it },
        termsAccepted = viewModel.termsAccepted,
        onTermsAcceptedChange = { viewModel.termsAccepted = it },
        isLoading = viewModel.isLoading,
        error = viewModel.error,
        onRegisterClick = { viewModel.onRegisterClick() },
        onNavigateBack = onNavigateBack,
        onRegisterSuccess = onRegisterSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    isLoading: Boolean,
    error: String?,
    onRegisterClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(TravelinDimens.PaddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
    ) {
        TravelIconButton(
            icon = ArrowLeftIcon,
            onClick = onNavigateBack,
            contentDescription = "Back"
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = "Create account",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Get the best out of the world by creating an account",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        // Labels adjusted to be smaller and closer to inputs
        Text(
            text = "First name",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = firstName,
            onValueChange = onFirstNameChange,
            placeholder = "First name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Last name",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = lastName,
            onValueChange = onLastNameChange,
            placeholder = "Last name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Phone",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        TravelPhoneNumberInput(
            countryCode = countryCode,
            onCountryCodeChange = onCountryCodeChange,
            phoneNumber = phone,
            onPhoneNumberChange = onPhoneChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Age",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppNumberInput(
            value = age,
            onValueChange = onAgeChange,
            placeholder = "30",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Email",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Password",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppPasswordInput(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange
            )
            Text(
                text = "I accept term and condition",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        TravelAuthPrimaryButton(
            text = "Create Account",
            onClick = onRegisterClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Go back")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }
        
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Travelin2026ProjectLabTheme {
        RegisterContent(
            firstName = "",
            onFirstNameChange = {},
            lastName = "",
            onLastNameChange = {},
            countryCode = "+855",
            onCountryCodeChange = {},
            phone = "",
            onPhoneChange = {},
            age = "",
            onAgeChange = {},
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            termsAccepted = false,
            onTermsAcceptedChange = {},
            isLoading = false,
            error = null,
            onRegisterClick = {},
            onNavigateBack = {},
            onRegisterSuccess = {}
        )
    }
}
