package com.softserveacademy.core.presentation.design_system.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softserveacademy.core.presentation.design_system.R

// 1. Define the FontFamilies
val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)

val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

// 2. Create the Typography System
val TravelinTypography = Typography(
    // LOGO: "TravelIn" (Inter Semi Bold 60)
    displayLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 60.sp,
        lineHeight = 72.sp
    ),
    // TITLES: "Ready to explore" (Inter Bold 34)
    displayMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),
    // LARGE SUBTITLES: "Hi Paul" / "Let's travel you in" (Inter SemiBold 28)
    displaySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    // WELCOME TEXT: "Discover the world with every" (Inter Regular 20)
    headlineLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    // SECTION TITLES/BUTTONS: "Hotels Recommendation" / "Sign in" / "Swiss bell hotel" (Inter SemiBold 16)
    headlineSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // INPUTS AND QUESTIONS: "Where to go" / "Texts in fields" (Inter Regular 16)
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // CARD TITLES: "Mount Bromo" / "Booking ID" (Inter SemiBold 14)
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // CODES: "CGK" (Inter Bold 14)
    titleSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // GENERAL TEXT: "See all" / "Upcoming" / "2000 points" / "Forgot password" (Inter Regular 14)
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // DETAILS: "24 march" / "4.9" / "3d2n" / "4 stars hotel" (Inter Regular 12)
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    // MINIMUM LABELS: "Start from" (Inter Regular 10)
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp
    ),
    // LANGUAGE: "English" (Roboto Bold 16)
    labelLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // ADDRESS/DESCRIPTION: "Jl Sunset" / "Volcano in east java" (Inter Regular 9)
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 12.sp
    )
)

@Preview(showBackground = true, name = "Typography Gallery")
@Composable
fun TypographyPreview() {
    Travelin2026ProjectLabTheme {
        Surface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Display Large (Logo)", style = TravelinTypography.displayLarge)
                Text(text = "Display Medium (Title)", style = TravelinTypography.displayMedium)
                Text(text = "Display Small (Subtitles)", style = TravelinTypography.displaySmall)

                Text(text = "Headline Large (Welcome)", style = TravelinTypography.headlineLarge, modifier = Modifier.padding(top = 16.dp))
                Text(text = "Headline Small (Sections)", style = TravelinTypography.headlineSmall)

                Text(text = "Title Large (Inputs)", style = TravelinTypography.titleLarge, modifier = Modifier.padding(top = 16.dp))
                Text(text = "Title Medium (Cards)", style = TravelinTypography.titleMedium)
                Text(text = "Title Small (Codes)", style = TravelinTypography.titleSmall)

                Text(text = "Body Large (General)", style = TravelinTypography.bodyLarge, modifier = Modifier.padding(top = 16.dp))
                Text(text = "Body Medium (Details)", style = TravelinTypography.bodyMedium)
                Text(text = "Body Small (Minimum)", style = TravelinTypography.bodySmall)

                Text(text = "Label Large (Language - Roboto)", style = TravelinTypography.labelLarge, modifier = Modifier.padding(top = 16.dp))
                Text(text = "Label Small (Address)", style = TravelinTypography.labelSmall)
            }
        }
    }
}
