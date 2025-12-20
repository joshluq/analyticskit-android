package es.joshluq.analyticskit.sdk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.joshluq.analyticskit.data.datasource.AnalyticsDataSource
import es.joshluq.analyticskit.data.repository.AnalyticsRepositoryImpl
import es.joshluq.analyticskit.domain.repository.AnalyticsRepository
import es.joshluq.analyticskit.domain.usecase.AddProviderUseCase
import es.joshluq.analyticskit.domain.usecase.RemoveProviderUseCase
import es.joshluq.analyticskit.domain.usecase.TrackEventUseCase
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies related to the Analyticskit library.
 */
@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideAnalyticsDataSource(): AnalyticsDataSource = AnalyticsDataSource()

    @Provides
    @Singleton
    fun provideAnalyticsRepository(
        dataSource: AnalyticsDataSource
    ): AnalyticsRepository = AnalyticsRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideTrackEventUseCase(
        repository: AnalyticsRepository
    ): TrackEventUseCase = TrackEventUseCase(repository)

    @Provides
    @Singleton
    fun provideAddProviderUseCase(
        repository: AnalyticsRepository
    ): AddProviderUseCase = AddProviderUseCase(repository)

    @Provides
    @Singleton
    fun provideRemoveProviderUseCase(
        repository: AnalyticsRepository
    ): RemoveProviderUseCase = RemoveProviderUseCase(repository)
}
