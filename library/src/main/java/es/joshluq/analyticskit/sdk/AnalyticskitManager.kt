package es.joshluq.analyticskit.sdk

import android.util.Log
import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.data.repository.AnalyticsRepositoryImpl
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.usecase.AddGlobalPropertyUseCase
import es.joshluq.analyticskit.domain.usecase.AddProviderUseCase
import es.joshluq.analyticskit.domain.usecase.RemoveGlobalPropertyUseCase
import es.joshluq.analyticskit.domain.usecase.RemoveProviderUseCase
import es.joshluq.analyticskit.domain.usecase.TraceEventUseCase
import es.joshluq.analyticskit.domain.usecase.TrackEventUseCase
import es.joshluq.analyticskit.domain.usecase.TrackTracedEventUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Main entry point for the Analyticskit library. This manager coordinates event tracking and
 * provider management using UseCases.
 *
 * Use the [Builder] to create an instance of this manager.
 */
class AnalyticskitManager private constructor(
    private val trackEventUseCase: TrackEventUseCase,
    private val addProviderUseCase: AddProviderUseCase,
    private val removeProviderUseCase: RemoveProviderUseCase,
    private val addGlobalPropertyUseCase: AddGlobalPropertyUseCase,
    private val removeGlobalPropertyUseCase: RemoveGlobalPropertyUseCase,
    private val traceEventUseCase: TraceEventUseCase,
    private val trackTracedEventUseCase: TrackTracedEventUseCase,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Tracks an analytics event.
     *
     * @param event The event to be tracked.
     * @param providerKey The key of the provider to send the event to (optional).
     */
    fun track(
        event: AnalyticsEvent,
        providerKey: String? = null,
    ) {
        scope.launch {
            trackEventUseCase(TrackEventUseCase.Input(event, providerKey))
                .onFailure { Log.e(TAG, "Error tracking event: ${it.message}", it) }
        }
    }

    /**
     * Adds a new analytics provider.
     *
     * @param provider The provider to be added.
     */
    fun addProvider(provider: AnalyticsProvider) {
        scope.launch {
            addProviderUseCase(AddProviderUseCase.Input(provider))
                .onFailure { Log.e(TAG, "Error adding provider: ${it.message}", it) }
        }
    }

    /**
     * Removes an analytics provider.
     *
     * @param key The key of the provider to be removed.
     */
    fun removeProvider(key: String) {
        scope.launch {
            removeProviderUseCase(RemoveProviderUseCase.Input(key))
                .onFailure { Log.e(TAG, "Error removing provider: ${it.message}", it) }
        }
    }

    /**
     * Adds a global property to a specific provider or to all providers if no key is specified.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param providerKey The key of the specific provider (optional).
     */
    fun addGlobalProperty(key: String, value: Any, providerKey: String? = null) {
        scope.launch {
            addGlobalPropertyUseCase(AddGlobalPropertyUseCase.Input(key, value, providerKey))
                .onFailure { Log.e(TAG, "Error adding global property: ${it.message}", it) }
        }
    }

    /**
     * Removes a global property from a specific provider or from all providers if no key is specified.
     *
     * @param propertyKey The key of the property to be removed.
     * @param providerKey The key of the specific provider (optional).
     */
    fun removeGlobalProperty(propertyKey: String, providerKey: String? = null) {
        scope.launch {
            removeGlobalPropertyUseCase(RemoveGlobalPropertyUseCase.Input(propertyKey, providerKey))
                .onFailure { Log.e(TAG, "Error removing global property: ${it.message}", it) }
        }
    }

    /**
     * Accumulates properties for a specific event trace. This is useful for grouping information
     * from different screens before sending a final event.
     *
     * @param eventName The key identifying the trace (usually the final event name).
     * @param properties The properties to add to the trace.
     */
    fun traceEvent(eventName: String, properties: Map<String, Any>) {
        scope.launch {
            traceEventUseCase(TraceEventUseCase.Input(eventName, properties))
                .onFailure { Log.e(TAG, "Error tracing event: ${it.message}", it) }
        }
    }

    /**
     * Tracks a traced event with all accumulated properties and clears the trace.
     *
     * @param eventName The key identifying the trace.
     * @param providerKey The key of the provider to send the event to (optional).
     */
    fun trackTracedEvent(eventName: String, providerKey: String? = null) {
        scope.launch {
            trackTracedEventUseCase(TrackTracedEventUseCase.Input(eventName, providerKey))
                .onFailure { Log.e(TAG, "Error tracking traced event: ${it.message}", it) }
        }
    }

    /**
     * Builder class for [AnalyticskitManager].
     */
    class Builder {
        private val providers = mutableListOf<AnalyticsProvider>()

        /**
         * Adds an initial provider to the manager.
         *
         * @param provider The provider to be added.
         * @return The builder instance.
         */
        fun addProvider(provider: AnalyticsProvider) = apply {
            providers.add(provider)
        }

        /**
         * Builds the [AnalyticskitManager] instance.
         *
         * @return A new instance of [AnalyticskitManager].
         */
        fun build(): AnalyticskitManager {
            val dataSource = AnalyticsDataSource()
            providers.forEach { dataSource.addProvider(it) }

            val repository = AnalyticsRepositoryImpl(dataSource)

            return AnalyticskitManager(
                trackEventUseCase = TrackEventUseCase(repository),
                addProviderUseCase = AddProviderUseCase(repository),
                removeProviderUseCase = RemoveProviderUseCase(repository),
                addGlobalPropertyUseCase = AddGlobalPropertyUseCase(repository),
                removeGlobalPropertyUseCase = RemoveGlobalPropertyUseCase(repository),
                traceEventUseCase = TraceEventUseCase(repository),
                trackTracedEventUseCase = TrackTracedEventUseCase(repository),
            )
        }
    }

    companion object {
        private const val TAG = "AnalyticskitManager"
    }
}
