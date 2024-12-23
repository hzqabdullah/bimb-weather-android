package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherStatus(
    @SerializedName("main")
    val status: String,
    val icon: String
)
