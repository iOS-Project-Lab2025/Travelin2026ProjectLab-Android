package com.softserveacademy.feature.onboarding.presentation;

public class OnboardingScreen {

    @Composable
    fun OnboardingScreen(
            onGetStarted: () -> Unit
) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Imagen de Fondo
            Image(
                    painter = painterResource(id = R.drawable.test_place), // La playa de tu imagen
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
            )

            // 2. Logo Central
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                TravelinLogo() // El componente que ya perfeccionamos
            }

            // 3. Card Blanca Inferior
            Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White
            ) {
                Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            text = stringResource(id = R.string.onboarding_title),
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón "Your Journey Starts Here"
                    Button(
                            onClick = onGetStarted,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(28.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(id = R.string.onboarding_button))
                            Spacer(Modifier.width(8.dp))
                            Icon(painterResource(id = R.drawable.ic_flight), null)
                        }
                    }
                }
            }
        }
    }
}
