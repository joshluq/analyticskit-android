package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/**
 * Use case for adding a global property to analytics providers.
 *
 * @property repository The repository to handle the property management.
 */
internal class AddGlobalPropertyUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<AddGlobalPropertyUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [AddGlobalPropertyUseCase].
     *
     * @property key The key of the property.
     * @property value The value of the property.
     * @property providerKey The key of the specific provider (optional).
     */
    data class Input(
        val key: String,
        val value: Any,
        val providerKey: String? = null,
    ) : UseCaseInput

    /**
     * Executes the addition of a global property.
     *
     * @param input The input containing the property details.
     * @return A [Result] containing [NoneOutput] once the property has been added.
     */
    override fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.addGlobalProperty(input.key, input.value, input.providerKey)
            NoneOutput
        }
}
