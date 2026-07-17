package com.softserveacademy.home.presentation

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import com.softserveacademy.home.presentation.model.UpcomingTripUi
import com.softserveacademy.home.presentation.ui.components.UpcomingTripCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Pruebas unitarias para el componente [com.softserveacademy.home.presentation.ui.components.UpcomingTripCard] siguiendo el manual de testing
 * del proyecto (Plantilla C: Componentes Compose sin emulador).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class UpcomingTripCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Verifica que si el perfil tiene un viaje asociado, los detalles clave se muestren.
     * Refleja el CA: "Si tiene un viaje asociado al perfil, este componente se muestre con sus datos".
     */
    @Test
    fun `given a trip associated when UpcomingTripCard is rendered then booking id and flight details are displayed`() {
        // GIVEN: Datos de un viaje de prueba (Simulando viaje asociado al perfil)
        val sampleTrip = UpcomingTripUi(
            status = "Upcoming",
            date = "24 March 2024",
            originCode = "CGK",
            originTime = "05:30",
            destinationCode = "DPS",
            destinationTime = "06:30",
            duration = "1h 30m",
            airline = "Sentosa Air",
            travelClass = "Economy",
            flightType = "Direct",
            bookingId = "ZEEBAW"
        )

        // WHEN: Renderizamos el componente en el entorno de prueba
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                UpcomingTripCard(trip = sampleTrip)
            }
        }

        // THEN: Validamos que la información crítica sea visible en la interfaz
        composeTestRule.onNodeWithText("ZEEBAW").assertIsDisplayed()
        composeTestRule.onNodeWithText("CGK").assertIsDisplayed()
        composeTestRule.onNodeWithText("DPS").assertIsDisplayed()
        composeTestRule.onNodeWithText("Upcoming").assertIsDisplayed()
    }

    @Test
    fun `given a trip when rendered then secondary flight info like airline and class are displayed`() {
        val sampleTrip = UpcomingTripUi(
            status = "Upcoming", date = "24 March 2024", originCode = "CGK", originTime = "05:30",
            destinationCode = "DPS", destinationTime = "06:30", duration = "1h 30m",
            airline = "Sentosa Air", travelClass = "Economy", flightType = "Direct", bookingId = "ZEEBAW"
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                UpcomingTripCard(trip = sampleTrip)
            }
        }

        // THEN: Verificamos la cadena combinada que construye el componente
        composeTestRule.onNodeWithText("Sentosa Air • Economy • Direct").assertIsDisplayed()
    }

    @Test
    fun `given a card without onClick when clicked then no action occurs`() {
        val sampleTrip = UpcomingTripUi(
            status = "Upcoming", date = "24 March 2024", originCode = "CGK", originTime = "05:30",
            destinationCode = "DPS", destinationTime = "06:30", duration = "1h 30m",
            airline = "Sentosa Air", travelClass = "Economy", flightType = "Direct", bookingId = "ZEEBAW"
        )

        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                UpcomingTripCard(trip = sampleTrip, onClick = null)
            }
        }

        // THEN: Verificamos que el nodo no tenga la propiedad "OnClick" (Action clickable)
        composeTestRule.onNodeWithText("ZEEBAW").assertHasNoClickAction()
    }
}