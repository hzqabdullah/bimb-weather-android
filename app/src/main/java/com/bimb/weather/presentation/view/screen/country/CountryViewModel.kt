package com.bimb.weather.presentation.view.screen.country

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
class CountryViewModel @Inject constructor(
    private val jsonExtractionUtil: JsonExtractionUtil,
    private val preferenceUtil: SharePreferenceUtil,
    private val getWeatherForCityUseCase: GetWeatherForCityUseCase
) : ViewModel() {

    var countryState by mutableStateOf(CountryState())
        private set

    init {
        handleOnCreate()
    }

    fun onEvent(event: CountryEvent) {
        when (event) {
            is CountryEvent.OnSelectedCountryCity -> onSelectedCountryCity(
                event.country,
                event.city
            )

            is CountryEvent.OnClickContinue -> onClickContinue()
            is CountryEvent.OnDismissedSnackbar -> onDismissedSnackbar()
            is CountryEvent.OnRefresh -> onRefresh()
        }
    }

    private fun handleOnCreate() {
        var countriesData = preferenceUtil.getAllCountries()
        if (countriesData == null) {
            countriesData = jsonExtractionUtil.countriesData
        }
        countryState = countryState.copy(countriesData = countriesData)
    }

    private fun onSelectedCountryCity(country: Country, city: String) {
        countryState = countryState.copy(
            selectedCountry = country,
            selectedCity = city
        )
    }

    private fun onClickContinue() {
        if (countryState.selectedCountry != null && countryState.selectedCity != null) {
            searchForCity()
        }
    }

    private fun searchForCity() {
        val city = countryState.selectedCity.orEmpty()

        viewModelScope.launch {
            getWeatherForCityUseCase.invoke(city).collect { response ->
                when (response) {
                    is ResultWrapper.Loading -> {
                        countryState = countryState.copy(
                            isLoading = true
                        )
                    }

                    is ResultWrapper.NetworkFailed -> {
                        countryState = countryState.copy(
                            isLoading = false,
                            networkIssue = response.message
                        )
                    }

                    is ResultWrapper.Error -> {
                        countryState = countryState.copy(
                            isLoading = false,
                            networkIssue = null,
                            error = response.message
                        )
                    }

                    is ResultWrapper.Success -> {
                        val weather = response.data
                        if (weather != null) {
                            countryState.selectedCountry?.let {
                                preferenceUtil.saveSelectedCountry(it)
                            }
                            countryState.selectedCity?.let {
                                preferenceUtil.saveSelectedCity(it)
                            }
                            countryState = countryState.copy(
                                isSelectedCity = true,
                                networkIssue = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onDismissedSnackbar() {
        countryState = countryState.copy(error = null)
    }

    private fun onRefresh() {
        handleOnCreate()
    }
}
