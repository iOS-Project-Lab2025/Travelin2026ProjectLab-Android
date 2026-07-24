package com.softserveacademy.home.presentation.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.model.UserProfileUi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TravelUserProfileCardInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProfile = UserProfileUi(
        name = "John Doe",
        points = 1280,
        avatarUrl = "https://i.pravatar.cc/300"
    )

    @Test
    fun displaysUserName() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUserProfileCard(userProfile = sampleProfile)
            }
        }
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
    }

    @Test
    fun displaysPoints() {
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelUserProfileCard(userProfile = sampleProfile)
            }
        }
        composeTestRule.onNodeWithText("1,280").assertIsDisplayed()
    }
}
