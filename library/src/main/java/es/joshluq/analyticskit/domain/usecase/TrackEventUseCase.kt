package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Use case for tracking an analytics event.
 *
 * @property repository The repository to handle the event tracking.
 */
class TrackEventUseCase
    @Inject
    constructor(
        private val repository: AnalyticsRepository,
    ) : UseCase<TrackEventUseCase.Input, NoneOutput> {
        /**
         * Input parameters for the [TrackEventUseCase].
         *
         * @property event The analytics event to be tracked.
         * @property providerKey The key of the provider to send the event to (optional).
         */
        data class Input(
            val event: AnalyticsEvent,
            val providerKey: String? = null,
        ) : UseCaseInput

        /**
         * Executes the tracking of an event.
         *
         * @param input The input containing the event to be tracked and the optional provider key.
         * @return A [Flow] emitting [NoneOutput] once the event has been passed to the repository.
         */
        override fun invoke(input: Input): Flow<NoneOutput> =
            flow {
                repository.track(input.event, input.providerKey)
                emit(NoneOutput)
            }.flowOn(Dispatchers.IO)
    }
