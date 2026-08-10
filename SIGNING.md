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

### 2. CI/Environment Variables

For CI environments (like GitHub Actions), these secrets can be provided as environment variables:

- `KEYSTORE_PATH`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

### Build Logic Fallback

If any of the release signing properties are missing, the build will fall back to using the `debug` signing configuration. This ensures that developers can still run release builds locally for testing purposes without needing the official release keystore.

## Security Note

**NEVER** commit your `local.properties` file or any keystore files to the repository. The `.gitignore` file is already configured to exclude `local.properties`.
