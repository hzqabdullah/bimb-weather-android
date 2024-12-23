package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class WindStatus(
    @SerializedName("speed")
    val windSpeed: Double
)
