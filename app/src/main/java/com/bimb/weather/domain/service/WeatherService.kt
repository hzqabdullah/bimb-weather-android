package com.bimb.weather.domain.service

import com.bimb.weather.constant.Constants.APP_ID
import com.bimb.weather.constant.Constants.TEMPERATURE_UNIT
import com.bimb.weather.domain.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeatherForCity(
        @Query("q") city: String,
        @Query("units") units: String = TEMPERATURE_UNIT,
        @Query("appid") appId: String = APP_ID
    ): Response<Weather>
}
