package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.foundationkit.usecase.NoneOutput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TraceEventUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = TraceEventUseCase(repository)

    @Test
    fun `when use case is invoked then trace event in repository`() =
        runTest {
            // Given
            val eventName = "test_trace"
            val properties = mapOf("prop" to "value")
            val input = TraceEventUseCase.Input(eventName, properties)
            every { repository.traceEvent(eventName, properties) } returns Unit

            // When
            val result = useCase(input)

            // Then
            verify(exactly = 1) { repository.traceEvent(eventName, properties) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
