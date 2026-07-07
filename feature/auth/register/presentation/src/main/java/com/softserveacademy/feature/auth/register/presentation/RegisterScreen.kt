package com.softserveacademy.feature.auth.register.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.softserveacademy.core.presentation.design_system.components.AppPasswordInput
import com.softserveacademy.core.presentation.design_system.components.AppTextInput
import com.softserveacademy.core.presentation.design_system.components.AppNumberInput
import com.softserveacademy.core.presentation.design_system.components.TravelIconButton
import com.softserveacademy.core.presentation.design_system.components.TravelAuthPrimaryButton
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
        countryCode = viewModel.countryCode,
        onCountryCodeChange = { viewModel.countryCode = it },
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
        onNavigateBack = onNavigateBack,
        onRegisterSuccess = onRegisterSuccess,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
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
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    isLoading: Boolean,
    error: String?,
    onRegisterClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var showCountryPicker by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(searchQuery) {
        countries.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.code.contains(searchQuery)
        }
    }

    if (showCountryPicker) {
        ModalBottomSheet(
            onDismissRequest = { 
                showCountryPicker = false
                searchQuery = ""
            },
            sheetState = rememberModalBottomSheetState(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TravelinDimens.PaddingMedium)
            ) {
                Text(
                    text = "Select Country",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = TravelinDimens.PaddingSmall)
                )

                AppTextInput(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Search country or code",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = SearchIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(filteredCountries) { country ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCountryCodeChange(country.code)
                                    showCountryPicker = false
                                    searchQuery = ""
                                }
                                .padding(vertical = TravelinDimens.PaddingSmall),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
                        ) {
                            Text(text = country.flag, style = MaterialTheme.typography.headlineSmall)
                            Text(text = country.name, modifier = Modifier.weight(1f))
                            Text(
                                text = country.code,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(TravelinDimens.PaddingLarge)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceExtraSmall)
    ) {
        TravelIconButton(
            icon = ArrowLeftIcon,
            onClick = onNavigateBack,
            contentDescription = "Back"
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        Text(
            text = "Create account",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Get the best out of the world by creating an account",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceMedium))

        // Labels adjusted to be smaller and closer to inputs
        Text(
            text = "First name",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = firstName,
            onValueChange = onFirstNameChange,
            placeholder = "First name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Last name",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = lastName,
            onValueChange = onLastNameChange,
            placeholder = "Last name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Phone",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Country Code Selector
            Surface(
                modifier = Modifier
                    .height(TravelinDimens.ButtonHeightMedium)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { showCountryPicker = true },
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = TravelinDimens.PaddingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TravelinDimens.Space2ExtraSmall)
                ) {
                    val country = countries.find { it.code == countryCode }
                    if (country != null) {
                        Text(text = country.flag, style = MaterialTheme.typography.bodyLarge)
                    }
                    Text(text = countryCode, style = MaterialTheme.typography.bodyLarge)
                    Icon(
                        imageVector = AngleRightIcon, // Mock dropdown arrow
                        contentDescription = null,
                        modifier = Modifier
                            .size(TravelinDimens.IconSizeExtraSmall)
                            .rotate(90f),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            AppNumberInput(
                value = phone,
                onValueChange = onPhoneChange,
                placeholder = "123 456 789",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Age",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppNumberInput(
            value = age,
            onValueChange = onAgeChange,
            placeholder = "30",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Email",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppTextInput(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Text(
            text = "Password",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        AppPasswordInput(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceExtraSmall))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange
            )
            Text(
                text = "I accept term and condition",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(TravelinDimens.SpaceSmall))

        TravelAuthPrimaryButton(
            text = "Create Account",
            onClick = onRegisterClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Go back")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onNavigateBack() }
            )
        }
        
        Spacer(modifier = Modifier.height(TravelinDimens.SpaceLarge))
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    Travelin2026ProjectLabTheme {
        RegisterContent(
            firstName = "",
            onFirstNameChange = {},
            lastName = "",
            onLastNameChange = {},
            countryCode = "+855",
            onCountryCodeChange = {},
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
            onNavigateBack = {},
            onRegisterSuccess = {}
        )
    }
}

private data class Country(val name: String, val code: String, val flag: String)

private val countries = listOf(
    Country("Afghanistan", "+93", "🇦🇫"),
    Country("Albania", "+355", "🇦🇱"),
    Country("Algeria", "+213", "🇩🇿"),
    Country("Andorra", "+376", "🇦🇩"),
    Country("Angola", "+244", "🇦🇴"),
    Country("Argentina", "+54", "🇦🇷"),
    Country("Armenia", "+374", "🇦🇲"),
    Country("Australia", "+61", "🇦🇺"),
    Country("Austria", "+43", "🇦🇹"),
    Country("Azerbaijan", "+994", "🇦🇿"),
    Country("Bahamas", "+1-242", "🇧🇸"),
    Country("Bahrain", "+973", "🇧🇭"),
    Country("Bangladesh", "+880", "🇧🇩"),
    Country("Barbados", "+1-246", "🇧🇧"),
    Country("Belarus", "+375", "🇧🇾"),
    Country("Belgium", "+32", "🇧🇪"),
    Country("Belize", "+501", "🇧🇿"),
    Country("Benin", "+229", "🇧🇯"),
    Country("Bhutan", "+975", "🇧🇹"),
    Country("Bolivia", "+591", "🇧🇴"),
    Country("Bosnia and Herzegovina", "+387", "🇧🇦"),
    Country("Botswana", "+267", "🇧🇼"),
    Country("Brazil", "+55", "🇧🇷"),
    Country("Brunei", "+673", "🇧🇳"),
    Country("Bulgaria", "+359", "🇧🇬"),
    Country("Burkina Faso", "+226", "🇧🇫"),
    Country("Burundi", "+257", "🇧🇮"),
    Country("Cambodia", "+855", "🇰🇭"),
    Country("Cameroon", "+237", "🇨🇲"),
    Country("Canada", "+1", "🇨🇦"),
    Country("Cape Verde", "+238", "🇨🇻"),
    Country("Central African Republic", "+236", "🇨🇫"),
    Country("Chad", "+235", "🇹🇩"),
    Country("Chile", "+56", "🇨🇱"),
    Country("China", "+86", "🇨🇳"),
    Country("Colombia", "+57", "🇨🇴"),
    Country("Comoros", "+269", "🇰🇲"),
    Country("Congo", "+242", "🇨🇬"),
    Country("Costa Rica", "+506", "🇨🇷"),
    Country("Croatia", "+385", "🇭🇷"),
    Country("Cuba", "+53", "🇨🇺"),
    Country("Cyprus", "+357", "🇨🇾"),
    Country("Czech Republic", "+420", "🇨🇿"),
    Country("Denmark", "+45", "🇩🇰"),
    Country("Djibouti", "+253", "🇩🇯"),
    Country("Dominica", "+1-767", "🇩🇲"),
    Country("Dominican Republic", "+1-809", "🇩🇴"),
    Country("Ecuador", "+593", "🇪🇨"),
    Country("Egypt", "+20", "🇪🇬"),
    Country("El Salvador", "+503", "🇸🇻"),
    Country("Equatorial Guinea", "+240", "🇬🇶"),
    Country("Eritrea", "+291", "🇪🇷"),
    Country("Estonia", "+372", "🇪🇪"),
    Country("Ethiopia", "+251", "🇪🇹"),
    Country("Fiji", "+679", "🇫🇯"),
    Country("Finland", "+358", "🇫🇮"),
    Country("France", "+33", "🇫🇷"),
    Country("Gabon", "+241", "🇬🇦"),
    Country("Gambia", "+220", "🇬🇲"),
    Country("Georgia", "+995", "🇬🇪"),
    Country("Germany", "+49", "🇩🇪"),
    Country("Ghana", "+233", "🇬🇭"),
    Country("Greece", "+30", "🇬🇷"),
    Country("Grenada", "+1-473", "🇬🇩"),
    Country("Guatemala", "+502", "🇬🇹"),
    Country("Guinea", "+224", "🇬🇳"),
    Country("Guinea-Bissau", "+245", "🇬🇼"),
    Country("Guyana", "+592", "🇬🇾"),
    Country("Haiti", "+509", "🇭🇹"),
    Country("Honduras", "+504", "🇭🇳"),
    Country("Hungary", "+36", "🇭🇺"),
    Country("Iceland", "+354", "🇮🇸"),
    Country("India", "+91", "🇮🇳"),
    Country("Indonesia", "+62", "🇮🇩"),
    Country("Iran", "+98", "🇮🇷"),
    Country("Iraq", "+964", "🇮🇶"),
    Country("Ireland", "+353", "🇮🇪"),
    Country("Israel", "+972", "🇮🇱"),
    Country("Italy", "+39", "🇮🇹"),
    Country("Jamaica", "+1-876", "🇯🇲"),
    Country("Japan", "+81", "🇯🇵"),
    Country("Jordan", "+962", "🇯🇴"),
    Country("Kazakhstan", "+7", "🇰🇿"),
    Country("Kenya", "+254", "🇰🇪"),
    Country("Kiribati", "+686", "🇰🇮"),
    Country("Korea, North", "+850", "🇰🇵"),
    Country("Korea, South", "+82", "🇰🇷"),
    Country("Kuwait", "+965", "🇰🇼"),
    Country("Kyrgyzstan", "+996", "🇰🇬"),
    Country("Laos", "+856", "🇱🇦"),
    Country("Latvia", "+371", "🇱🇻"),
    Country("Lebanon", "+961", "🇱🇧"),
    Country("Lesotho", "+266", "🇱🇸"),
    Country("Liberia", "+231", "🇱🇷"),
    Country("Libya", "+218", "🇱🇾"),
    Country("Liechtenstein", "+423", "🇱🇮"),
    Country("Lithuania", "+370", "🇱🇹"),
    Country("Luxembourg", "+352", "🇱🇺"),
    Country("Macedonia", "+389", "🇲🇰"),
    Country("Madagascar", "+261", "🇲🇬"),
    Country("Malawi", "+265", "🇲🇼"),
    Country("Malaysia", "+60", "🇲🇾"),
    Country("Maldives", "+960", "🇲🇻"),
    Country("Mali", "+223", "🇲🇱"),
    Country("Malta", "+356", "🇲🇹"),
    Country("Marshall Islands", "+692", "🇲🇭"),
    Country("Mauritania", "+222", "🇲🇷"),
    Country("Mauritius", "+230", "🇲🇺"),
    Country("Mexico", "+52", "🇲🇽"),
    Country("Micronesia", "+691", "🇫🇲"),
    Country("Moldova", "+373", "🇲🇩"),
    Country("Monaco", "+377", "🇲🇨"),
    Country("Mongolia", "+976", "🇲🇳"),
    Country("Montenegro", "+382", "🇲🇪"),
    Country("Morocco", "+212", "🇲🇦"),
    Country("Mozambique", "+258", "🇲🇿"),
    Country("Myanmar", "+95", "🇲🇲"),
    Country("Namibia", "+264", "🇳🇦"),
    Country("Nauru", "+674", "🇳🇷"),
    Country("Nepal", "+977", "🇳🇵"),
    Country("Netherlands", "+31", "🇳🇱"),
    Country("New Zealand", "+64", "🇳🇿"),
    Country("Nicaragua", "+505", "🇳🇮"),
    Country("Niger", "+227", "🇳🇪"),
    Country("Nigeria", "+234", "🇳🇬"),
    Country("Norway", "+47", "🇳🇴"),
    Country("Oman", "+968", "🇴🇲"),
    Country("Pakistan", "+92", "🇵🇰"),
    Country("Palau", "+680", "🇵🇼"),
    Country("Panama", "+507", "🇵🇦"),
    Country("Papua New Guinea", "+675", "🇵🇬"),
    Country("Paraguay", "+595", "🇵🇾"),
    Country("Peru", "+51", "🇵🇪"),
    Country("Philippines", "+63", "🇵🇭"),
    Country("Poland", "+48", "🇵🇱"),
    Country("Portugal", "+351", "🇵🇹"),
    Country("Qatar", "+974", "🇶🇦"),
    Country("Romania", "+40", "🇷🇴"),
    Country("Russia", "+7", "🇷🇺"),
    Country("Rwanda", "+250", "🇷🇼"),
    Country("Saint Kitts and Nevis", "+1-869", "🇰🇳"),
    Country("Saint Lucia", "+1-758", "🇱🇨"),
    Country("Saint Vincent and the Grenadines", "+1-784", "🇻🇨"),
    Country("Samoa", "+685", "🇼🇸"),
    Country("San Marino", "+378", "🇸🇲"),
    Country("Sao Tome and Principe", "+239", "🇸🇹"),
    Country("Saudi Arabia", "+966", "🇸🇦"),
    Country("Senegal", "+221", "🇸🇳"),
    Country("Serbia", "+381", "🇷🇸"),
    Country("Seychelles", "+248", "🇸🇨"),
    Country("Sierra Leone", "+232", "🇸🇱"),
    Country("Singapore", "+65", "🇸🇬"),
    Country("Slovakia", "+421", "🇸🇰"),
    Country("Slovenia", "+386", "🇸🇮"),
    Country("Solomon Islands", "+677", "🇸🇧"),
    Country("Somalia", "+252", "🇸🇴"),
    Country("South Africa", "+27", "🇿🇦"),
    Country("South Sudan", "+211", "🇸🇸"),
    Country("Spain", "+34", "🇪🇸"),
    Country("Sri Lanka", "+94", "🇱🇰"),
    Country("Sudan", "+249", "🇸🇩"),
    Country("Suriname", "+597", "🇸🇷"),
    Country("Swaziland", "+268", "🇸🇿"),
    Country("Sweden", "+46", "🇸🇪"),
    Country("Switzerland", "+41", "🇨🇭"),
    Country("Syria", "+963", "🇸🇾"),
    Country("Taiwan", "+886", "🇹🇼"),
    Country("Tajikistan", "+992", "🇹🇯"),
    Country("Tanzania", "+255", "🇹🇿"),
    Country("Thailand", "+66", "🇹🇭"),
    Country("Togo", "+228", "🇹🇬"),
    Country("Tonga", "+676", "🇹🇴"),
    Country("Trinidad and Tobago", "+1-868", "🇹🇹"),
    Country("Tunisia", "+216", "🇹🇳"),
    Country("Turkey", "+90", "🇹🇷"),
    Country("Turkmenistan", "+993", "🇹🇲"),
    Country("Tuvalu", "+688", "🇹🇻"),
    Country("Uganda", "+256", "🇺🇬"),
    Country("Ukraine", "+380", "🇺🇦"),
    Country("United Arab Emirates", "+971", "🇦🇪"),
    Country("United Kingdom", "+44", "🇬🇧"),
    Country("United States", "+1", "🇺🇸"),
    Country("Uruguay", "+598", "🇺🇾"),
    Country("Uzbekistan", "+998", "🇺🇿"),
    Country("Vanuatu", "+678", "🇻🇺"),
    Country("Vatican City", "+379", "🇻🇦"),
    Country("Venezuela", "+58", "🇻🇪"),
    Country("Vietnam", "+84", "🇻🇳"),
    Country("Yemen", "+967", "🇾🇪"),
    Country("Zambia", "+260", "🇿🇲"),
    Country("Zimbabwe", "+263", "🇿🇼")
).sortedBy { it.name }
