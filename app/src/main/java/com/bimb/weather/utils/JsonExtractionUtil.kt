package com.bimb.weather.utils

import android.content.Context
import com.bimb.weather.R
import com.bimb.weather.domain.model.CountriesData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JsonExtractionUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var countries: CountriesData
    val countriesData: CountriesData
        get() {
            if (!this::countries.isInitialized) loadCountriesData()
            return countries
        }

    private fun loadCountriesData() {
        val countriesJson = this.context.resources.openRawResource(R.raw.countries)
            .bufferedReader()
            .use { it.readText() }

        countries = Gson().fromJson(countriesJson, CountriesData::class.java)
    }
}
