package com.bimb.weather.presentation.view.screen.weather

import com.bimb.weather.domain.model.Country

sealed class WeatherEvent {
    data class OnSelectedCountryCity(val country: Country, val city: String) : WeatherEvent()
    data class OnClickSearchCity(val city: String) : WeatherEvent()
    data object OnDismissedSnackbar : WeatherEvent()
    data object OnRefresh : WeatherEvent()
}
