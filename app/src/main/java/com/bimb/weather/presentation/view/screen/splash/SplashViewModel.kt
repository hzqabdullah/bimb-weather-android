package com.bimb.weather.presentation.view.screen.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bimb.weather.domain.usecase.GetCountriesAndCitiesUseCase
import com.bimb.weather.utils.JsonExtractionUtil
import com.bimb.weather.utils.ResultWrapper
import com.bimb.weather.utils.SharePreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val jsonExtractionUtil: JsonExtractionUtil,
    private val preferenceUtil: SharePreferenceUtil,
    private val getCountriesAndCitiesUseCase: GetCountriesAndCitiesUseCase
) : ViewModel() {

    var splashState by mutableStateOf(SplashState())
        private set

    init {
        handleOnCreate()
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.OnDismissedSnackbar -> onDismissedSnackbar()
            SplashEvent.OnRefresh -> onRefresh()
        }
    }

    private fun handleOnCreate() {
        getAllCountries()
    }

    private fun getAllCountries() {
        viewModelScope.launch {
            if (preferenceUtil.getAllCountries() == null) {
                getCountriesAndCitiesUseCase.invoke().collect { response ->
                    when (response) {
                        is ResultWrapper.Loading -> {
                            splashState = splashState.copy(
                                isLoading = true,
                                isUserAlreadyLoggedIn = null,
                            )
                        }

                        is ResultWrapper.NetworkFailed -> {
                            splashState = splashState.copy(
                                isLoading = false,
                                networkIssue = response.message
                            )
                        }

                        is ResultWrapper.Error -> {
                            splashState = splashState.copy(
                                isLoading = false,
                                isUserAlreadyLoggedIn = null,
                                networkIssue = null
                            )
                            val countriesData = jsonExtractionUtil.countriesData
                            preferenceUtil.saveAllCountries(countriesData)
                        }

                        is ResultWrapper.Success -> {
                            val countriesData = response.data
                            if (countriesData != null) {
                                splashState = splashState.copy(
                                    isLoading = false,
                                    countriesData = countriesData,
                                    networkIssue = null,
                                    error = null
                                )
                                preferenceUtil.saveAllCountries(countriesData)
                            }
                        }
                    }
                }
            }
            splashState = splashState.copy(
                isLoading = false,
                isUserAlreadyLoggedIn = preferenceUtil.isUserAlreadyLoggedIn(),
                error = null
            )
        }
    }

    private fun onDismissedSnackbar() {
        splashState = splashState.copy(error = null)
    }

    private fun onRefresh() {
        handleOnCreate()
    }
}
