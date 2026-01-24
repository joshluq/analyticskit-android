package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.foundationkit.usecase.NoneOutput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddProviderUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = AddProviderUseCase(repository)

    @Test
    fun `when use case is invoked then add provider in repository`() =
        runTest {
            // Given
            val provider: AnalyticsProvider = mockk()
            val input = AddProviderUseCase.Input(provider)
            every { repository.addProvider(provider) } returns Unit

            // When
            val result = useCase(input)

            // Then
            verify(exactly = 1) { repository.addProvider(provider) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
