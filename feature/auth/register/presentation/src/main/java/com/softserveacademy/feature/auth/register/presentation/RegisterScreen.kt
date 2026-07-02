package com.softserveacademy.feature.auth.register.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
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
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RegisterContent(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
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
    onNavigateBack: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

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

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Get the best out of Discover by creating an account",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        OutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = EyeIcon, contentDescription = "Toggle password visibility")
                }
            },
            shape = shapes.small
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange
            )
            Text(text = "I accept terms and condition", style = MaterialTheme.typography.bodySmall)
        }

        if (error != null) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ButtonHeightLarge),
            shape = shapes.small,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        TextButton(onClick = onNavigateBack) {
            Text(
                text = "Already have an account? Go back",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterContent(
        firstName = "",
        onFirstNameChange = {},
        lastName = "",
        onLastNameChange = {},
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
        onNavigateBack = {}
    )
}
