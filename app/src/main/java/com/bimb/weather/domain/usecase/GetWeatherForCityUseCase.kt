package com.bimb.weather.domain.usecase

import com.bimb.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherForCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    fun invoke(city: String) = repository.getWeatherForCity(city)
}
