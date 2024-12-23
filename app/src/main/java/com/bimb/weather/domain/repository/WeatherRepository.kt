package com.bimb.weather.domain.repository

import com.bimb.weather.domain.model.Weather
import com.bimb.weather.domain.service.WeatherService
import com.bimb.weather.utils.ResultWrapper
import com.bimb.weather.utils.networkRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface WeatherRepository {
    fun getWeatherForCity(city: String): Flow<ResultWrapper<Weather>>
}

class WeatherRepositoryImpl @Inject constructor(
    private val service: WeatherService
) : WeatherRepository {
    override fun getWeatherForCity(city: String): Flow<ResultWrapper<Weather>> = flow {
        emit(ResultWrapper.Loading())
        delay(1000L)
        val result = networkRequest {
            service.getWeatherForCity(city = city)
        }
        when (result) {
            is ResultWrapper.Error -> emit(ResultWrapper.Error(result.message!!))
            is ResultWrapper.Success -> emit(ResultWrapper.Success(result.data!!))
            is ResultWrapper.NetworkFailed -> emit(ResultWrapper.NetworkFailed(result.message!!))
            is ResultWrapper.Loading -> Unit
        }
    }
}
