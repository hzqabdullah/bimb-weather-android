package com.bimb.weather.domain.repository

import com.bimb.weather.domain.model.CountriesData
import com.bimb.weather.domain.service.CountryService
import com.bimb.weather.utils.ResultWrapper
import com.bimb.weather.utils.networkRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface CountryRepository {
    fun getCountriesAndCities(): Flow<ResultWrapper<CountriesData>>
}

class CountryRepositoryImpl @Inject constructor(
    private val service: CountryService
) : CountryRepository {
    override fun getCountriesAndCities(): Flow<ResultWrapper<CountriesData>> = flow {
        emit(ResultWrapper.Loading())
        delay(1000L)
        val result = networkRequest {
            service.getCountriesAndCities()
        }
        when (result) {
            is ResultWrapper.Error -> emit(ResultWrapper.Error(result.message!!))
            is ResultWrapper.Success -> emit(ResultWrapper.Success(result.data!!))
            is ResultWrapper.NetworkFailed -> emit(ResultWrapper.NetworkFailed(result.message!!))
            is ResultWrapper.Loading -> Unit
        }
    }
}
