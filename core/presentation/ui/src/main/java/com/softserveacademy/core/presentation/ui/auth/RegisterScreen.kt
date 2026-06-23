package com.softserveacademy.core.presentation.ui.auth

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
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = viewModel.age,
            onValueChange = { viewModel.age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.small
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
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
                checked = viewModel.termsAccepted,
                onCheckedChange = { viewModel.termsAccepted = it }
            )
            Text(text = "I accept terms and condition", style = MaterialTheme.typography.bodySmall)
        }

        if (viewModel.error != null) {
            Text(text = viewModel.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        Button(
            onClick = { viewModel.onRegisterClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ButtonHeightLarge),
            shape = shapes.small,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
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
    RegisterScreen(
        viewModel = RegisterViewModel(com.softserveacademy.core.domain.usecase.RegisterUseCase(object : com.softserveacademy.core.domain.repository.AuthRepository {
            override suspend fun register(user: com.softserveacademy.core.domain.model.User, password: String) = Result.success(Unit)
            override suspend fun login(email: String, password: String) = Result.success(Unit)
        })),
        onNavigateBack = {},
        onRegisterSuccess = {}
    )
}
