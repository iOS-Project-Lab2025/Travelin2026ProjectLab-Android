# Testing Strategy and Code Quality Manual

This document establishes the mandatory development guidelines, testing architecture, and automation for our Trunk-based Development workflow, under Clean Architecture + MVVM with Jetpack Compose. All team developers must follow the same structure.

---

## 1. The Mandatory Workflow (Local + CI)

To keep the `main` branch always stable and integrate code quickly and safely, the lifecycle of each task is as follows:

[Read Acceptance Criteria (AC)] -> [Write Code + KDocs] -> [Write Tests in test/ folder] -> [Execute Local Validation] -> [Open PR and GitHub Integration]

### Mandatory commands before every git push
Open the Terminal tab at the bottom of Android Studio and run these three commands. If any fails, the GitHub Actions server will reject your code in the cloud (you can also find manual ways to run these in Android Studio):

```bash
# 1. Automatically format your code (visual aesthetics)
./gradlew ktlintFormat

# 2. Analyze semantics and enforce KDoc documentation
./gradlew detekt

# 3. Run all local unit and UI tests
./gradlew testDebugUnitTest
```

### How does GitHub Actions work? (Zero Cost)
Since our repository is PUBLIC and EDUCATIONAL, we have servers with free and unlimited minutes. When opening a Pull Request (PR):
1. Download: The server transparently simulates the merging of your branch with main.
2. Setup: It downloads the Android environment using cache to compile in less than 4 minutes.
3. Evaluation: It strictly executes the review commands mentioned above.
4. Report: If everything passes, the PR gets a green check. If something fails, it is blocked with a red X and publishes an interactive report with the exact line of the error. It is not necessary to download a teammate's branch to validate if it compiles.

---

## 2. Architecture and Dependency Location
To prevent every developer from altering Gradle versions in a chaotic way, the tool stack is managed centrally.
•
Global Tools (ktlint and detekt): These should be injected in bulk from the root file. We do not need to add them to submodules.
•
Testing Libraries: Versions live in the single gradle/libs.versions.toml file.

```toml
[versions]
mockk = "1.13.13" # referential, must be updated
robolectric = "4.14.1" # referential, must be updated

[libraries]
mockk = { group = "io.mockk", name = "mockk", version.ref = "mockk" }
robolectric = { group = "org.robolectric", name = "robolectric", version.ref = "robolectric" }

[bundles]
testing-stack = ["junit", "mockk", "robolectric"]
```
* To use them in a module, you only need to call the grouped package (bundle) in the dependencies of your build.gradle.kts file:

```kotlin
dependencies {
     // Inject JUnit, MockK, and Robolectric in a single command
     testImplementation(libs.bundles.testing.stack)

     // If your module handles screens with Jetpack Compose, add the UI rule:
     testImplementation("androidx.compose.ui:ui-test-junit4:1.7.0")
     debugImplementation(libs.test.compose.manifest)
}
```

---

## 3. Test Folder Structure per Module

Test files must mirror exactly the same package structure as the production layer (main), but within the test/java/ directory.
Observe this map of a typical feature module (e.g., :feature:login):

```text
feature/login/src/
├── main/java/com/yourproject/login/
│   ├── domain/
│   │   └── LoginUseCase.kt             <-- Production code
│   ├── data/
│   │   └── LoginRepositoryImpl.kt
│   └── presentation/
│       ├── LoginViewModel.kt           <-- Production code
│       └── LoginScreen.kt              <-- Compose View
│
└── test/java/com/yourproject/login/     <-- YOUR TESTS GO HERE
    ├── domain/
    │   └── LoginUseCaseTest.kt         <-- Uses Template A (MockK)
    ├── data/
    │   └── LoginRepositoryImplTest.kt
    └── presentation/
        ├── LoginViewModelTest.kt       <-- Uses Template B (MockK)
        └── LoginScreenTest.kt          <-- Uses Template C (Robolectric)
```

Golden Rule: The test file must be named exactly the same as the original component, adding the Test suffix. Example: LoginViewModel.kt -> LoginViewModelTest.kt.

---

## How to associate Unit Tests with Acceptance Criteria (AC)
We don't invent test cases. Each @Test method must be the direct technical reflection of an Acceptance Criterion detailed in your task card.
Semantic Structure: Given - When - Then
Each test function should be named and internationalized under the user behavior structure:
* given: The initial base scenario and the preparation of simulated data with MockK (as an option).
* when: The executable user action or the architecture method call (e.g., viewModel.onLoginClick()).
* then: The assertion or expected result (as dictated by the Acceptance Criterion) (e.g., assertTrue(result.isSuccess)).

---

## Sample Code Templates

### Template A: Domain Layer (Use Cases)
* Location: Pure Kotlin modules (without Android libraries).
* Focus: Testing calculation rules or business conditions.

```kotlin
package com.softserveacademy.feature.auth.login.domain

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests linked to the AC of the US-Auth.
 * Verifies the business logic of the login process.
 */
class LoginUseCaseTest {

    private val repository = mockk<LoginRepository>()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `given valid credentials when login then returns success result`() = runTest {
        // GIVEN: The repository returns success
        coEvery { repository.login("test@mail.com", "pass123") } returns Result.success(Unit)

        // WHEN: We execute the UseCase
        val result = useCase("test@mail.com", "pass123")

        // THEN: The result should be successful according to AC-1
        assertTrue(result.isSuccess)
    }
}
```

### Template B: Presentation Layer (ViewModels / MVVM)
* Location: Modules that expose loading, success, or error states to the UI.
* Focus: Validating that the flows and reactive data correctly update the visual state.

```kotlin
package com.softserveacademy.feature.auth.login.presentation

import com.softserveacademy.feature.auth.login.domain.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests linked to the AC of the US-Login.
 * Verifies that the UI reacts correctly to errors.
 */
class LoginViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()

    @Test
    fun `given network error when login is clicked then error state is updated`() = runTest {
        // GIVEN: The loginUseCase fails with a network error message
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception("Network Error"))

        // WHEN: The user attempts to log in the ViewModel
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onLoginClick()

        // THEN: The 'error' state of the ViewModel should capture the message
        assertEquals("Network Error", viewModel.error)
    }
}
```

### Template C: Presentation Layer (Compose Components WITHOUT Emulator)
* Location: Screens and reusable components of Jetpack Compose.
* Focus: Testing visual flows and clicks simulating Android on the PC via Robolectric (Cost 0 and high speed).

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
 * Validation of Design System components using Robolectric.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [37])
class TravelPrimaryButtonTest {

     @get:Rule
     val composeTestRule = createComposeRule()

     @Test
     fun `given enabled button when clicked then action lambda is executed`() {
          var isClicked = false

          // GIVEN: Enabled Design System button
          composeTestRule.setContent {
               Travelin2026ProjectLabTheme {
                    TravelPrimaryButton(
                         text = "Confirm",
                         onClick = { isClicked = true }
                    )
               }
          }

          // WHEN: User physically clicks
          composeTestRule.onNodeWithText("Confirm").performClick()

          // THEN: Verify the side effect (variable change)
          assert(isClicked)
     }
}
```

---

## 6. Prompt Context for Artificial Intelligence (Copilot / ChatGPT / Cursor / etc.)

To ensure the AI generates useful and standardized results, use this two-step flow. This ensures it doesn't just write code, but "understands" our documentation and testing standard.

### Step 1: Establish the Role (System Prompt)
Copy this when starting your chat to configure the AI's "mind":

> "Act as a Senior Android Developer for the Travelin2026 project. Your mission is to generate code, KDoc documentation, and unit tests under the following rules:
> 1. Architecture: Clean Architecture + MVVM + Modularization.
> 2. Testing: MockK, Coroutines Test, and Robolectric.
> 3. Naming: Given-When-Then structure for tests.
> 4. Base package: `com.softserveacademy`.
     > Confirm if you are ready to receive the code."

### Step 2: Execute the Task (Task Prompt)
Once the AI confirms, use this format to request the work on your current file:

> "Based on the code in this file/functionality:
> **[PASTE YOUR CODE OR EXPLAIN THE FUNCTIONALITY]**
>
> Please generate:
> 1. **Documentation:** Add descriptive KDocs in English to all classes and functions.
> 2. **Unit Tests:** Create the corresponding Test file following the project standard (Given-When-Then).
> 3. **Validation:** Ensure the tests cover both success and error flows."

---

## 7. Acceptance Criteria for the Pull Request (PR)

For your code to be approved, it must meet the following criteria:
1. **Compilation:** Passes `./gradlew assembleDebug`.
2. **Quality:** Passes `./gradlew detekt` and `./gradlew ktlintCheck`.
3. **Tests:** Each new functionality has its counterpart in the `test/` folder with logical coverage.
4. **Docs:** The generated code contains KDocs that explain the "why" and not just the "what".