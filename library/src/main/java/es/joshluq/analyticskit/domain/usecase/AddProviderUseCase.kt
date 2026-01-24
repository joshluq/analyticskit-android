package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.foundationkit.usecase.NoneOutput
import es.joshluq.foundationkit.usecase.UseCase
import es.joshluq.foundationkit.usecase.UseCaseInput

/**
 * Use case for adding an analytics provider.
 *
 * @property repository The repository to handle the provider management.
 */
internal class AddProviderUseCase(
    private val repository: AnalyticsRepository,
) : UseCase<AddProviderUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [AddProviderUseCase].
     *
     * @property provider The analytics provider to be added.
     */
    data class Input(
        val provider: AnalyticsProvider,
    ) : UseCaseInput

    /**
     * Executes the addition of a provider.
     *
     * @param input The input containing the provider to be added.
     * @return A [Result] containing [NoneOutput] once the provider has been added.
     */
    override suspend fun invoke(input: Input): Result<NoneOutput> =
        runCatching {
            repository.addProvider(input.provider)
            NoneOutput
        }
}
