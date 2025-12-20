package es.joshluq.analyticskit.data.datasource

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AnalyticsDataSourceTest {
    private lateinit var dataSource: AnalyticsDataSource
    private val provider1: AnalyticsProvider = mockk()
    private val provider2: AnalyticsProvider = mockk()

    @Before
    fun setUp() {
        dataSource = AnalyticsDataSource()
        coEvery { provider1.key } returns "KEY_1"
        coEvery { provider2.key } returns "KEY_2"
        coEvery { provider1.track(any()) } returns Unit
        coEvery { provider2.track(any()) } returns Unit
    }

    @Test
    fun `when sendEvent is called then all registered providers track the event`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            dataSource.addProvider(provider1)
            dataSource.addProvider(provider2)

            // When
            dataSource.sendEvent(event)

            // Then
            coVerify(exactly = 1) { provider1.track(event) }
            coVerify(exactly = 1) { provider2.track(event) }
        }

    @Test
    fun `when sendEvent is called with key then only matching provider tracks the event`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            dataSource.addProvider(provider1)
            dataSource.addProvider(provider2)

            // When
            dataSource.sendEvent(event, "KEY_1")

            // Then
            coVerify(exactly = 1) { provider1.track(event) }
            coVerify(exactly = 0) { provider2.track(event) }
        }

    @Test
    fun `when provider is removed then it no longer tracks events`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            dataSource.addProvider(provider1)
            dataSource.removeProvider("KEY_1")

            // When
            dataSource.sendEvent(event)

            // Then
            coVerify(exactly = 0) { provider1.track(event) }
        }

    @Test
    fun `when adding provider with same key then it is not duplicated`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            dataSource.addProvider(provider1)
            dataSource.addProvider(provider1)

            // When
            dataSource.sendEvent(event)

            // Then
            coVerify(exactly = 1) { provider1.track(event) }
        }
}
