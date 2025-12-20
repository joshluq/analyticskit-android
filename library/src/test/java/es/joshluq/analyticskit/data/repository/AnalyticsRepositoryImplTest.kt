package es.joshluq.analyticskit.data.repository

import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AnalyticsRepositoryImplTest {
    private val dataSource: AnalyticsDataSource = mockk()
    private val repository = AnalyticsRepositoryImpl(dataSource)

    @Test
    fun `when track is called then send event to data source`() =
        runTest {
            // Given
            val event = AnalyticsEvent.Custom("test_event")
            coEvery { dataSource.sendEvent(event, null) } returns Unit

            // When
            repository.track(event, null)

            // Then
            coVerify(exactly = 1) { dataSource.sendEvent(event, null) }
        }

    @Test
    fun `when addProvider is called then add provider to data source`() {
        // Given
        val provider: AnalyticsProvider = mockk()
        coEvery { dataSource.addProvider(provider) } returns Unit

        // When
        repository.addProvider(provider)

        // Then
        coVerify(exactly = 1) { dataSource.addProvider(provider) }
    }

    @Test
    fun `when removeProvider is called then remove provider from data source`() {
        // Given
        val key = "TEST_KEY"
        coEvery { dataSource.removeProvider(key) } returns Unit

        // When
        repository.removeProvider(key)

        // Then
        coVerify(exactly = 1) { dataSource.removeProvider(key) }
    }
}
