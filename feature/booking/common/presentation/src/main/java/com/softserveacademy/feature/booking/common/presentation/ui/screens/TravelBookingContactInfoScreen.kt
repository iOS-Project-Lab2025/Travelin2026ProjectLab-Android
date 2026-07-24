package com.softserveacademy.feature.booking.common.presentation.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.TravelPhoneNumberInput
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens
import com.softserveacademy.feature.booking.common.presentation.events.TravelBookingContactInfoEvent
import com.softserveacademy.feature.booking.common.presentation.states.TravelBookingContactInfoState
import com.softserveacademy.feature.booking.common.presentation.ui.components.TravelBookingBottomBar
import com.softserveacademy.feature.booking.common.presentation.R

@Composable
fun TravelBookingContactInfoScreen(
    modifier: Modifier = Modifier,
    state: TravelBookingContactInfoState,
    onEvent: (TravelBookingContactInfoEvent) -> Unit,
    onBackClick: () -> Unit,
    subtitle: String? = null,
) {
    Scaffold(
        bottomBar = {
            TravelBookingBottomBar(
                onBackClick = onBackClick,
                onNextClick = { onEvent(TravelBookingContactInfoEvent.OnNextClick) }
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
                    text = subtitle?: stringResource(id = R.string.contact_info_subtitle),
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
                        onValueChange = { onEvent(TravelBookingContactInfoEvent.FirstNameChanged(it)) },
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
                        onValueChange = { onEvent(TravelBookingContactInfoEvent.LastNameChanged(it)) },
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
                        onCountryCodeChange = { onEvent(TravelBookingContactInfoEvent.CountryCodeChanged(it)) },
                        phoneNumber = state.phoneNumber,
                        onPhoneNumberChange = { onEvent(TravelBookingContactInfoEvent.PhoneNumberChanged(it)) },
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
                        onValueChange = { onEvent(TravelBookingContactInfoEvent.EmailChanged(it)) },
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
fun TravelBookingContactInfoScreenPreview() {
    Travelin2026ProjectLabTheme {
        TravelBookingContactInfoScreen(
            state = TravelBookingContactInfoState(
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
