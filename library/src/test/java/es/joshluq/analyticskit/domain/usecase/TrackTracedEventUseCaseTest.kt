package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.foundationkit.usecase.NoneOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackTracedEventUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = TrackTracedEventUseCase(repository)

    @Test
    fun `when use case is invoked then track traced event in repository`() =
        runTest {
            // Given
            val eventName = "test_event"
            val input = TrackTracedEventUseCase.Input(eventName)
            coEvery { repository.trackTracedEvent(eventName, null) } returns Unit

            // When
            val result = useCase(input)

            // Then
            coVerify(exactly = 1) { repository.trackTracedEvent(eventName, null) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
