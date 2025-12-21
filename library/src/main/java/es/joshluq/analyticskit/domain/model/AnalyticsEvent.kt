package es.joshluq.analyticskit.domain.model

/** Sealed class representing the different types of analytics events that can be tracked. */
sealed class AnalyticsEvent {
    /**
     * Represents a custom event with a specific name and optional properties.
     *
     * @property name The name of the event.
     * @property properties A map of metadata associated with the event.
     */
    data class Custom(
        val name: String,
        val properties: Map<String, Any> = emptyMap(),
    ) : AnalyticsEvent()

    /**
     * Represents a screen navigation event.
     *
     * @property screenName The name of the screen being visited.
     * @property screenClass The class name of the screen (optional).
     */
    data class ScreenView(
        val screenName: String,
        val screenClass: String? = null,
    ) : AnalyticsEvent()

    /**
     * Represents a conversion funnel step.
     *
     * @property funnelName The name of the funnel.
     * @property stepName The name of the step within the funnel.
     * @property properties Optional metadata for the step.
     */
    data class FunnelStep(
        val funnelName: String,
        val stepName: String,
        val properties: Map<String, Any> = emptyMap(),
    ) : AnalyticsEvent()
}
