package com.bimb.weather.domain.usecase

import com.bimb.weather.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountriesAndCitiesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    fun invoke() = repository.getCountriesAndCities()
}
