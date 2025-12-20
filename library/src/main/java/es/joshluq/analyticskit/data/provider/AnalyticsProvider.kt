package es.joshluq.analyticskit.data.provider

import es.joshluq.analyticskit.domain.model.AnalyticsEvent

/**
 * Interface for analytics providers.
 * Implement this interface to route analytics data to specific services (e.g., Firebase, Mixpanel).
 */
interface AnalyticsProvider {
    /**
     * Unique identifier for the provider.
     */
    val key: String

    /**
     * Tracks an analytics event.
     * @param event The event to be tracked.
     */
    suspend fun track(event: AnalyticsEvent)
}
