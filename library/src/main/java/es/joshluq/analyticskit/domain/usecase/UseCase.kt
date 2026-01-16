package es.joshluq.analyticskit.domain.usecase

/** Base interface for all use case inputs. */
internal interface UseCaseInput

/** Base interface for all use case outputs. */
internal interface UseCaseOutput

/**
 * Represents a generic Use Case in Clean Architecture.
 *
 * @param I Input type that must implement [UseCaseInput].
 * @param O Output type that must implement [UseCaseOutput].
 */
internal interface UseCase<in I : UseCaseInput, out O : UseCaseOutput> {
    /**
     * Executes the business logic of the use case.
     *
     * @param input The parameters required for the operation.
     * @return A [Result] containing the output of the operation.
     */
    suspend operator fun invoke(input: I): Result<O>
}

/** Represents a standard empty output for Use Cases that do not return a specific value. */
internal object NoneOutput : UseCaseOutput

/** Empty input for Use Cases that do not require parameters. */
internal object NoneInput : UseCaseInput
