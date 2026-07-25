package es.joshluq.analyticskit.sdk

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.domain.usecase.*
import es.joshluq.foundationkit.coroutines.DispatcherProvider
import es.joshluq.foundationkit.usecase.NoneOutput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val addGlobalPropertyUseCase: AddGlobalPropertyUseCase = mockk()
    private val removeGlobalPropertyUseCase: RemoveGlobalPropertyUseCase = mockk()
    private val traceEventUseCase: TraceEventUseCase = mockk()
    private val trackTracedEventUseCase: TrackTracedEventUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
        override val unconfined: CoroutineDispatcher = testDispatcher
    }

    private lateinit var manager: AnalyticskitManager

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // Reflection is used to instantiate the private constructor for testing.
        // We find the primary constructor by parameter count to avoid issues with synthetic bridge constructors.
        val constructor = AnalyticskitManager::class.java.declaredConstructors.first { it.parameterCount == 8 }
        constructor.isAccessible = true
        manager = constructor.newInstance(
            trackEventUseCase,
            addProviderUseCase,
            removeProviderUseCase,
            addGlobalPropertyUseCase,
            removeGlobalPropertyUseCase,
            traceEventUseCase,
            trackTracedEventUseCase,
            dispatcherProvider
        ) as AnalyticskitManager
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
        coEvery { trackEventUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.track(event)

        // Then
        coVerify(exactly = 1) { trackEventUseCase(input) }
    }

    @Test
    fun `when addProvider is called then invoke addProviderUseCase`() {
        // Given
        val provider: AnalyticsProvider = mockk()
        val input = AddProviderUseCase.Input(provider)
        coEvery { addProviderUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.addProvider(provider)

        // Then
        coVerify(exactly = 1) { addProviderUseCase(input) }
    }

    @Test
    fun `when removeProvider is called then invoke removeProviderUseCase`() {
        // Given
        val key = "test_key"
        val input = RemoveProviderUseCase.Input(key)
        coEvery { removeProviderUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.removeProvider(key)

        // Then
        coVerify(exactly = 1) { removeProviderUseCase(input) }
    }

    @Test
    fun `when addGlobalProperty is called then invoke addGlobalPropertyUseCase`() {
        // Given
        val key = "global_key"
        val value = "global_value"
        val input = AddGlobalPropertyUseCase.Input(key, value, null)
        coEvery { addGlobalPropertyUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.addGlobalProperty(key, value)

        // Then
        coVerify(exactly = 1) { addGlobalPropertyUseCase(input) }
    }

    @Test
    fun `when traceEvent is called then invoke traceEventUseCase`() {
        // Given
        val eventName = "trace_event"
        val properties = mapOf("key" to "value")
        val input = TraceEventUseCase.Input(eventName, properties)
        coEvery { traceEventUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.traceEvent(eventName, properties)

        // Then
        coVerify(exactly = 1) { traceEventUseCase(input) }
    }

    @Test
    fun `when trackTracedEvent is called then invoke trackTracedEventUseCase`() {
        // Given
        val eventName = "trace_event"
        val input = TrackTracedEventUseCase.Input(eventName, null)
        coEvery { trackTracedEventUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.trackTracedEvent(eventName)

        // Then
        coVerify(exactly = 1) { trackTracedEventUseCase(input) }
    }

    @Test
    fun `when removeGlobalProperty is called then invoke removeGlobalPropertyUseCase`() {
        // Given
        val propertyKey = "global_key"
        val input = RemoveGlobalPropertyUseCase.Input(propertyKey, null)
        coEvery { removeGlobalPropertyUseCase(input) } returns Result.success(NoneOutput)

        // When
        manager.removeGlobalProperty(propertyKey)

        // Then
        coVerify(exactly = 1) { removeGlobalPropertyUseCase(input) }
    }
}
