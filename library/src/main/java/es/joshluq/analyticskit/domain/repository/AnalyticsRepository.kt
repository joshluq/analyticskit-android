package es.joshluq.analyticskit.domain.repository

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent

/** Interface representing the repository for analytics operations. */
internal interface AnalyticsRepository {
    /**
     * Tracks an analytics event.
     *
     * @param event The event to be tracked.
     * @param providerKey The key of the provider to send the event to (optional).
     */
    suspend fun track(
        event: AnalyticsEvent,
        providerKey: String? = null,
    )

    /**
     * Adds an analytics provider.
     *
     * @param provider The provider to be added.
     */
    fun addProvider(provider: AnalyticsProvider)

    /**
     * Removes an analytics provider.
     *
     * @param key The key of the provider to be removed.
     */
    fun removeProvider(key: String)

    /**
     * Adds a global property to a specific provider or to all providers if no key is specified.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param providerKey The key of the provider (optional).
     */
    fun addGlobalProperty(key: String, value: Any, providerKey: String? = null)

    /**
     * Removes a global property from a specific provider or from all providers if no key is specified.
     *
     * @param propertyKey The key of the property to be removed.
     * @param providerKey The key of the provider (optional).
     */
    fun removeGlobalProperty(propertyKey: String, providerKey: String? = null)
}
