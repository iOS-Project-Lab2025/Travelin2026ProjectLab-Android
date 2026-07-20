package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
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
                    onClick = viewModel::onSaveChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(TravelinDimens.PaddingLarge)
                        .height(TravelinDimens.ButtonHeightLarge),
                    shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                    enabled = viewModel.hasChanges() && state !is EditProfileState.Loading,
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
                EditProfileTextField(
                    value = viewModel.firstName,
                    onValueChange = { viewModel.firstName = it },
                    label = "First name"
                )

                EditProfileTextField(
                    value = viewModel.lastName,
                    onValueChange = { viewModel.lastName = it },
                    label = "Last name"
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Phone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray80,
                        modifier = Modifier.padding(bottom = TravelinDimens.PaddingExtraSmall)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
                    ) {
                        EditProfileTextField(
                            value = viewModel.countryCode,
                            onValueChange = { viewModel.countryCode = it },
                            label = "",
                            modifier = Modifier.width(100.dp)
                        )
                        EditProfileTextField(
                            value = viewModel.phone,
                            onValueChange = { viewModel.phone = it },
                            label = "",
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                }

                EditProfileTextField(
                    value = viewModel.age,
                    onValueChange = { viewModel.age = it },
                    label = "Age",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                EditProfileTextField(
                    value = viewModel.location,
                    onValueChange = { viewModel.location = it },
                    label = "Direction"
                )

                var passwordVisible by remember { mutableStateOf(false) }
                EditProfileTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = "Password",
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = EyeIcon,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )

                EditProfileTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = "Confirm password",
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
            }

            if (state is EditProfileState.Error) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(TravelinDimens.PaddingLarge),
                    action = {
                        TextButton(onClick = viewModel::clearError) {
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
fun EditProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray80,
                modifier = Modifier.padding(bottom = TravelinDimens.PaddingExtraSmall)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(TravelinDimens.SpaceMedium),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Teal40,
                unfocusedBorderColor = Gray40,
                cursorColor = Teal40
            )
        )
    }
}
