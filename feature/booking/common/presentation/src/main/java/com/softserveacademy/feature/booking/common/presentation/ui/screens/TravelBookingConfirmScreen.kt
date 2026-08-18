package com.softserveacademy.feature.booking.common.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.domain.util.formatPrice
import com.softserveacademy.core.presentation.design_system.components.TravelPrimaryButton
import com.softserveacademy.core.presentation.design_system.theme.ArrowLeftIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.R
import com.softserveacademy.core.presentation.design_system.components.TravelErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelBookingConfirmScreen(
    totalPrice: Double,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    isConfirmLoading: Boolean = false,
    error: String? = null,
    onRetryClick: () -> Unit = {},
    onDismissError: () -> Unit = onBackClick,
    content: @Composable (PaddingValues) -> Unit
) {
    if (error != null) {
        TravelErrorScreen(
            message = error,
            onRetry = onRetryClick,
            onBackClick = onDismissError
        )
    } else {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.booking_confirm_screen_title),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(TravelinDimens.PaddingMedium)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = ArrowLeftIcon,
                                contentDescription = stringResource(R.string.back_button_label)
                            )
                        }
                    },
                    windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
                )
            },
            bottomBar = {
                TravelBookingConfirmBottomBar(
                    totalPrice = totalPrice,
                    onButtonClick = onConfirmClick,
                    isLoading = isConfirmLoading
                )
            }
        ) { padding ->
            content(padding)
        }
    }
}

@Composable
fun TravelBookingConfirmBottomBar(
    totalPrice: Double,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(TravelinDimens.PaddingMedium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val totalInfo = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                ) {
                    append(stringResource(R.string.booking_confirm_total_label))
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                ) {
                    append(" $${formatPrice(totalPrice)}")
                }
            }

            Text(
                text = totalInfo,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.weight(1f)
            )
            TravelPrimaryButton(
                text = stringResource(R.string.booking_confirm_button_label),
                onClick = onButtonClick,
                isLoading = isLoading,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
@Preview
fun TravelBookingConfirmScreenPreview() {
    Travelin2026ProjectLabTheme() {
        TravelBookingConfirmScreen(
            totalPrice = 100.0,
            onBackClick = {},
            onConfirmClick = {},
            content = {
                Text(text = "Content")
            }
        )
    }
}
