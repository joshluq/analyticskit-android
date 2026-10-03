---
name: analyticskit-feature-scaffold
description: >-
  Step-by-step procedural guide to implement or extend features in Analyticskit Android,
  enforcing Clean Architecture, internal visibility, fire-and-forget API, KDocs, and 100% test coverage.
  Use when adding new event types, use cases, manager methods, or data operations.
---

# Analyticskit Feature Implementation Guide

Use this skill when introducing or modifying features, event types, or operational capabilities in the `analyticskit-android` library. Every feature must comply with Clean Architecture and the standards defined in `AGENTS.md`.

---

## Architectural Workflow (Step-by-Step)

```text
Domain Layer (Sealed Events & internal UseCases returning Result<O>)
      │
      ▼
Data Layer (internal Repository & DataSource, public Provider interface)
      │
      ▼
SDK / Presentation Layer (AnalyticskitManager fire-and-forget API with CoroutineScope)
      │
      ▼
KDocs & 100% Unit Tests (JUnit 4 + MockK + Coroutines Test)
```

---

## Step 1: Domain Layer

### 1.1 Sealed Event Models (if adding new event structures)
- Path: `library/src/main/java/es/joshluq/analyticskit/domain/model/`
- Extend `AnalyticsEvent` using sealed classes/interfaces to ensure type-safety and immutability.
- Keep properties immutable (`val`).

### 1.2 Use Cases
- Path: `library/src/main/java/es/joshluq/analyticskit/domain/usecase/`
- Must implement the standard `UseCase<Input, Output>` interface.
- Return type **must** be `Result<Output>`.
- **Visibility rule**: All UseCases must be marked as `internal`. They must never be exposed directly to the library consumer.
- Accept repositories via constructor injection.

```kotlin
internal class MyNewUseCase(
    private val repository: AnalyticsRepository
) : UseCase<MyInput, Unit> {
    override suspend fun invoke(input: MyInput): Result<Unit> {
        return repository.performOperation(input)
    }
}
```

---

## Step 2: Data Layer

### 2.1 Repository Interface & Implementation
- Interface: `domain/repository/AnalyticsRepository.kt` (`internal`)
- Implementation: `data/repository/AnalyticsRepositoryImpl.kt` (`internal`)
- Coordinates business logic between the UseCases and `AnalyticsDataSource`.

### 2.2 Data Source
- Path: `data/datasource/AnalyticsDataSource.kt` (`internal`)
- Manages state, registered providers, and provider execution.
- **Thread Safety**: Use `ConcurrentHashMap` for key-value maps and `CopyOnWriteArrayList` for collections. Never use non-thread-safe collections without proper synchronization.

---

## Step 3: SDK / Presentation Layer (`AnalyticskitManager`)

- Path: `library/src/main/java/es/joshluq/analyticskit/sdk/AnalyticskitManager.kt`
- Serves as the single public entry point for library consumers.

### Rules for Manager Methods:
1. **Synchronous-looking / Fire-and-Forget**:
   Consumers should call methods synchronously without needing `suspend` or managing coroutines themselves.
2. **Internal CoroutineScope**:
   Launch execution inside the manager's internal scope (`CoroutineScope(dispatcherProvider.main + SupervisorJob())`).
3. **Automatic Error Handling**:
   Handle asynchronous failures via `Result.onFailure` and log errors to Logcat using the internal logger.
4. **Builder Configuration**:
   If the feature introduces new configuration options, expose them via `AnalyticskitManager.Builder`.

```kotlin
/**
 * Executes the new feature operation asynchronously.
 *
 * @param param Detailed explanation of param.
 */
fun performNewFeature(param: String) {
    scope.launch {
        myNewUseCase(param).onFailure { error ->
            logger.e(TAG, "Failed to perform new feature", error)
        }
    }
}
```

---

## Step 4: Documentation (KDocs)

- **Mandatory**: Every public class, interface, method, and property must have complete KDocs.
- Document parameter semantics, default values, return types, and potential side-effects.

---

## Step 5: Unit Testing (100% Logic Coverage)

- Path: `library/src/test/java/es/joshluq/analyticskit/`
- Every layer must have a corresponding test class:
  - `domain/usecase/MyNewUseCaseTest.kt`
  - `data/repository/AnalyticsRepositoryImplTest.kt`
  - `sdk/AnalyticskitManagerTest.kt`
- **Frameworks**: JUnit 4, MockK (`mockk`, `coEvery`, `coVerify`), Kotlin Coroutines Test (`StandardTestDispatcher`, `runTest`).
- Verify both success paths (`Result.success`) and failure paths (`Result.failure`).
- Check that all conditions, branches, and exception catches are executed.

---

## Step 6: Verification

Before completing the task, invoke the `analyticskit-quality-check` skill:
```powershell
$env:JAVA_HOME = "C:\Users\josh_\AppData\Local\Programs\Android Studio\jbr"
.\gradlew.bat spotlessApply :analyticskit:testDebugUnitTest :analyticskit:koverVerifyDebug :showcase:assembleDebug
```
