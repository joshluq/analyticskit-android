package es.joshluq.analyticskit.data.repository

import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Implementation of the [AnalyticsRepository] interface. Coordinates event tracking through the
 * [AnalyticsDataSource].
 *
 * @property dataSource The data source for managing analytics providers.
 */
internal class AnalyticsRepositoryImpl(
    private val dataSource: AnalyticsDataSource,
) : AnalyticsRepository {
    /**
     * Tracks an analytics event by dispatching it to the data source.
     *
     * @param event The event to be tracked.
     * @param providerKey The key of the provider to send the event to (optional).
     */
    override suspend fun track(
        event: AnalyticsEvent,
        providerKey: String?,
    ) {
        dataSource.sendEvent(event, providerKey)
    }

    /**
     * Adds an analytics provider.
     *
     * @param provider The provider to be added.
     */
    override fun addProvider(provider: AnalyticsProvider) {
        dataSource.addProvider(provider)
    }

    /**
     * Removes an analytics provider.
     *
     * @param key The key of the provider to be removed.
     */
    override fun removeProvider(key: String) {
        dataSource.removeProvider(key)
    }

    /**
     * Adds a global property to a specific provider or to all providers if no key is specified.
     *
     * @param key The key of the property.
     * @param value The value of the property.
     * @param providerKey The key of the provider (optional).
     */
    override fun addGlobalProperty(key: String, value: Any, providerKey: String?) {
        dataSource.addGlobalProperty(key, value, providerKey)
    }

    /**
     * Removes a global property from a specific provider or from all providers if no key is specified.
     *
     * @param propertyKey The key of the property to be removed.
     * @param providerKey The key of the provider (optional).
     */
    override fun removeGlobalProperty(propertyKey: String, providerKey: String?) {
        dataSource.removeGlobalProperty(propertyKey, providerKey)
    }
}
