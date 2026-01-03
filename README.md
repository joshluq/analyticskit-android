# Analyticskit

**"Turn user behavior into strategic insights."**

Analyticskit is a modular tool built for data collection and user behavior analysis. It streamlines the tracking of custom events, screen navigation, and conversion funnels, featuring a flexible architecture that allows sending data to multiple providers (Firebase, Mixpanel, or custom backends) simultaneously.

## 🚀 Key Features

- **Multi-Provider Support**: Route data to multiple analytics consumers (Firebase, Mixpanel, etc.) simultaneously.
- **Dynamic Management**: Add or remove providers at runtime based on user preferences or app state.
- **Rich Event Tracking**: Track custom events, screen views, and conversion funnels with type-safe models.
- **Clean Architecture**: Strict separation of concerns with a UseCase-driven approach.
- **Zero Dependencies**: Lightweight library with no third-party dependencies (Hilt-free).
- **High Performance**: Optimized for low overhead using thread-safe collections and efficient result handling.

## 🏗 Architecture

Analyticskit follows **Clean Architecture** to ensure isolation of tracking logic and ease of expansion to new analytics platforms.

```mermaid
graph TD
    subgraph "Presentation Layer (SDK)"
        M[AnalyticskitManager]
        B[AnalyticskitManager.Builder]
    end

    subgraph "Domain Layer (Internal)"
        UC[UseCases: Track, Add, Remove]
        R[AnalyticsRepository Interface]
    end

    subgraph "Public Models"
        MOD[AnalyticsEvent Sealed Class]
        P[AnalyticsProvider Interface]
    end

    subgraph "Data Layer (Internal)"
        RepoImpl[AnalyticsRepository Implementation]
        DS[AnalyticsDataSource]
    end

    subgraph "Consumer Application"
        ImplP[FirebaseProvider Implementation]
        ExtLib[Firebase SDK]
    end

    B --> M
    M --> UC
    UC --> R
    RepoImpl -- implements --> R
    RepoImpl --> DS
    DS --> P
    ImplP -- implements --> P
    ImplP --> ExtLib
```

## 🛠 Usage Example

### 1. Implement a Provider
Create a bridge between Analyticskit and your preferred service by implementing `AnalyticsProvider`.

```kotlin
class ConsoleAnalyticsProvider : AnalyticsProvider {
    override val key: String = "CONSOLE_PROVIDER"

    override suspend fun track(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.Custom -> println("Event: ${event.name}")
            is AnalyticsEvent.ScreenView -> println("Screen: ${event.screenName}")
            is AnalyticsEvent.FunnelStep -> println("Funnel: ${event.funnelName}")
        }
    }
}
```

### 2. Initialize and Manage Providers
Use the `Builder` to create an instance of `AnalyticskitManager`.

```kotlin
class MyApp : Application() {
    lateinit var analyticskitManager: AnalyticskitManager

    override fun onCreate() {
        super.onCreate()
        
        // Initialize via Builder
        analyticskitManager = AnalyticskitManager.Builder()
            .addProvider(ConsoleAnalyticsProvider())
            .build()
    }
}
```

### 3. Track Events
Track data from any part of your application.

```kotlin
// Track a custom event globally
analyticskitManager.track(
    AnalyticsEvent.Custom("purchase_completed", mapOf("price" to 19.99))
)

// Target a specific provider
analyticskitManager.track(
    event = AnalyticsEvent.Custom("debug_event"),
    providerKey = "CONSOLE_PROVIDER"
)
```

## 📂 Project Structure

- `:analyticskit`: The core library module.
    - `sdk`: Public API (`AnalyticskitManager` and `Builder`).
    - `domain`: Internal business logic and public `AnalyticsEvent` models.
    - `data`: Internal repository implementation and public `Provider` abstractions.
- `:showcase`: A sample app demonstrating dynamic provider toggling and event tracking.

## ⚙️ Configuration

The project uses a `config/project-config.properties` file for centralized configuration:

- `catalogVersion`: Version of the shared dependency catalog.
- `libraryVersion`: Current version of the Analyticskit library.

## 🧪 Quality Assurance

- **KDocs**: 100% API documentation for public members.
- **Unit Testing**: 100% logic coverage using **JUnit 4**, **MockK**, and **Coroutines Test**.
- **Performance**: Thread-safe provider management using `CopyOnWriteArrayList` and synchronous result handling via `Result<T>`.

---

*Developed with focus on scalability and data precision.*
