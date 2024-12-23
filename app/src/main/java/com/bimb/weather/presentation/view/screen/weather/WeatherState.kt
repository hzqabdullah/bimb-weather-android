package com.bimb.weather.presentation.view.screen.weather

import com.bimb.weather.domain.model.CountriesData
import com.bimb.weather.domain.model.Country
import com.bimb.weather.domain.model.Weather

data class WeatherState(
    val countriesData: CountriesData? = null,
    val weather: Weather? = null,
    val searchCity: String? = null,
    val searchCountry: String? = null,
    val selectedCountry: Country? = null,
    val selectedCity: String? = null,
    val error: String? = null,
    val networkIssue: String? = null,
    val isLoading: Boolean = false
)
