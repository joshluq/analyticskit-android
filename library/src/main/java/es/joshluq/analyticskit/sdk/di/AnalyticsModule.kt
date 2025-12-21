package es.joshluq.analyticskit.sdk.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.joshluq.analyticskit.data.repository.AnalyticsRepositoryImpl
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository

/** Hilt module for providing dependencies related to the Analyticskit library. */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindAnalyticsRepository(analyticsRepositoryImpl: AnalyticsRepositoryImpl): AnalyticsRepository
}
