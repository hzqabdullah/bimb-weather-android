package com.bimb.weather.di

import com.bimb.weather.domain.repository.CountryRepository
import com.bimb.weather.domain.repository.CountryRepositoryImpl
import com.bimb.weather.domain.repository.WeatherRepository
import com.bimb.weather.domain.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun getCountryRepository(repositoryImpl: CountryRepositoryImpl): CountryRepository

    @Binds
    abstract fun getWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository
}
