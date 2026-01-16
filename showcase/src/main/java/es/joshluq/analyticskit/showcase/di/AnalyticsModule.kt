package es.joshluq.analyticskit.showcase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.joshluq.analyticskit.sdk.AnalyticskitManager
import es.joshluq.analyticskit.showcase.ConsoleAnalyticsProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideAnalyticskitManager(): AnalyticskitManager {
        return AnalyticskitManager.Builder()
            .addProvider(ConsoleAnalyticsProvider())
            .build()
    }
}
