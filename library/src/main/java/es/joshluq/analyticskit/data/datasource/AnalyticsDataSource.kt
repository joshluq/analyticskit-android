package es.joshluq.analyticskit.data.datasource

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/** Data source for managing and dispatching events to multiple analytics providers. */
internal class AnalyticsDataSource {
    private val providers = CopyOnWriteArrayList<AnalyticsProvider>()
    private val traces = ConcurrentHashMap<String, MutableMap<String, Any>>()

    /**
     * Adds a new analytics provider to the list.
     *
     * @param provider The provider to be added.
     */
    fun addProvider(provider: AnalyticsProvider) {
        if (providers.none { it.key == provider.key }) {
            providers.add(provider)
        }
    }

    /**
     * Removes an analytics provider from the list.
     *
     * @param key The key of the provider to be removed.
     */
    fun removeProvider(key: String) {
        providers.removeIf { it.key == key }
    }

    /**
     * Sends an event to registered providers.
     *
     * @param event The event to be sent.
     * @param providerKey The key of the provider to send the event to (optional).
     */
    suspend fun sendEvent(
        event: AnalyticsEvent,
        providerKey: String? = null,
    ) {
        if (providerKey != null) {
            providers.firstOrNull { it.key == providerKey }?.track(event)
        } else {
            for (provider in providers) {
                provider.track(event)
            }
        }
    }

    /**
     * Adds a global property to a specific provider or to all providers if no key is specified.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param providerKey The key of the provider (optional).
     */
    fun addGlobalProperty(
        key: String,
        value: Any,
        providerKey: String? = null,
    ) {
        if (providerKey != null) {
            providers.firstOrNull { it.key == providerKey }?.addGlobalProperty(key, value)
        } else {
            for (provider in providers) {
                provider.addGlobalProperty(key, value)
            }
        }
    }

    /**
     * Removes a global property from a specific provider or from all providers if no key is specified.
     *
     * @param propertyKey The key of the property to be removed.
     * @param providerKey The key of the provider (optional).
     */
    fun removeGlobalProperty(
        propertyKey: String,
        providerKey: String? = null,
    ) {
        if (providerKey != null) {
            providers.firstOrNull { it.key == providerKey }?.removeGlobalProperty(propertyKey)
        } else {
            for (provider in providers) {
                provider.removeGlobalProperty(propertyKey)
            }
        }
    }

    /**
     * Accumulates properties for a specific event trace.
     *
     * @param eventName The key identifying the trace (usually the final event name).
     * @param properties The properties to add to the trace.
     */
    fun traceEvent(
        eventName: String,
        properties: Map<String, Any>,
    ) {
        val trace = traces.getOrPut(eventName) { ConcurrentHashMap() }
        trace.putAll(properties)
    }

    /**
     * Tracks a traced event with all accumulated properties and clears the trace.
     *
     * @param eventName The key identifying the trace.
     * @param providerKey The key of the provider (optional).
     */
    suspend fun trackTracedEvent(
        eventName: String,
        providerKey: String? = null,
    ) {
        val accumulatedProperties = traces.remove(eventName) ?: emptyMap()
        val event = AnalyticsEvent.Custom(eventName, accumulatedProperties)
        sendEvent(event, providerKey)
    }

    /**
     * Clears an event trace without tracking it.
     *
     * @param eventName The key identifying the trace.
     */
    fun clearTrace(eventName: String) {
        traces.remove(eventName)
    }
}
