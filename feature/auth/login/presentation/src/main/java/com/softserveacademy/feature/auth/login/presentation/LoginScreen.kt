package com.softserveacademy.feature.auth.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.AppPasswordInput
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
import com.softserveacademy.core.presentation.design_system.components.TravelSocialButton
import com.softserveacademy.core.presentation.design_system.components.util.buttons.AuthPrimaryButtonVariant
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
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
        onNavigateToForgotPassword = onNavigateToForgotPassword,
        onNavigateToRegister = onNavigateToRegister
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
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Background Image at the bottom
        Image(
            painter = painterResource(id = com.softserveacademy.core.presentation.design_system.R.drawable.test_place),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(300.dp),
            contentScale = ContentScale.FillWidth,
            alpha = 0.5f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TravelinDimens.PaddingMedium),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "English ",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Icon(
                    imageVector = AngleRightIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(TravelinDimens.IconSizeSmall)
                        .rotate(0f)
                )
            }

            Spacer(modifier = Modifier.height(TravelinDimens.Space2ExtraLarge))

            // Main Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TravelinDimens.PaddingLarge),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = TravelinDimens.PaddingExtraLarge,
                        vertical = 32.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraLarge)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                    append("Let's ")
                                }
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                    append("Travel")
                                }
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                    append(" you in.")
                                }
                            },
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Discover the World with Every\nSign In",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)) {
                        AppTextInput(
                            value = email,
                            onValueChange = onEmailChange,
                            placeholder = "Email or Phone Number",
                            modifier = Modifier.fillMaxWidth(),
                            state = if (error != null) AppInputState.Error else AppInputState.Normal,
                            errorMessage = error
                        )

                        AppPasswordInput(
                            value = password,
                            onValueChange = onPasswordChange,
                            placeholder = "Password",
                            modifier = Modifier.fillMaxWidth(),
                            state = if (error != null) AppInputState.Error else AppInputState.Normal
                        )

                        Text(
                            text = "Forgot password?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable { onNavigateToForgotPassword() }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge)) {
                        TravelAuthPrimaryButton(
                            text = "Sign In",
                            onClick = onLoginClick,
                            enabled = !isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(TravelinDimens.ButtonHeightLarge)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceLarge),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "or sign in with",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TravelSocialButton(
                                    icon = SocialGoogleIcon,
                                    onClick = {},
                                    modifier = Modifier.height(TravelinDimens.SocialButtonWidth)
                                )
                                Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))
                                TravelSocialButton(
                                    icon = SocialAppleIcon,
                                    onClick = {},
                                    colorByTheme = true,
                                    modifier = Modifier.height(TravelinDimens.SocialButtonWidth)
                                )
                                Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))
                                TravelSocialButton(
                                    icon = SocialFacebookIcon,
                                    onClick = {},
                                    modifier = Modifier.height(TravelinDimens.SocialButtonWidth)
                                )
                            }
                        }
                    }

                    Text(
                        text = "I don't have a account?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

            // Sign Up Section
            TravelAuthPrimaryButton(
                text = "Sign Up",
                onClick = onNavigateToRegister,
                variant = AuthPrimaryButtonVariant.ColorContent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = TravelinDimens.PaddingLarge)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )

            Spacer(modifier = Modifier.height(TravelinDimens.PaddingLarge))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Travelin2026ProjectLabTheme {
        LoginContent(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            isLoading = false,
            error = null,
            onLoginClick = {},
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenErrorPreview() {
    Travelin2026ProjectLabTheme {
        LoginContent(
            email = "wrong@email.com",
            onEmailChange = {},
            password = "password",
            onPasswordChange = {},
            isLoading = false,
            error = "The email or password is incorrect, please try again",
            onLoginClick = {},
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {}
        )
    }
}
