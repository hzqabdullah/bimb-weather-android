package com.bimb.weather.di

import com.bimb.weather.domain.service.CountryService
import com.bimb.weather.domain.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun countryService(
        @Named("CountryCityBaseUrl") retrofit: Retrofit
    ): CountryService {
        return retrofit.create(CountryService::class.java)
    }

    @Provides
    @Singleton
    fun weatherService(
        @Named("WeatherBaseUrl") retrofit: Retrofit
    ): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}
