package net.marwanaziz.cityscout.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import net.marwanaziz.cityscout.KeyProvider
import net.marwanaziz.cityscoutshared.CityScoutFactory
import net.marwanaziz.cityscoutshared.Remote
import net.marwanaziz.cityscoutshared.RemoteKeys
import net.marwanaziz.cityscoutshared.SearchCityViewModel

@Module
@InstallIn(SingletonComponent::class)
object CityScoutModule {
    @Provides
    @Named("rapidApiKey")
    fun provideRapidApiKey(): String = KeyProvider.rapidApiKey

    @Provides
    @Named("weatherApiKey")
    fun provideWeatherApiKey(): String = KeyProvider.weatherApiKey

    @Provides
    @Singleton
    fun provideRemote(
        @Named("rapidApiKey") rapidApiKey: String,
        @Named("weatherApiKey") weatherApiKey: String
    ): Remote {
        RemoteKeys.rapidApiKey = rapidApiKey
        RemoteKeys.weatherApiKey = weatherApiKey
        return CityScoutFactory.createRemote(RemoteKeys)
    }

    @Provides
    @Singleton
    fun provideSearchCityViewModel(remote: Remote): SearchCityViewModel =
        CityScoutFactory.creatSearchCityViewModel(remote)
}
