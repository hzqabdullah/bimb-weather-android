package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class CountriesData(
    @SerializedName("data")
    val countries: List<Country>
)
