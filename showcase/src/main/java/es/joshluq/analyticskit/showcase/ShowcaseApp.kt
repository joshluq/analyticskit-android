package es.joshluq.analyticskit.showcase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import es.joshluq.analyticskit.data.provider.AnalyticsProvider
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.sdk.AnalyticskitManager
import javax.inject.Inject

/**
 * Custom Application class for the showcase app, initialized with Hilt.
 */
@HiltAndroidApp
class ShowcaseApp : Application() {

    @Inject
    lateinit var analyticskitManager: AnalyticskitManager

    override fun onCreate() {
        super.onCreate()
        
        // Registering a simple console provider for demonstration
        analyticskitManager.addProvider(ConsoleAnalyticsProvider())
        
        // Track an initial app open event
        analyticskitManager.track(AnalyticsEvent.Custom("app_open"))
    }
}

/**
 * A simple implementation of [AnalyticsProvider] that logs to the console.
 */
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
                println("Showcase - [Funnel Step] Funnel: ${event.funnelName}, Step: ${event.stepName}, Properties: ${event.properties}")
            }
        }
    }
}
