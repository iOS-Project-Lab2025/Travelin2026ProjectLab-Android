package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveacademy.core.presentation.design_system.components.util.inputs.AppInputState
import com.softserveacademy.core.presentation.design_system.theme.AngleRightIcon
import com.softserveacademy.core.presentation.design_system.theme.SearchIcon
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.core.presentation.design_system.theme.TravelinDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPhoneNumberInput(
    modifier: Modifier = Modifier,
    countryCode: String,
    onCountryCodeChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    placeholder: String = "123 456 789",
    state: AppInputState = AppInputState.Normal,
    errorMessage: String? = null
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

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall),
            verticalAlignment = Alignment.Top
        ) {
            // Country Code Selector
            Surface(
                modifier = Modifier
                    .height(TravelinDimens.ButtonHeightLarge)
                    .border(
                        width = 1.dp,
                        color = if (state == AppInputState.Error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { showCountryPicker = true },
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .padding(TravelinDimens.PaddingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(TravelinDimens.SpaceSmall)
                ) {
                    val country = countries.find { it.code == countryCode }
                    if (country != null) {
                        Text(
                            text = country.flag,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Text(
                        text = countryCode,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = AngleRightIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.rotate(90f)
                    )
                }
            }

            // Phone Number Input
            AppNumberInput(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = placeholder,
                modifier = Modifier.weight(1f),
                state = state,
                errorMessage = null // We show error message below the row
            )
        }

        if (state == AppInputState.Error && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = TravelinDimens.PaddingSmall)
            )
        }
    }
}

@Preview
@Composable
fun TravelPhoneNumberInputPreview() {
    Travelin2026ProjectLabTheme {
        TravelPhoneNumberInput(
            countryCode = "+1",
            onCountryCodeChange = {},
            phoneNumber = "123 456 789",
            onPhoneNumberChange = {}
        )
    }
}

data class Country(val name: String, val code: String, val flag: String)

val countries = listOf(
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
