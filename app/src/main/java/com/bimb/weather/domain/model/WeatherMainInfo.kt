package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherMainInfo(
    @SerializedName("temp")
    val currentTemperature: Double,
    val humidity: Int,
    @SerializedName("sea_level")
    val pressureAtSeaLevel: Int
)
