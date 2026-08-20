# Travelin 2026 Project Lab

## Build Variants

This project uses Gradle Product Flavors and Build Types to manage different environments.

### Flavors

- **dev**: Development environment. Points to the Apiary mock backend.
- **staging**: Staging/Testing environment. Points to the Render production backend.
- **prod**: Production environment. Points to the Render production backend.

### Signing & Secrets

For information on how to configure signing for release builds and manage secrets like API keys (Google Maps, Koog AI, Stripe), see [SIGNING.md](SIGNING.md).

| Feature | Key Required | Purpose |
| :--- | :--- | :--- |
| **Maps** | `MAPS_API_KEY` | Google Maps rendering and Places API. |
| **Aira Assistant** | `AI_KEY` | Gemini LLM integration for travel suggestions. |
| **Payments** | `STRIPE_PUBLISHABLE_KEY` | Frontend Stripe SDK initialization. |
| **Payments** | `STRIPE_SECRET_KEY` | Backend/Mock PaymentIntent creation. |

### Build Types

- **debug**: Debuggable build with `.debug` (or flavor-specific) package name suffix.
- **release**: Optimized build with R8/ProGuard enabled.

### Lab Demos

For lab demos, please use the **`devDebug`** or **`prodDebug`** variant. You can switch variants in Android Studio via the "Build Variants" tab.

### Backend URL

The backend URL is configured via `BuildConfig.BASE_URL` in the `core:data` module (and others). It is automatically set based on the selected flavor.

- **dev**: `https://private-amnesiac-923781-travelin1.apiary-mock.com/`
- **staging** & **prod**: `https://travelin2026projectlab-android-api.onrender.com/`

#### Local Override
You can override the backend URL for all flavors by adding the following line to your `local.properties` file:
```properties
BASE_URL="https://your-custom-url.com/"
```
This is useful for testing local backend instances or specific environment versions without changing the source code.
