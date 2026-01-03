# Analyticskit: General Context

"Turn user behavior into strategic insights."

## Overview
Analyticskit is a modular tool built for data collection and user behavior analysis. It streamlines the tracking of custom events, screen navigation, and conversion funnels, featuring a flexible architecture that allows sending data to multiple providers (Firebase, Mixpanel, or custom backends) simultaneously.

## Architecture
The library follows **Clean Architecture** principles to ensure maintainability, scalability, and testability.

### Layers and Patterns
- **Domain Layer**: Contains the core business logic using the **Repository** and **UseCase** patterns.
    - **Sealed Classes**: `AnalyticsEvent` defines extensible and type-safe analytics data.
    - **UseCase Interface**: Standardized pattern for all business operations returning `Result<UseCaseOutput>`.
    - **Internal Visibility**: Domain logic is encapsulated and not exposed to the consumer.
- **Presentation Layer (`sdk` folder)**: Houses the `AnalyticskitManager`, which acts as the main entry point. It depends on internal UseCases to maintain strict decoupling.
- **Builder Pattern**: The manager is instantiated via a `Builder` to centralize dependency orchestration without external DI frameworks.
- **Data Layer**:
    - **AnalyticsProvider Interface**: Public abstraction for external services.
    - **AnalyticsDataSource**: Manages provider registration using `CopyOnWriteArrayList` for thread safety (Internal).
    - **Repository Implementation**: Coordinates execution (Internal).

## Core Features
- **AnalyticskitManager**: A centralized manager that coordinates event tracking and provider lifecycle. It supports a fluent API for adding and removing providers dynamically at runtime.
- **Multi-Provider Support**: Supports multiple `AnalyticsProvider` implementations simultaneously. Data can be routed to all providers or targeted to a specific one using a unique key.
- **Rich Tracking**:
    - **Custom Events**: Track specific user actions with detailed metadata.
    - **Screen Navigation**: Track user movement through the app.
    - **Conversion Funnels**: Monitor user progression through critical business flows.
- **Zero Dependencies**: The library has no third-party dependencies (no Hilt, no Koin). It's lightweight and easy to integrate.

## Standards
- **Documentation**: All classes and public APIs are fully documented using **KDocs**.
- **Testing**: 100% logic coverage across all layers using:
    - **JUnit 4**
    - **MockK**
    - **Kotlin Coroutines Test**
- **Performance**: Synchronous execution returning `Result` and thread-safe collections for high-frequency operations.

```
