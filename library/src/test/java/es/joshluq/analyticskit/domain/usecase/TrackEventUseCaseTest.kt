package es.joshluq.analyticskit.domain.usecase

import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackEventUseCaseTest {
    private val repository: AnalyticsRepository = mockk()
    private val useCase = TrackEventUseCase(repository)

    @Test
    fun `when use case is invoked then track event in repository`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            val input = TrackEventUseCase.Input(event)
            coEvery { repository.track(event, null) } returns Unit

            // When
            val result = useCase(input)

            // Then
            coVerify(exactly = 1) { repository.track(event, null) }
            assertEquals(Result.success(NoneOutput), result)
        }

    @Test
    fun `when use case is invoked with provider key then track event in specific repository provider`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            val providerKey = "TEST_PROVIDER"
            val input = TrackEventUseCase.Input(event, providerKey)
            coEvery { repository.track(event, providerKey) } returns Unit

            // When
            val result = useCase(input)

            // Then
            coVerify(exactly = 1) { repository.track(event, providerKey) }
            assertEquals(Result.success(NoneOutput), result)
        }
}
