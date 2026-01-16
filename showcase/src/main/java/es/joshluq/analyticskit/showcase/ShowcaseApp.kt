package es.joshluq.analyticskit.showcase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import es.joshluq.analyticskit.domain.model.AnalyticsEvent
import es.joshluq.analyticskit.sdk.AnalyticskitManager
import javax.inject.Inject

/** Custom Application class for the showcase app, initialized with Hilt. */
@HiltAndroidApp
class ShowcaseApp : Application() {
    @Inject lateinit var analyticskitManager: AnalyticskitManager

    override fun onCreate() {
        super.onCreate()

        // Track an initial app open event
        analyticskitManager.track(AnalyticsEvent.Custom("app_open"))
    }
}
