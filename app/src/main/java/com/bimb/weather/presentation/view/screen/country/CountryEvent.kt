package com.bimb.weather.presentation.view.screen.country

import com.bimb.weather.domain.model.Country

sealed class CountryEvent {
    data class OnSelectedCountryCity(val country: Country, val city: String) : CountryEvent()
    data object OnClickContinue : CountryEvent()
    data object OnDismissedSnackbar : CountryEvent()
    data object OnRefresh : CountryEvent()
}
