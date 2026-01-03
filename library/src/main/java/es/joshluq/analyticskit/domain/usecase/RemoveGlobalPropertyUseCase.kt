package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for removing a global property from analytics providers.
 *
 * @property repository The repository to handle the property management.
 */
internal class RemoveGlobalPropertyUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<RemoveGlobalPropertyUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [RemoveGlobalPropertyUseCase].
     *
     * @property propertyKey The key of the property to be removed.
     * @property providerKey The key of the specific provider (optional).
     */
    data class Input(
        val propertyKey: String,
        val providerKey: String? = null,
    ) : UseCaseInput

    /**
     * Executes the removal of a global property.
     *
     * @param input The input containing the property key.
     * @return A [Result] containing [NoneOutput] once the property has been removed.
     */
    override fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.removeGlobalProperty(input.propertyKey, input.providerKey)
            NoneOutput
        }
}
