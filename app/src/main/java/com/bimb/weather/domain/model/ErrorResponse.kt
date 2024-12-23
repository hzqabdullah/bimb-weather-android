package com.bimb.weather.domain.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("cod")
    val errorCode: String? = null,
    val message: String? = null
)
