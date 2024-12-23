package com.bimb.weather.domain.model

data class Country(
    val country: String,
    val iso2: String,
    val cities: List<String>
)
