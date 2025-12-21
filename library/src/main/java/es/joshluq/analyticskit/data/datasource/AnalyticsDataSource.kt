package es.joshluq.analyticskit.data.datasource

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton

/** Data source for managing and dispatching events to multiple analytics providers. */
@Singleton
class AnalyticsDataSource @Inject constructor() {
    private val providers = CopyOnWriteArrayList<AnalyticsProvider>()

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
        getProviders(providerKey).forEach { it.track(event) }
    }

    /**
     * Filters the providers based on the key. If a key is provided, only the matching provider is
     * returned. If no key is provided, all providers are returned (default behavior).
     */
    private fun getProviders(key: String?): List<AnalyticsProvider> =
        if (key != null) {
            providers.filter { it.key == key }
        } else {
            providers
        }
}
