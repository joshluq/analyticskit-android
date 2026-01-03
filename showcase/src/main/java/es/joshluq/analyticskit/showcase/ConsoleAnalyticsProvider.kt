package es.joshluq.analyticskit.showcase

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import java.util.concurrent.ConcurrentHashMap

/** A simple implementation of [AnalyticsProvider] that logs to the console. */
class ConsoleAnalyticsProvider : AnalyticsProvider {
    override val key: String = "CONSOLE_PROVIDER"
    private val globalProperties = ConcurrentHashMap<String, Any>()

    override suspend fun track(event: AnalyticsEvent) {
        val eventProperties = when (event) {
            is AnalyticsEvent.Custom -> event.properties
            is AnalyticsEvent.FunnelStep -> event.properties
            else -> emptyMap()
        }

        val allProperties = globalProperties + eventProperties

        when (event) {
            is AnalyticsEvent.Custom -> {
                println("Showcase - [Custom Event] Name: ${event.name}, Properties: $allProperties")
            }
            is AnalyticsEvent.ScreenView -> {
                println("Showcase - [Screen View] Screen: ${event.screenName}, Class: ${event.screenClass}, Globals: $globalProperties")
            }
            is AnalyticsEvent.FunnelStep -> {
                println(
                    "Showcase - [Funnel Step] Funnel: ${event.funnelName}, Step: ${event.stepName}, Properties: $allProperties",
                )
            }
        }
    }

    override fun addGlobalProperty(key: String, value: Any) {
        globalProperties[key] = value
    }

    override fun removeGlobalProperty(key: String) {
        globalProperties.remove(key)
    }
}
