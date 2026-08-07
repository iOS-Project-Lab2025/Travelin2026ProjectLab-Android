# Travelin 2026 Project Lab

## Build Variants

This project uses Gradle Product Flavors and Build Types to manage different environments.

### Flavors

- **dev**: Development environment. Points to the Apiary mock backend.
- **staging**: Staging/Testing environment. Points to the Apiary mock backend.
- **prod**: Production environment. Points to the Apiary mock backend.

### Build Types

- **debug**: Debuggable build with `.debug` (or flavor-specific) package name suffix.
- **release**: Optimized build with R8/ProGuard enabled.

### Lab Demos

For lab demos, please use the **`devDebug`** or **`prodDebug`** variant. You can switch variants in Android Studio via the "Build Variants" tab.

### Backend URL

The backend URL is configured via `BuildConfig.BASE_URL` in the `core:data` module (and others). It is automatically set based on the selected flavor.

Current Base URL for all flavors: `https://private-amnesiac-923781-travelin1.apiary-mock.com/`
