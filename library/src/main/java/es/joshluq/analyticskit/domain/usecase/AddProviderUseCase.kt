package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for adding an analytics provider.
 * @property repository The repository to handle the provider management.
 */
class AddProviderUseCase @Inject constructor(
    private val repository: AnalyticsRepository,
) : UseCase<AddProviderUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [AddProviderUseCase].
     * @property provider The analytics provider to be added.
     */
    data class Input(
        val provider: AnalyticsProvider,
    ) : UseCaseInput

    /**
     * Executes the addition of a provider.
     * @param input The input containing the provider to be added.
     * @return A [Flow] emitting [NoneOutput] once the provider has been added.
     */
    override fun invoke(input: Input): Flow<NoneOutput> =
        flow {
            repository.addProvider(input.provider)
            emit(NoneOutput)
        }
}
