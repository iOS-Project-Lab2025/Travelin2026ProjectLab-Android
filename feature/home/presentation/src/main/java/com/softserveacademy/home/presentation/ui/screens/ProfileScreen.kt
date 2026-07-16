package com.softserveacademy.home.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.domain.model.AppTheme
import com.softserveacademy.core.domain.model.UserProfile
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.theme.*
import com.softserveacademy.home.presentation.viewmodel.ProfileViewModel
import com.softserveacademy.home.presentation.state.ProfileState
import com.softserveacademy.home.presentation.R
import com.softserveacademy.home.presentation.ui.components.TravelNavigationBar
import kotlinx.coroutines.flow.collectLatest

/**
 * Main entry point for the Profile screen.
 * @param viewModel The [ProfileViewModel] that manages the screen state.
 * @param onNavigateBack Callback when the user clicks the back button.
 * @param onLogoutSuccess Callback when logout is successful.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onHomeClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.logoutEvent) {
        viewModel.logoutEvent.collectLatest {
            onLogoutSuccess()
        }
    }

    ProfileContent(
        state = viewModel.state,
        currentTheme = viewModel.currentTheme,
        onNavigateBack = onNavigateBack,
        onEditProfileClick = viewModel::onEditProfileClick,
        onLogoutClick = { showLogoutDialog = true },
        onThemeClick = { showThemeDialog = true },
        onRetry = viewModel::loadProfile,
        onHomeClick = onHomeClick
    )

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                showLogoutDialog = false
                viewModel.onLogoutClick()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = viewModel.currentTheme,
            onThemeSelected = {
                viewModel.onThemeSelected(it)
                showThemeDialog = false
            },
            onDismissRequest = { showThemeDialog = false }
        )
    }
}

/**
 * Dialog for confirming user logout.
 */
@Composable
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.large,
        title = {
            Text(
                text = stringResource(R.string.logout_dialog_title),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = stringResource(R.string.logout_dialog_message),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = TravelinDimens.PaddingSmall),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
            ) {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = stringResource(R.string.logout_confirm))
                }
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(TravelinDimens.SpaceSmall)
                ) {
                    Text(text = stringResource(R.string.logout_cancel))
                }
            }
        },
        dismissButton = null
    )
}

/**
 * Dialog for selecting the application theme.
 */
@Composable
private fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = MaterialTheme.shapes.large,
        title = { Text(text = stringResource(R.string.theme_dialog_title)) },
        text = {
            Column {
                ThemeOption(
                    text = stringResource(R.string.theme_light),
                    selected = currentTheme == AppTheme.LIGHT,
                    onClick = { onThemeSelected(AppTheme.LIGHT) }
                )
                ThemeOption(
                    text = stringResource(R.string.theme_dark),
                    selected = currentTheme == AppTheme.DARK,
                    onClick = { onThemeSelected(AppTheme.DARK) }
                )
                ThemeOption(
                    text = stringResource(R.string.theme_system),
                    selected = currentTheme == AppTheme.SYSTEM,
                    onClick = { onThemeSelected(AppTheme.SYSTEM) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Close")
            }
        }
    )
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * Composable that renders the content of the profile screen based on its state.
 */
@Composable
fun ProfileContent(
    state: ProfileState,
    currentTheme: AppTheme,
    onNavigateBack: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onThemeClick: () -> Unit,
    onRetry: () -> Unit,
    onHomeClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(TravelinDimens.PaddingLarge),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TravelIconButton(
                    icon = ArrowLeftIcon,
                    onClick = onNavigateBack,
                    contentDescription = "Back"
                )
            }
        },
        bottomBar = {
            TravelNavigationBar(
                selectedTab = 3,
                onTabClick = { index ->
                    if (index == 0) onHomeClick()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                is ProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileState.Success -> {
                    ProfileSuccessContent(
                        profile = state.profile,
                        currentTheme = currentTheme,
                        onEditProfileClick = onEditProfileClick,
                        onLogoutClick = onLogoutClick,
                        onThemeClick = onThemeClick
                    )
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Renders the profile details when successfully loaded.
 */
@Composable
fun ProfileSuccessContent(
    profile: UserProfile,
    currentTheme: AppTheme,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onThemeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = TravelinDimens.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Image(
                painter = painterResource(id = com.softserveacademy.core.presentation.design_system.R.drawable.test_place),
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        // Name
        Text(
            text = profile.name,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        // Location
        profile.location?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = Gray80
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        // Account Setting Header
        Text(
            text = "Account Setting",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        // Setting Items
        SettingItem(
            icon = UserCircleFilledIcon,
            title = "Edit profile",
            onClick = onEditProfileClick
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        SettingItem(
            icon = MoonIcon,
            title = "Color mode",
            onClick = onThemeClick,
            trailingText = when (currentTheme) {
                AppTheme.LIGHT -> "Light"
                AppTheme.DARK -> "Dark"
                AppTheme.SYSTEM -> "System"
            }
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraLarge))

        // Logout Button
        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(TravelinDimens.ButtonHeightMedium),
            shape = RoundedCornerShape(TravelinDimens.SpaceSmall),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleLarge,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))

        // App Version
        Text(
            text = "Version 3.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = TravelBlue40
        )
        
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
    }
}

/**
 * Helper composable for individual setting menu items.
 */
@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    trailingText: String? = null
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingMedium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(TravelinDimens.IconSizeMedium),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(TravelinDimens.SpaceMedium))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            trailingText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(TravelinDimens.SpaceSmall))
            }
            Icon(
                imageVector = AngleRightIcon,
                contentDescription = null,
                modifier = Modifier.size(TravelinDimens.IconSizeSmall),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Travelin2026ProjectLabTheme {
        ProfileContent(
            state = ProfileState.Success(
                UserProfile("John Doe", 100, "", "Mars, Solar System")
            ),
            currentTheme = AppTheme.SYSTEM,
            onNavigateBack = {},
            onEditProfileClick = {},
            onLogoutClick = {},
            onThemeClick = {},
            onRetry = {},
            onHomeClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenDarkPreview() {
    Travelin2026ProjectLabTheme {
        ProfileContent(
            state = ProfileState.Success(
                UserProfile("John Doe", 100, "", "Mars, Solar System")
            ),
            currentTheme = AppTheme.SYSTEM,
            onNavigateBack = {},
            onEditProfileClick = {},
            onLogoutClick = {},
            onThemeClick = {},
            onRetry = {},
            onHomeClick = {}
        )
    }
}
