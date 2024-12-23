package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("name")
    val cityName: String,
    @SerializedName("coord")
    val coordinate: Coordinate,
    @SerializedName("main")
    val weatherMainInfo: WeatherMainInfo,
    @SerializedName("weather")
    val weatherStatus: List<WeatherStatus>,
    @SerializedName("wind")
    val windStatus: WindStatus,
    @SerializedName("timezone")
    val timeZone: Long,
    @SerializedName("sys")
    val weatherSystemInfo: WeatherSystemInfo,
)
