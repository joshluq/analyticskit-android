package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case for removing an analytics provider.
 * @property repository The repository to handle the provider management.
 */
class RemoveProviderUseCase @Inject constructor(
    private val repository: AnalyticsRepository,
) : UseCase<RemoveProviderUseCase.Input, NoneOutput> {
    /**
     * Input parameters for the [RemoveProviderUseCase].
     * @property key The unique identifier of the provider to be removed.
     */
    data class Input(
        val key: String,
    ) : UseCaseInput

    /**
     * Executes the removal of a provider.
     * @param input The input containing the key of the provider to be removed.
     * @return A [Flow] emitting [NoneOutput] once the provider has been removed.
     */
    override fun invoke(input: Input): Flow<NoneOutput> =
        flow {
            repository.removeProvider(input.key)
            emit(NoneOutput)
        }
}
