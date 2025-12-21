package es.joshluq.analyticskit.domain.repository

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent

/** Interface representing the repository for analytics operations. */
interface AnalyticsRepository {
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
}
