# Analyticskit: General Context

"Turn user behavior into strategic insights."

## Overview
Analyticskit is a modular tool built for data collection and user behavior analysis. It streamlines the tracking of custom events, screen navigation, and conversion funnels, featuring a flexible architecture that allows sending data to multiple providers (Firebase, Mixpanel, or custom backends) simultaneously.

## Architecture
The library follows **Clean Architecture** principles to ensure maintainability, scalability, and testability.

### Layers and Patterns
- **Domain Layer**: Contains the core business logic using the **Repository** and **UseCase** patterns.
    - **Sealed Classes**: `AnalyticsEvent` defines extensible and type-safe analytics data.
    - **UseCase Interface**: Standardized pattern for all business operations returning `Flow<UseCaseOutput>`.
- **Presentation Layer (`sdk` folder)**: Houses the `AnalyticskitManager`, a Hilt-injectable singleton that acts as the main entry point. It depends **only** on UseCases to maintain strict decoupling.
- **Data Layer**:
    - **AnalyticsProvider Interface**: Abstraction for external services.
    - **AnalyticsDataSource**: Manages provider registration using `CopyOnWriteArrayList` for thread safety.
    - **Repository Implementation**: Coordinates execution without enforcing specific threading (delegated to UseCases via `flowOn`).

## Core Features
- **AnalyticskitManager**: A centralized manager that coordinates event tracking and provider lifecycle. It supports a fluent API for adding and removing providers dynamically at runtime.
- **Multi-Provider Support**: Supports multiple `AnalyticsProvider` implementations simultaneously. Data can be routed to all providers or targeted to a specific one using a unique key.
- **Rich Tracking**:
    - **Custom Events**: Track specific user actions with detailed metadata.
    - **Screen Navigation**: Track user movement through the app.
    - **Conversion Funnels**: Monitor user progression through critical business flows.
- **Library Agnostic**: The core library has zero third-party dependencies. Integrations (Firebase, Mixpanel, etc.) are implemented by the consumer application.

## Standards
- **Documentation**: All classes and public APIs are fully documented using **KDocs**.
- **Testing**: 100% logic coverage across all layers using:
    - **JUnit 4**
    - **MockK**
    - **Kotlin Coroutines Test**
- **Performance**: High-frequency operations are optimized for low overhead, utilizing structured concurrency and thread-safe collections.
