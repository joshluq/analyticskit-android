package es.joshluq.analyticskit.showcase

import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent

/** A simple implementation of [AnalyticsProvider] that logs to the console. */
class ConsoleAnalyticsProvider : AnalyticsProvider {
    override val key: String = "CONSOLE_PROVIDER"

    override suspend fun track(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.Custom -> {
                println("Showcase - [Custom Event] Name: ${event.name}, Properties: ${event.properties}")
            }
            is AnalyticsEvent.ScreenView -> {
                println("Showcase - [Screen View] Screen: ${event.screenName}, Class: ${event.screenClass}")
            }
            is AnalyticsEvent.FunnelStep -> {
                println(
                    "Showcase - [Funnel Step] Funnel: ${event.funnelName}, Step: ${event.stepName}, Properties: ${event.properties}",
                )
            }
        }
    }
}
