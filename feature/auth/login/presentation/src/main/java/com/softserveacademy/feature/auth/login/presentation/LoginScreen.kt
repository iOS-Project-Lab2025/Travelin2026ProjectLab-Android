package com.softserveacademy.feature.auth.login.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    if (viewModel.isSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    LoginContent(
        email = viewModel.email,
        onEmailChange = { viewModel.email = it },
        password = viewModel.password,
        onPasswordChange = { viewModel.password = it },
        isLoading = viewModel.isLoading,
        error = viewModel.error,
        onLoginClick = { viewModel.onLoginClick() },
        onNavigateToRegister = onNavigateToRegister,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
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
        Spacer(modifier = Modifier.height(64.dp))
        
        // Discover Logo Placeholder
        DiscoverLogo(modifier = Modifier.size(100.dp))

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

        Text(
            text = "Welcome to Discover",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Please choose your login option below",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

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

            Text(text = "Password", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = EyeIcon, contentDescription = "Toggle password visibility")
                    }
                },
                shape = shapes.small
            )

            TextButton(
                onClick = onNavigateToForgotPassword,
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF03A9F4),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = TravelinDimens.PaddingMedium)
            )
        }

        Button(
            onClick = onLoginClick,
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
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(vertical = TravelinDimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Doesn't have account on discover? ", color = Color.Gray, fontSize = 12.sp)
            TextButton(
                onClick = onNavigateToRegister,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Create Account", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DiscoverLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.width * 0.15f
        val color = Color(0xFF03A9F4)
        
        // Drawing three slanted lines/shapes that resemble the logo in the screenshot
        // Line 1
        drawLine(
            color = color,
            start = Offset(size.width * 0.2f, size.height * 0.7f),
            end = Offset(size.width * 0.5f, size.height * 0.2f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        
        // Line 2
        drawLine(
            color = color,
            start = Offset(size.width * 0.45f, size.height * 0.8f),
            end = Offset(size.width * 0.75f, size.height * 0.35f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        
        // Dot/Small line
        drawCircle(
            color = color,
            center = Offset(size.width * 0.75f, size.height * 0.75f),
            radius = strokeWidth * 0.7f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        isLoading = false,
        error = null,
        onLoginClick = {},
        onNavigateToRegister = {},
        onNavigateToForgotPassword = {}
    )
}
