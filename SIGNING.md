# Signing Configuration

This document explains how to configure signing for local release builds and CI environments.

## Debug Signing

Debug builds work out-of-the-box. They use the default Android debug keystore located at `~/.android/debug.keystore`.

## Release Signing

To sign release builds locally, you must provide keystore information via `local.properties`.

### 1. Local Configuration (`local.properties`)

Add the following properties to your `local.properties` file in the project root:

```properties
KEYSTORE_PATH=/path/to/your/release-keystore.jks
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=your_key_alias
KEY_PASSWORD=your_key_password
```

Refer to `local.properties.example` for a template.

## Secrets & API Keys

The project requires several API keys to function correctly. These should be provided in `local.properties` for local development and as environment variables/GitHub Secrets for CI.

### 1. Google Maps API Key (`MAPS_API_KEY`)
Used for displaying maps and POIs in the Hotel Details and Exploration screens.
- **Provider**: Google Maps Platform.

### 2. Koog AI Key (`AI_KEY`)
Used by the Aira Assistant to process voice queries and provide travel recommendations.
- **Provider**: Google AI Studio (Gemini API).

### 3. Stripe Keys (`STRIPE_SECRET_KEY`, `STRIPE_PUBLISHABLE_KEY`)
Used for processing payments in the booking flows.
- **Provider**: Stripe Dashboard.

## CI/Environment Variables

For CI environments (like GitHub Actions), these secrets can be provided as environment variables:

- `KEYSTORE_PATH`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`
- `MAPS_API_KEY`
- `AI_KEY`
- `STRIPE_SECRET_KEY`
- `STRIPE_PUBLISHABLE_KEY`

### Build Logic Fallback

If any of the release signing properties are missing, the build will fall back to using the `debug` signing configuration. This ensures that developers can still run release builds locally for testing purposes without needing the official release keystore.

## Security Note

**NEVER** commit your `local.properties` file or any keystore files to the repository. The `.gitignore` file is already configured to exclude `local.properties`.
