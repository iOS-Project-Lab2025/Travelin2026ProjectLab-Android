package com.softserveacademy.feature.booking.hotel.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.collectLatest
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelPhoneNumberInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.hotel.presentation.viewmodel.HotelContactInfoViewModel
import com.softserveacademy.feature.booking.hotel.presentation.events.HotelContactInfoEvent
import com.softserveacademy.feature.booking.hotel.presentation.states.HotelContactInfoState
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.common.presentation.R

@Composable
fun HotelContactInfoScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    viewModel: HotelContactInfoViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.validationSuccess) {
        viewModel.validationSuccess.collectLatest { success ->
            if (success) {
                onNextClick()
                viewModel.resetValidationStatus()
            }
        }
    }

    HotelContactInfoContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun HotelContactInfoContent(
    state: HotelContactInfoState,
    onEvent: (HotelContactInfoEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            TravelBookingBottomBar(
                onBackClick = onBackClick,
                onNextClick = { onEvent(HotelContactInfoEvent.OnNextClick) }
            )
        },
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(TravelinDimens.PaddingMedium)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceMedium)
            ) {
                Text(
                    text = stringResource(id = R.string.contact_info_title),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.contact_info_who_check_in),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

                // First Name
                Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
                    Text(
                        text = stringResource(id = R.string.contact_info_first_name),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    AppTextInput(
                        value = state.firstName,
                        onValueChange = { onEvent(HotelContactInfoEvent.FirstNameChanged(it)) },
                        placeholder = stringResource(id = R.string.contact_info_first_name_placeholder),
                        modifier = Modifier.fillMaxWidth(),
                        state = if (state.firstNameError != null) AppInputState.Error else AppInputState.Normal,
                        errorMessage = state.firstNameError
                    )
                }

                // Last Name
                Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
                    Text(
                        text = stringResource(id = R.string.contact_info_last_name),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    AppTextInput(
                        value = state.lastName,
                        onValueChange = { onEvent(HotelContactInfoEvent.LastNameChanged(it)) },
                        placeholder = stringResource(id = R.string.contact_info_last_name_placeholder),
                        modifier = Modifier.fillMaxWidth(),
                        state = if (state.lastNameError != null) AppInputState.Error else AppInputState.Normal,
                        errorMessage = state.lastNameError
                    )
                }

                // Phone
                Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
                    Text(
                        text = stringResource(id = R.string.contact_info_phone),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    TravelPhoneNumberInput(
                        countryCode = state.countryCode,
                        onCountryCodeChange = { onEvent(HotelContactInfoEvent.CountryCodeChanged(it)) },
                        phoneNumber = state.phoneNumber,
                        onPhoneNumberChange = { onEvent(HotelContactInfoEvent.PhoneNumberChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        state = if (state.phoneNumberError != null) AppInputState.Error else AppInputState.Normal,
                        errorMessage = state.phoneNumberError
                    )
                }

                // Email
                Column(verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)) {
                    Text(
                        text = stringResource(id = R.string.contact_info_email),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    AppTextInput(
                        value = state.email,
                        onValueChange = { onEvent(HotelContactInfoEvent.EmailChanged(it)) },
                        placeholder = stringResource(id = R.string.contact_info_email_placeholder),
                        modifier = Modifier.fillMaxWidth(),
                        state = if (state.emailError != null) AppInputState.Error else AppInputState.Normal,
                        errorMessage = state.emailError
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelContactInfoScreenPreview() {
    Travelin2026ProjectLabTheme {
        HotelContactInfoContent(
            state = HotelContactInfoState(
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@gmail.com",
                phoneNumber = "123 456 789"
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
