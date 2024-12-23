package com.bimb.weather.presentation.view.screen.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bimb.weather.domain.model.Country
import com.bimb.weather.domain.usecase.GetWeatherForCityUseCase
import com.bimb.weather.utils.JsonExtractionUtil
import com.bimb.weather.utils.ResultWrapper
import com.bimb.weather.utils.SharePreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val jsonExtractionUtil: JsonExtractionUtil,
    private val preferenceUtil: SharePreferenceUtil,
    private val getWeatherForCityUseCase: GetWeatherForCityUseCase
) : ViewModel() {

    var weatherState by mutableStateOf(WeatherState())
        private set

    init {
        handleOnCreate()
    }

    fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.OnClickSearchCity -> getCityWeather(event.city)
            is WeatherEvent.OnDismissedSnackbar -> onDismissedSnackbar()
            is WeatherEvent.OnSelectedCountryCity -> onSelectedCountryCity(
                event.country,
                event.city
            )

            is WeatherEvent.OnRefresh -> onRefresh()
        }
    }

    private fun handleOnCreate() {
        var countriesData = preferenceUtil.getAllCountries()
        val selectedCountry = preferenceUtil.getSelectedCountry()
        val selectedCity = preferenceUtil.getSelectedCity()

        if (countriesData == null) {
            countriesData = jsonExtractionUtil.countriesData
        }
        if (selectedCountry != null && selectedCity != null) {
            weatherState = weatherState.copy(countriesData = countriesData)
            getCityWeather(selectedCity)
        }
    }

    private fun getCityWeather(city: String, country: Country? = null) {
        viewModelScope.launch {
            getWeatherForCityUseCase.invoke(city).collect { response ->
                when (response) {
                    is ResultWrapper.Loading -> {
                        weatherState = weatherState.copy(
                            isLoading = true
                        )
                    }

                    is ResultWrapper.NetworkFailed -> {
                        weatherState = weatherState.copy(
                            isLoading = false,
                            networkIssue = response.message
                        )
                    }

                    is ResultWrapper.Error -> {
                        weatherState = weatherState.copy(
                            isLoading = false,
                            networkIssue = null,
                            error = response.message
                        )
                    }

                    is ResultWrapper.Success -> {
                        val weather = response.data
                        if (weather != null) {
                            val countryName = getCountryName(weather.weatherSystemInfo.country)

                            if (country != null) {
                                preferenceUtil.saveSelectedCountry(country)
                                preferenceUtil.saveSelectedCity(weather.cityName)
                            }
                            weatherState = weatherState.copy(
                                isLoading = false,
                                weather = weather,
                                searchCity = weather.cityName,
                                searchCountry = countryName,
                                selectedCity = preferenceUtil.getSelectedCity(),
                                selectedCountry = preferenceUtil.getSelectedCountry(),
                                networkIssue = null,
                                error = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSelectedCountryCity(country: Country, city: String) {
        getCityWeather(city, country)
    }

    private fun onDismissedSnackbar() {
        weatherState = weatherState.copy(error = null)
    }

    private fun onRefresh() {
        handleOnCreate()
    }

    private fun getCountryName(countryCode: String): String {
        var countryName = ""
        val countries = preferenceUtil.getAllCountries()?.countries

        if (!countries.isNullOrEmpty()) {
            countryName = countries.find { it.iso2 == countryCode }?.country.orEmpty()
        }
        return countryName
    }
}
