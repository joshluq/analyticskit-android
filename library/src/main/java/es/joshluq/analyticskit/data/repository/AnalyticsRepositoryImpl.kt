package es.joshluq.analyticskit.data.repository

import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the [AnalyticsRepository] interface.
 * Coordinates event tracking through the [AnalyticsDataSource].
 * @property dataSource The data source for managing analytics providers.
 */
@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    private val dataSource: AnalyticsDataSource,
) : AnalyticsRepository {
    /**
     * Tracks an analytics event by dispatching it to the data source.
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
     * @param provider The provider to be added.
     */
    override fun addProvider(provider: AnalyticsProvider) {
        dataSource.addProvider(provider)
    }

    /**
     * Removes an analytics provider.
     * @param key The key of the provider to be removed.
     */
    override fun removeProvider(key: String) {
        dataSource.removeProvider(key)
    }
}
