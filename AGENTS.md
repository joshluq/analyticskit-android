# Analyticskit: General Context

"Turn user behavior into strategic insights."

## Overview
Analyticskit is a modular tool built for data collection and user behavior analysis. It streamlines the tracking of custom events, screen navigation, and conversion funnels, featuring a flexible architecture that allows sending data to multiple providers (Firebase, Mixpanel, or custom backends) simultaneously.

## Architecture
The library follows **Clean Architecture** principles to ensure maintainability, scalability, and testability.

### Layers and Patterns
- **Domain Layer**: Contains the core business logic using the **Repository** and **UseCase** patterns.
    - **Sealed Classes**: `AnalyticsEvent` defines extensible and type-safe analytics data.
    - **UseCase Interface**: Standardized pattern for all business operations returning `Result<O>`. All UseCases are marked as `internal`.
    - **Internal Visibility**: Domain logic is encapsulated and not exposed to the consumer.
- **Presentation Layer (`sdk` folder)**: Houses the `AnalyticskitManager`, which acts as the main entry point.
    - **Synchronous API**: The manager provides a synchronous-looking API by managing an internal `CoroutineScope` (Main + SupervisorJob).
    - **Fire-and-Forget**: All tracking and management calls are non-blocking, executing asynchronously and logging failures automatically via `Result.onFailure`.
- **Builder Pattern**: The manager is instantiated via a `Builder` to centralize dependency orchestration without external DI frameworks.
- **Data Layer**:
    - **AnalyticsProvider Interface**: Public abstraction for external services.
    - **AnalyticsDataSource**: Manages provider registration and state (Internal).
    - **Repository Implementation**: Coordinates execution between UseCases and DataSource (Internal).

## Core Features
- **AnalyticskitManager**: A centralized manager that coordinates event tracking, provider lifecycle, and global state.
- **Multi-Provider Support**: Supports multiple `AnalyticsProvider` implementations simultaneously. Data can be routed to all providers or targeted to a specific one using a unique key.
- **Global Properties**: Allows setting properties at the manager level that are propagated to all subsequent events.
- **Event Tracing (Stateful Tracking)**: Accumulates properties for a specific event key across multiple steps or screens before sending a consolidated final event.
- **Zero Dependencies**: The library has no third-party dependencies. It is lightweight, thread-safe (using `ConcurrentHashMap` and `CopyOnWriteArrayList`), and easy to integrate.

## Standards
- **Documentation**: All classes and public APIs are fully documented using **KDocs**.
- **Testing**: 100% logic coverage across all layers using JUnit 4, MockK, and Kotlin Coroutines Test.
- **Observability**: Built-in error logging to Logcat for all asynchronous operations failure.
- **Thread Safety**: High-concurrency support for multi-provider environments.
