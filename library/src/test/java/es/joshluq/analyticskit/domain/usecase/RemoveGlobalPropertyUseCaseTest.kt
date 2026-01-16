package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoveGlobalPropertyUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = RemoveGlobalPropertyUseCase(repository)

    @Test
    fun `when use case is invoked then remove global property in repository`() =
        runTest {
            // Given
            val propertyKey = "test_key"
            val input = RemoveGlobalPropertyUseCase.Input(propertyKey)
            coEvery { repository.removeGlobalProperty(propertyKey, null) } returns Unit

            // When
            val result = useCase(input)

            // Then
            coVerify(exactly = 1) { repository.removeGlobalProperty(propertyKey, null) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
