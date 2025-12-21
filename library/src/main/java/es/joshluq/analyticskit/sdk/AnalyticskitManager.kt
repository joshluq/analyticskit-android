package es.joshluq.analyticskit.sdk

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.usecase.AddProviderUseCase
import es.joshluq.analyticskit.domain.usecase.RemoveProviderUseCase
import es.joshluq.analyticskit.domain.usecase.TrackEventUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Main entry point for the Analyticskit library. This manager coordinates event tracking and
 * provider management using UseCases.
 */
@Singleton
class AnalyticskitManager @Inject constructor(
    private val trackEventUseCase: TrackEventUseCase,
    private val addProviderUseCase: AddProviderUseCase,
    private val removeProviderUseCase: RemoveProviderUseCase,
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
        trackEventUseCase(TrackEventUseCase.Input(event, providerKey)).launchIn(scope)
    }

    /**
     * Adds a new analytics provider.
     *
     * @param provider The provider to be added.
     */
    fun addProvider(provider: AnalyticsProvider) {
        addProviderUseCase(AddProviderUseCase.Input(provider)).launchIn(scope)
    }

    /**
     * Removes an analytics provider.
     *
     * @param key The key of the provider to be removed.
     */
    fun removeProvider(key: String) {
        removeProviderUseCase(RemoveProviderUseCase.Input(key)).launchIn(scope)
    }
}
