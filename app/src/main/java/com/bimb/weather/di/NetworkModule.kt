package com.bimb.weather.di

import com.bimb.weather.constant.Constants.COUNTRY_URL
import com.bimb.weather.constant.Constants.WEATHER_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("CountryCityBaseUrl")
    fun getCountryCityRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(COUNTRY_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    @Provides
    @Singleton
    @Named("WeatherBaseUrl")
    fun getWeatherRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(WEATHER_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val networkTimeout = 60L
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
            readTimeout(networkTimeout, TimeUnit.SECONDS)
            writeTimeout(networkTimeout, TimeUnit.SECONDS)
            connectTimeout(networkTimeout, TimeUnit.SECONDS)
        }.build()
    }
}
