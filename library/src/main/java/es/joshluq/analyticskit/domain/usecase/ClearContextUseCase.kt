package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for clearing all event traces.
 *
 * @property repository The repository to handle the trace management.
 */
internal class ClearContextUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<NoneInput, NoneOutput> {

    /**
     * Executes the clearing of all event traces.
     *
     * @param input The empty input.
     * @return A [Result] containing [NoneOutput] once the traces have been cleared.
     */
    override suspend fun invoke(input: NoneInput): Result<NoneOutput> =
        runCatching {
            // Note: Since we shifted from 'Context' to 'Trace', 
            // we should probably clear all traces here if that's the intention,
            // or we might need a specific 'ClearTracesUseCase'.
            // For now, I'll assume we want to clear everything accumulated in traces.
            // As there isn't a clearAllTraces in Repository yet, I will add it or use clearTrace if we had a key.
            // But since the requirement was about 'Context', and we moved to 'Traces',
            // I will update the repository to support clearAllTraces.
            repository.clearAllTraces()
            NoneOutput
        }
}
