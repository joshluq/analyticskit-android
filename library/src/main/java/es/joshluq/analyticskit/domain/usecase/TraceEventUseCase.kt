package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for accumulating properties for a specific event trace.
 *
 * @property repository The repository to handle the trace management.
 */
internal class TraceEventUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<TraceEventUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [TraceEventUseCase].
     *
     * @property eventName The key identifying the trace.
     * @property properties The properties to add to the trace.
     */
    data class Input(
        val eventName: String,
        val properties: Map<String, Any>,
    ) : UseCaseInput

    /**
     * Executes the trace accumulation.
     *
     * @param input The input containing the trace details.
     * @return A [Result] containing [NoneOutput] once the properties have been added to the trace.
     */
    override suspend fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.traceEvent(input.eventName, input.properties)
            NoneOutput
        }
}
