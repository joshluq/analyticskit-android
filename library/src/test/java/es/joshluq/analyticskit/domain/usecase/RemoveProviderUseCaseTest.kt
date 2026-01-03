package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoveProviderUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = RemoveProviderUseCase(repository)

    @Test
    fun `when use case is invoked then remove provider in repository`() =
        runTest {
            // Given
            val key = "test_provider_key"
            val input = RemoveProviderUseCase.Input(key)
            every { repository.removeProvider(key) } returns Unit

            // When
            val result = useCase(input)

            // Then
            verify(exactly = 1) { repository.removeProvider(key) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
