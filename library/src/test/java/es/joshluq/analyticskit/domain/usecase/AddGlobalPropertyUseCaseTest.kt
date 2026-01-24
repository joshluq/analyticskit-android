package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.foundationkit.usecase.NoneOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddGlobalPropertyUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = AddGlobalPropertyUseCase(repository)

    @Test
    fun `when use case is invoked then add global property in repository`() =
        runTest {
            // Given
            val key = "test_key"
            val value = "test_value"
            val input = AddGlobalPropertyUseCase.Input(key, value)
            coEvery { repository.addGlobalProperty(key, value, null) } returns Unit

            // When
            val result = useCase(input)

            // Then
            coVerify(exactly = 1) { repository.addGlobalProperty(key, value, null) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
