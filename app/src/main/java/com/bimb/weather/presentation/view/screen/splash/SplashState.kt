package com.bimb.weather.presentation.view.screen.splash

import com.bimb.weather.domain.model.CountriesData

data class SplashState(
    val countriesData: CountriesData? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val networkIssue: String? = null,
    val isUserAlreadyLoggedIn: Boolean? = null
)
