# Analyticskit: General Context

"Turn user behavior into strategic insights."

## Overview
Analyticskit is a modular tool built for data collection and user behavior analysis. It streamlines the tracking of custom events, screen navigation, and conversion funnels, featuring a flexible architecture that allows sending data to multiple providers (Firebase, Mixpanel, or custom backends) simultaneously.

## Architecture
The library follows **Clean Architecture** principles to ensure maintainability, scalability, and testability.

### Layers and Patterns
- **Domain Layer**: Contains the core business logic using the **Repository** and **UseCase** patterns. It defines extensible and type-safe analytics data structures.
- **Presentation Layer**: Houses the `AnalyticskitManager`, a Hilt-injectable singleton that acts as the main entry point for the library.
- **Data Layer**: Defines the `AnalyticsProvider` interface and manages multiple provider implementations for simultaneous data dispatch.

## Core Features
- **AnalyticskitManager**: A centralized manager that coordinates event tracking and screen navigation. It supports a fluent API for adding providers dynamically.
- **Multi-Provider Support**: Supports multiple `AnalyticsProvider` implementations simultaneously. Data can be routed to all providers or targeted to specific ones.
- **Rich Tracking**:
    - **Custom Events**: Track specific user actions with detailed metadata.
    - **Screen Navigation**: Track user movement through the app.
    - **Conversion Funnels**: Monitor user progression through critical business flows.
- **Library Agnostic**: The core library focuses on the tracking logic. Specific integrations (Firebase, Mixpanel, etc.) are implemented as providers.

## Standards
- **Documentation**: All classes and public APIs are fully documented using **KDocs**.
- **Testing**: Robust unit testing with high logic coverage using:
    - **JUnit**
    - **MockK**
    - **Kotlin Coroutines Test**
- **Performance**: Optimized for minimal impact on application performance and thread safety.
