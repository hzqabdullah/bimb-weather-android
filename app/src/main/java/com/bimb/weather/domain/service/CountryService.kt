package com.bimb.weather.domain.service

import com.bimb.weather.domain.model.CountriesData
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
    @GET("countries")
    suspend fun getCountriesAndCities(): Response<CountriesData>
}
