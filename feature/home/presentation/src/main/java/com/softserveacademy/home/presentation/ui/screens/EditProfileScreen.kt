package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.*
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.home.presentation.state.EditProfileState
import com.softserveacademy.home.presentation.viewmodel.EditProfileViewModel

/**
 * Screen for editing the user's profile information.
 */
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val state = viewModel.state

    LaunchedEffect(state) {
        if (state is EditProfileState.UpdateSuccess) {
            kotlinx.coroutines.delay(1500)
            onSaveSuccess()
        }
    }

    EditProfileContent(
        state = state,
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
        location = viewModel.location,
        onLocationChange = { viewModel.location = it },
        password = viewModel.password,
        onPasswordChange = { viewModel.password = it },
        confirmPassword = viewModel.confirmPassword,
        onConfirmPasswordChange = { viewModel.confirmPassword = it },
        hasChanges = viewModel.hasChanges(),
        onSaveChanges = viewModel::onSaveChanges,
        onClearError = viewModel::clearError,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun EditProfileContent(
    state: EditProfileState,
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
    location: String,
    onLocationChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    hasChanges: Boolean,
    onSaveChanges: () -> Unit,
    onClearError: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = TravelinDimens.PaddingLarge, vertical = TravelinDimens.PaddingSmall)
            ) {
                TravelIconButton(
                    icon = ArrowLeftIcon,
                    onClick = onNavigateBack,
                    contentDescription = "Back"
                )
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))
                Text(
                    text = "Edit profile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = TravelinDimens.ElevationLarge
            ) {
                Button(
                    onClick = onSaveChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(TravelinDimens.PaddingLarge)
                        .height(TravelinDimens.ButtonHeightLarge),
                    shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                    enabled = hasChanges && state !is EditProfileState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Teal40,
                        contentColor = Color.White,
                        disabledContainerColor = Teal40_Alpha50
                    )
                ) {
                    if (state is EditProfileState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Save changes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = TravelinDimens.PaddingLarge),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)
            ) {
                LabeledInput(label = "First name") {
                    AppTextInput(
                        value = firstName,
                        onValueChange = onFirstNameChange,
                        placeholder = "First name"
                    )
                }

                LabeledInput(label = "Last name") {
                    AppTextInput(
                        value = lastName,
                        onValueChange = onLastNameChange,
                        placeholder = "Last name"
                    )
                }

                LabeledInput(label = "Phone") {
                    TravelPhoneNumberInput(
                        countryCode = countryCode,
                        onCountryCodeChange = onCountryCodeChange,
                        phoneNumber = phone,
                        onPhoneNumberChange = onPhoneChange,
                        placeholder = "Phone number"
                    )
                }

                LabeledInput(label = "Age") {
                    AppNumberInput(
                        value = age,
                        onValueChange = onAgeChange,
                        placeholder = "Age"
                    )
                }

                LabeledInput(label = "Direction") {
                    AppTextInput(
                        value = location,
                        onValueChange = onLocationChange,
                        placeholder = "Direction"
                    )
                }

                LabeledInput(label = "Password") {
                    AppPasswordInput(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = "Password"
                    )
                }

                LabeledInput(label = "Confirm password") {
                    AppPasswordInput(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        placeholder = "Confirm password"
                    )
                }

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
            }

            if (state is EditProfileState.Error) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(TravelinDimens.PaddingLarge),
                    action = {
                        TextButton(onClick = onClearError) {
                            Text("Dismiss", color = MaterialTheme.colorScheme.inversePrimary)
                        }
                    }
                ) {
                    Text(text = state.message)
                }
            }

            if (state is EditProfileState.UpdateSuccess) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(TravelinDimens.PaddingLarge),
                    containerColor = Green50,
                    contentColor = Color.White
                ) {
                    Text(text = "Profile updated successfully!")
                }
            }
        }
    }
}

@Composable
fun LabeledInput(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Gray80,
            modifier = Modifier.padding(bottom = TravelinDimens.PaddingExtraSmall)
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileContentPreview() {
    Travelin2026ProjectLabTheme {
        EditProfileContent(
            state = EditProfileState.UpdateSuccess,
            firstName = "John",
            onFirstNameChange = {},
            lastName = "Doe",
            onLastNameChange = {},
            countryCode = "+1",
            onCountryCodeChange = {},
            phone = "1234567890",
            onPhoneChange = {},
            age = "25",
            onAgeChange = {},
            location = "New York",
            onLocationChange = {},
            password = "password123",
            onPasswordChange = {},
            confirmPassword = "password123",
            onConfirmPasswordChange = {},
            hasChanges = true,
            onSaveChanges = {},
            onClearError = {},
            onNavigateBack = {}
        )
    }
}
