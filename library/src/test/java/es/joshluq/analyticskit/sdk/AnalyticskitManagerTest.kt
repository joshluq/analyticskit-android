package es.joshluq.analyticskit.sdk

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.usecase.AddProviderUseCase
import es.joshluq.analyticskit.domain.usecase.NoneOutput
import es.joshluq.analyticskit.domain.usecase.RemoveProviderUseCase
import es.joshluq.analyticskit.domain.usecase.TrackEventUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticskitManagerTest {
    private val trackEventUseCase: TrackEventUseCase = mockk()
    private val addProviderUseCase: AddProviderUseCase = mockk()
    private val removeProviderUseCase: RemoveProviderUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var manager: AnalyticskitManager

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        manager =
            AnalyticskitManager(
                trackEventUseCase,
                addProviderUseCase,
                removeProviderUseCase,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when track is called then invoke trackEventUseCase`() {
        // Given
        val event = AnalyticsEvent.Custom("test_event")
        val input = TrackEventUseCase.Input(event, null)
        every { trackEventUseCase(input) } returns flowOf(NoneOutput)

        // When
        manager.track(event)

        // Then
        verify(exactly = 1) { trackEventUseCase(input) }
    }

    @Test
    fun `when addProvider is called then invoke addProviderUseCase`() {
        // Given
        val provider: AnalyticsProvider = mockk()
        val input = AddProviderUseCase.Input(provider)
        every { addProviderUseCase(input) } returns flowOf(NoneOutput)

        // When
        manager.addProvider(provider)

        // Then
        verify(exactly = 1) { addProviderUseCase(input) }
    }

    @Test
    fun `when removeProvider is called then invoke removeProviderUseCase`() {
        // Given
        val key = "test_key"
        val input = RemoveProviderUseCase.Input(key)
        every { removeProviderUseCase(input) } returns flowOf(NoneOutput)

        // When
        manager.removeProvider(key)

        // Then
        verify(exactly = 1) { removeProviderUseCase(input) }
    }
}
