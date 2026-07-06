# Manual de Estrategia de Testing y Calidad de Código

Este documento establece las directrices obligatorias de desarrollo, arquitectura de pruebas y automatización para nuestro flujo Trunk-based Development, bajo arquitectura Clean Architecture + MVVM con Jetpack Compose. Todos los desarrolladores del equipo deben seguir la misma estructura.

---

## 1. El Flujo de Trabajo Obligatorio (Local + CI)

Para mantener la rama main siempre estable e integrar código de forma rápida y segura, el ciclo de vida de cada tarea es el siguiente:

[Leer Criterios de Aceptación (CA)] -> [Escribir Código + KDocs] -> [Escribir Tests en carpeta test/] -> [Ejecutar Validación Local] -> [Abrir PR e Integración en GitHub]

### Comandos obligatorios antes de cada git push
Abre la pestaña Terminal abajo en Android Studio y ejecuta estos tres comandos. Si alguno falla, el servidor de GitHub Actions rechazará tu código en la nube (puedes ver formas manuales en android studio de ejecutar esto):

```bash
# 1. Da formato automático a tu código (estética visual)
./gradlew ktlintFormat

# 2. Analiza la semántica y obliga a documentar en KDoc
./gradlew detekt

# 3. Corre todas las pruebas unitarias y de UI locales
./gradlew testDebugUnitTest
```

### Como funciona GitHub Actions arriba? (Costo 0)
Dado que nuestro repositorio es PUBLICO y EDUCATIVO, disponemos de servidores con minutos gratuitos e ilimitados. Al abrir una Pull Request (PR):

1. Descarga: El servidor simula de forma transparente la fusión de tu rama con main.
2. Configuración: Descarga el entorno de Android usando caché para compilar en menos de 4 minutos.
3. Evaluación: Ejecuta de forma estricta los comandos de revisión de arriba.
4. Reporte: Si todo pasa, la PR obtiene el check verde. Si algo falla, se bloquea con una X roja y publica un informe interactivo con la línea exacta del error. No es necesario descargar la rama de un compañero para validar si compila.

---

## 2. Arquitectura y Ubicación de Dependencias

Para evitar que cada desarrollador altere las versiones de Gradle de forma caótica, el stack de herramientas se administra de manera centralizada.

* Herramientas Globales (ktlint y detekt): Deben inyectarse masivamente desde el archivo raíz. No necesitamos agregar a submódulos.
* Librerías de Pruebas: Las versiones viven en el archivo único gradle/libs.versions.toml. 

```toml
[versions]
mockk = "1.13.13" #referenciales, se deben actualizar
robolectric = "4.14.1" #referenciales, se deben actualizar

[libraries]
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }

[bundles]
testing-stack = ["junit", "mockk", "robolectric"]
```
* Para utilizarlas en un módulo, solo debes llamar al paquete agrupado (bundle) en las dependencias de tu archivo build.gradle.kts:

```kotlin
dependencies {
    // Inyecta JUnit, MockK y Robolectric en un solo comando
    testImplementation(libs.bundles.testing.stack)
    
    // Si tu módulo maneja pantallas con Jetpack Compose, añade la regla de UI:
    testImplementation("androidx.compose.ui:ui-test-junit4:1.7.0")
    debugImplementation(libs.test.compose.manifest)
}
```

---

## 3. Estructura de Carpetas de Prueba por Módulo

Los archivos de prueba deben clonar exactamente la misma estructura de paquetes de la capa de producción (main), pero dentro del directorio test/java/.

Observa este mapa de un módulo de funcionalidad típico (ej: :feature:login):

```text
feature/login/src/
├── main/java/com/tuproyecto/login/
│   ├── domain/
│   │   └── LoginUseCase.kt             <-- Código de producción
│   ├── data/
│   │   └── LoginRepositoryImpl.kt
│   └── presentation/
│       ├── LoginViewModel.kt           <-- Código de producción
│       └── LoginScreen.kt              <-- Vista en Compose
│
└── test/java/com/tuproyecto/login/     <-- AQUÍ VAN TUS TESTS
    ├── domain/
    │   └── LoginUseCaseTest.kt         <-- Usa Plantilla A (MockK)
    ├── data/
    │   └── LoginRepositoryImplTest.kt
    └── presentation/
        ├── LoginViewModelTest.kt       <-- Usa Plantilla B (MockK)
        └── LoginScreenTest.kt          <-- Usa Plantilla C (Robolectric)
```

Regla de Oro: El archivo de prueba debe llamarse exactamente igual al componente original, agregando el sufijo Test. Ejemplo: LoginViewModel.kt -> LoginViewModelTest.kt.

---

## 4. Como asociar los Unit Tests a los Criterios de Aceptación (CA)

No inventamos los casos de prueba. Cada método @Test debe ser el reflejo técnico directo de un Criterio de Aceptación detallado en tu tarjeta de la tarea.

### Estructura Semántica: Given - When - Then
Cada función de prueba debe nombrarse e internacionalizarse bajo la estructura de comportamiento del usuario:

* given (Dado): El escenario base inicial y la preparación de datos simulados con MockK (como opcion).
* when (Cuando): La acción ejecutable del usuario o la llamada al método de la arquitectura.  (ej: viewModel.onLoginClick())
* then (Entonces): La aserción o resultado esperado (lo que dicta el Criterio de Aceptación). (ej: assertTrue(result.isSuccess)

---

## 5. Plantillas de Código Ejemplo (Templates)

### Plantilla A: Capa de Dominio (Casos de Uso / Use Cases)
* Ubicación: Módulos de lógica pura Kotlin (sin librerías de Android).
* Enfoque: Probar las reglas de cálculo o condiciones del negocio.

```kotlin
package com.softserveacademy.feature.auth.login.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas unitarias vinculadas a los CA de la US-Auth.
 * Verifica la lógica de negocio del inicio de sesión.
 */
class LoginUseCaseTest {

    private val repository = mockk<LoginRepository>()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `given valid credentials when login then returns success result`() = runTest {
        // GIVEN: El repositorio devuelve éxito
        coEvery { repository.login("test@mail.com", "pass123") } returns Result.success(Unit)

        // WHEN: Ejecutamos el UseCase
        val result = useCase("test@mail.com", "pass123")

        // THEN: El resultado debe ser exitoso según el CA-1
        assertTrue(result.isSuccess)
    }
}
```

### Plantilla B: Capa de Presentación (ViewModels / MVVM)
* Ubicación: Módulos que exponen estados de carga, éxito o error a la vista.
* Enfoque: Validar que los flujos y datos reactivos modifiquen el estado visual correctamente.

```kotlin
package com.softserveacademy.feature.auth.login.presentation

import com.softserveacademy.feature.auth.login.domain.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas de estados vinculados a la US-Login.
 * Verifica que la UI reaccione correctamente ante errores.
 */
class LoginViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()

    @Test
    fun `given network error when login is clicked then error state is updated`() = runTest {
        // GIVEN: El loginUseCase falla con un mensaje de red
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception("Network Error"))

        // WHEN: El usuario intenta loguearse en el ViewModel
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onLoginClick()

        // THEN: El estado de 'error' del ViewModel debe capturar el mensaje
        assertEquals("Network Error", viewModel.error)
    }
}
```

### Plantilla C: Capa de Interfaz (Componentes Compose SIN Emulador)
* Ubicación: Pantallas y componentes reutilizables de Jetpack Compose.
* Enfoque: Probar flujos visuales y clicks simulando Android en la PC vía Robolectric (Costo 0 y alta velocidad).

```kotlin
package com.softserveacademy.core.presentation.design_system.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.softserveacademy.core.presentation.design_system.theme.Travelin2026ProjectLabTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Validación de componentes del Design System usando Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [37]) // Creo estamos usando esta
class TravelPrimaryButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given enabled button when clicked then action lambda is executed`() {
        var isClicked = false

        // GIVEN: El botón del Design System habilitado
        composeTestRule.setContent {
            Travelin2026ProjectLabTheme {
                TravelPrimaryButton(
                    text = "Confirm",
                    onClick = { isClicked = true }
                )
            }
        }

        // WHEN: El usuario hace clic físicamente
        composeTestRule.onNodeWithText("Confirm").performClick()

        // THEN: Verificamos el efecto colateral (el cambio de la variable)
        assert(isClicked)
    }
}
```

---

## 6. Contexto de Prompts para Inteligencia Artificial (Copilot / ChatGPT / Cursor / Claudio / Gepeto / ETC)

Para asegurar que la IA genere resultados útiles y estandarizados, utiliza este flujo de dos pasos. Esto garantiza que no solo escriba código, sino que "entienda" nuestro estándar de documentación y pruebas.

### Paso 1: Establecer el Rol (System Prompt)
Copia esto al iniciar tu chat para configurar la "mente" de la IA:

> "Actúa como un Desarrollador Android Senior para el proyecto **Travelin2026**. Tu misión es generar código, documentación KDoc y pruebas unitarias bajo las siguientes reglas:
> 1. Arquitectura: Clean Architecture + MVVM + Modularización.
> 2. Testing: MockK, Coroutines Test y Robolectric.
> 3. Naming: Estructura Given-When-Then para tests.
> 4. Paquete base: `com.softserveacademy`.
     > Confirma si estás listo para recibir el código."

### Paso 2: Ejecutar la Tarea (Task Prompt)
Una vez que la IA confirme, usa este formato para solicitar el trabajo sobre tu archivo actual:

> "Basándote en el código de este archivo/funcionalidad:
> **[PEGA AQUÍ TU CÓDIGO O EXPLICA LA FUNCIÓN]**
>
> Por favor, genera:
> 1. **Documentación:** Agrega KDocs descriptivos en inglés a todas las clases y funciones.
> 2. **Pruebas Unitarias:** Crea el archivo de Test correspondiente siguiendo el estándar del proyecto (Given-When-Then).
> 3. **Validación:** Asegúrate de que los tests cubran los flujos de éxito y error."

---

## 7. Criterios de Aceptación para la Pull Request (PR)

Para que tu código sea aprobado, debe cumplir con:
1. **Compilación:** Pasa `./gradlew assembleDebug`.
2. **Calidad:** Pasa `./gradlew detekt` y `./gradlew ktlintCheck`.
3. **Tests:** Cada nueva funcionalidad tiene su espejo en la carpeta `test/` con cobertura lógica.
4. **Docs:** El código generado contiene KDocs que explican el "por qué" y no solo el "qué".