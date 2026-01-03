package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for removing an analytics provider.
 *
 * @property repository The repository to handle the provider management.
 */
internal class RemoveProviderUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<RemoveProviderUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [RemoveProviderUseCase].
     *
     * @property key The unique identifier of the provider to be removed.
     */
    data class Input(
        val key: String,
    ) : UseCaseInput

    /**
     * Executes the removal of a provider.
     *
     * @param input The input containing the key of the provider to be removed.
     * @return A [Result] containing [NoneOutput] once the provider has been removed.
     */
    override fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.removeProvider(input.key)
            NoneOutput
        }
}
