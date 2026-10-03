---
name: analyticskit-provider-integration
description: >-
  Procedures and best practices for creating, integrating, and testing analytics providers
  (e.g., Firebase, Mixpanel, Custom Backend) in Analyticskit Android without compromising
  zero-dependency and thread-safety requirements.
---

# Analyticskit Provider Integration Runbook

Use this skill when implementing a new `AnalyticsProvider`, integrating external services (e.g., Firebase, Mixpanel, custom telemetry backend), or testing provider routing.

---

## Strict Architectural Rules

1. **Zero Dependencies in Core (`:library`)**:
   - The `:library` module must NEVER depend on Firebase, Mixpanel, or third-party analytics SDKs.
   - Provider implementations for third-party SDKs must reside either in the consumer application, in `:showcase`, or in dedicated extension modules.
2. **Thread Safety**:
   - Provider instances are invoked concurrently across coroutines.
   - Any internal caching or state (such as global properties) **must** use thread-safe data structures like `ConcurrentHashMap`.
3. **Failure Isolation**:
   - Errors inside a provider must never bring down the host application. Exceptions thrown in `track()` are caught by the manager and logged.

---

## Step 1: Implement the `AnalyticsProvider` Interface

Reference interface: `es.joshluq.analyticskit.data.provider.AnalyticsProvider`

```kotlin
class MyCustomAnalyticsProvider(
    override val key: String = "MY_CUSTOM_PROVIDER"
) : AnalyticsProvider {

    private val globalProperties = ConcurrentHashMap<String, Any>()

    override suspend fun track(event: AnalyticsEvent) {
        val eventProperties = when (event) {
            is AnalyticsEvent.Custom -> event.properties
            is AnalyticsEvent.FunnelStep -> event.properties
            is AnalyticsEvent.ScreenView -> emptyMap()
        }
        val mergedProperties = HashMap(globalProperties).apply { putAll(eventProperties) }

        when (event) {
            is AnalyticsEvent.Custom -> {
                // Forward to external SDK or network client
            }
            is AnalyticsEvent.ScreenView -> {
                // Forward screen tracking
            }
            is AnalyticsEvent.FunnelStep -> {
                // Forward funnel step tracking
            }
        }
    }

    override fun addGlobalProperty(key: String, value: Any) {
        globalProperties[key] = value
    }

    override fun removeGlobalProperty(key: String) {
        globalProperties.remove(key)
    }
}
```

---

## Step 2: Provider Registration

### Method A: Builder Registration (Initialization time)
```kotlin
val manager = AnalyticskitManager.Builder()
    .addProvider(MyCustomAnalyticsProvider())
    .build()
```

### Method B: Dynamic Registration (Runtime)
```kotlin
val provider = MyCustomAnalyticsProvider()
manager.addProvider(provider)

// Later removal:
manager.removeProvider(provider.key)
```

---

## Step 3: Verifying Routing Modes

Analyticskit supports two distinct routing behaviors:

1. **Broadcast Routing (Default)**:
   ```kotlin
   // Dispatches to ALL registered providers
   manager.track(AnalyticsEvent.Custom("button_clicked", mapOf("id" to "checkout")))
   ```

2. **Targeted Routing (By Provider Key)**:
   ```kotlin
   // Dispatches ONLY to the specified provider
   manager.track(
       event = AnalyticsEvent.Custom("secret_event", emptyMap()),
       providerKey = "MY_CUSTOM_PROVIDER"
   )
   ```

---

## Step 4: Testing & Verification

1. **Unit Testing Provider Isolation**:
   - Create mock/fake implementations using `MockK`.
   - Verify that adding multiple providers with distinct keys dispatches correctly in `AnalyticsDataSourceTest`.
2. **Showcase Validation**:
   - For UI-driven verification, test the provider inside the `:showcase` module using `ConsoleAnalyticsProvider` as reference (`showcase/src/main/java/es/joshluq/analyticskit/showcase/ConsoleAnalyticsProvider.kt`).
3. **Run Pre-Flight Quality Check**:
   ```powershell
   $env:JAVA_HOME = "C:\Users\josh_\AppData\Local\Programs\Android Studio\jbr"
   .\gradlew.bat :analyticskit:testDebugUnitTest :showcase:assembleDebug
   ```
