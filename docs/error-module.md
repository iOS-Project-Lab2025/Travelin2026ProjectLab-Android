# Módulo `:core:error` — Hoja informativa

## Resumen de la implementación

Se creó un módulo independiente (`core/error`) que centraliza el modelado, mapeo y manejo de errores en toda la app. Sigue los principios SOLID y está diseñado como un módulo JVM puro (sin dependencias de Android) para que pueda ser usado tanto por la capa de dominio como por datos y presentación.

### Estructura principal

| Paquete | Contenido |
|---------|-----------|
| `model/` | `AppError` (jerarquía sellada), `AppResult` (Success/Failure), `ErrorAction` (qué hacer en UI), `UiText` (texto localizable) |
| `mapper/` | `ExceptionMapperPlugin` (interfaz extensible), `ExceptionMapper` (motor que recorre plugins), `RetrofitExceptionMapperPlugin` (mapeo HTTP vía reflexión) |
| `handler/` | `ErrorHandler` (interfaz), `DefaultErrorHandler` (implementación exhaustiva), `GlobalErrorHandler` (errores globales con Channel/Flow) |
| `extension/` | `ResultExtensions` (`onSuccess`, `onFailure`, `map`, `flatMap`, `recover`, `getOrNull`, `getOrDefault`) |
| `util/` | `safeCall` (envuelve bloques suspendidos, reconvierte excepciones en `AppResult`) |

Las configuraciones de Hilt (`@Module`, `@InstallIn`) residen en `core/data/di/ErrorDataModule.kt` para mantener el módulo de error libre de Android.

---

## ¿Por qué se implementó?

Antes de este módulo, el manejo de errores en la app era inconsistente:

- Algunos repositorios usaban `Result<T>` de Kotlin
- Otros lanzaban excepciones directamente (`throw NoSuchElementException`)
- Los ViewModels tenían `try/catch` dispersos con lógica duplicada
- No había una forma uniforme de mapear excepciones técnicas a errores de dominio

El módulo `core:error` resuelve esto proporcionando **un lenguaje común para errores** en toda la arquitectura.

---

## Ventajas

1. **Separación de responsabilidades (SRP):** `AppError` describe *qué pasó* (ej. `Network.NoConnection`); la presentación decide *qué mostrar*. No mezclan.

2. **Extensible sin modificar (OCP):** Para agregar soporte a un nuevo framework (Ktor, GraphQL), solo se crea un nuevo `ExceptionMapperPlugin` y se registra con `@Binds @IntoSet`. No se toca el módulo core.

3. **Contrato seguro (LSP):** `ErrorHandler.handle()` siempre retorna `ErrorAction` (nunca `null`). El `when` exhaustivo sobre `AppError` sellado fuerza a cubrir todos los casos.

4. **Interfaces segregadas (ISP):** Los errores globales (auth, sesión) se manejan en `GlobalErrorHandler` a nivel Activity; los errores locales (de pantalla) se manejan con `ErrorHandler` en cada ViewModel.

5. **Inversión de dependencias (DIP):** Los repositorios dependen de `AppResult` y `ExceptionMapper`, no de Retrofit, Room ni ningún framework específico.

6. **Compilación segura:** Al ser `AppError` un `sealed interface`, agregar un nuevo subtipo rompe todos los `when` en tiempo de compilación. No hay errores silenciosos.

7. **Localización lista:** `UiText.Resource(resId, args)` permite usar strings de recursos Android sin acoplar el dominio a Android.

---

## Cómo usarlo en nuevas features

### 1. En la capa de dominio (interfaz de repositorio)

```kotlin
interface UserRepository {
    suspend fun getUser(id: String): AppResult<User>
    suspend fun updateUser(user: User): AppResult<Unit>
}
```

### 2. En la capa de datos (implementación)

```kotlin
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val mapper: ExceptionMapper
) : UserRepository {

    override suspend fun getUser(id: String): AppResult<User> =
        safeCall(mapper) { api.getUser(id).toDomain() }
}
```

### 3. En el ViewModel

```kotlin
fun loadUser(id: String) {
    viewModelScope.launch {
        getUserUseCase(id)
            .onSuccess { user ->
                _state.value = UserState.Loaded(user)
            }
            .onFailure { error ->
                when (error) {
                    is AppError.Auth -> globalErrorHandler.dispatchAuth(error)
                    else -> _state.value = UserState.Error(
                        errorHandler.handle(error)
                    )
                }
            }
    }
}
```

### 4. Agregar un nuevo plugin (ej. para Ktor)

```kotlin
class KtorExceptionMapperPlugin @Inject constructor() : ExceptionMapperPlugin {
    override fun map(throwable: Throwable): AppError? {
        if (throwable !is KtorException) return null
        return when (throwable.status) {
            HttpStatusCode.Unauthorized -> AppError.Auth.SessionExpired
            HttpStatusCode.NotFound -> AppError.Data.NotFound("resource")
            else -> AppError.Network.Server(throwable.status.value)
        }
    }
}
```

Registrarlo en un módulo Hilt con `@Binds @IntoSet`.

### 5. Dependencia Gradle

Cada módulo que necesite `AppResult` o `AppError` debe tener acceso a `core:error` (transitivo vía `core:domain` que usa `api`). Módulos Android que necesiten los handlers de Hilt deben depender de `core:data` o agregar su propio módulo DI.

---

## Árbol de archivos del módulo

```
core/error/
├── build.gradle.kts
└── src/main/java/com/softserveacademy/core/error/
    ├── model/
    │   ├── AppError.kt
    │   ├── AppResult.kt
    │   ├── ErrorAction.kt
    │   └── UiText.kt
    ├── mapper/
    │   ├── ExceptionMapperPlugin.kt
    │   ├── ExceptionMapper.kt
    │   └── RetrofitExceptionMapperPlugin.kt
    ├── handler/
    │   ├── ErrorHandler.kt
    │   ├── DefaultErrorHandler.kt
    │   └── GlobalErrorHandler.kt
    ├── extension/
    │   └── ResultExtensions.kt
    └── util/
        └── SafeCall.kt
```
