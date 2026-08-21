package com.softserveacademy.home.presentation.koog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.softserveacademy.core.domain.model.AiRecommendation
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UI tests for [RecommendationCard] using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class RecommendationCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given recommendation when rendered then shows English Take me there button`() {
        var clicked = false
        val recommendation = AiRecommendation(
            name = "Beach Place",
            latitude = 1.0,
            longitude = 2.0,
            description = "A beautiful beach",
            type = "Beach",
            imageUrl = null
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                RecommendationCard(
                    recommendation = recommendation,
                    onDismiss = {},
                    onNavigateClick = { clicked = true }
                )
            }
        }

        // Check English text
        composeTestRule.onNodeWithText("Beach Place").assertIsDisplayed()
        composeTestRule.onNodeWithText("A beautiful beach").assertIsDisplayed()
        
        // Verify "Take me there!" button
        composeTestRule.onNodeWithText("Take me there!").assertIsDisplayed().performClick()
        
        assertTrue(clicked)
    }
}
