package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for tracking a traced event with all accumulated properties.
 *
 * @property repository The repository to handle the trace management.
 */
internal class TrackTracedEventUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<TrackTracedEventUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [TrackTracedEventUseCase].
     *
     * @property eventName The key identifying the trace.
     * @property providerKey The key of the specific provider (optional).
     */
    data class Input(
        val eventName: String,
        val providerKey: String? = null,
    ) : UseCaseInput

    /**
     * Executes the tracking of a traced event.
     *
     * @param input The input containing the trace details.
     * @return A [Result] containing [NoneOutput] once the event has been tracked.
     */
    override suspend fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.trackTracedEvent(input.eventName, input.providerKey)
            NoneOutput
        }
}
