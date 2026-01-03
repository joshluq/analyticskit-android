package es.joshluq.analyticskit.sdk

import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.data.repository.AnalyticsRepositoryImpl
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.usecase.AddProviderUseCase
import es.joshluq.analyticskit.domain.usecase.RemoveProviderUseCase
import es.joshluq.analyticskit.domain.usecase.TrackEventUseCase

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
) {
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
        trackEventUseCase(TrackEventUseCase.Input(event, providerKey))
    }

    /**
     * Adds a new analytics provider.
     *
     * @param provider The provider to be added.
     */
    fun addProvider(provider: AnalyticsProvider) {
        addProviderUseCase(AddProviderUseCase.Input(provider))
    }

    /**
     * Removes an analytics provider.
     *
     * @param key The key of the provider to be removed.
     */
    fun removeProvider(key: String) {
        removeProviderUseCase(RemoveProviderUseCase.Input(key))
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
            )
        }
    }
}
