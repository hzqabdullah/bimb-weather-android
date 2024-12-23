package com.bimb.weather.presentation.view.screen.country

import com.bimb.weather.domain.model.CountriesData
import com.bimb.weather.domain.model.Country

data class CountryState(
    val countriesData: CountriesData? = null,
    val selectedCountry: Country? = null,
    val selectedCity: String? = null,
    val error: String? = null,
    val isSelectedCity: Boolean = false,
    val networkIssue: String? = null,
    val isLoading: Boolean = false
)
