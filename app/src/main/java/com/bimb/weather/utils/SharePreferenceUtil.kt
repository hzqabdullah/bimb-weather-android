package com.bimb.weather.utils

import android.content.Context
import android.content.SharedPreferences
import com.bimb.weather.constant.Constants.ALL_COUNTRIES
import com.bimb.weather.constant.Constants.APP_CACHE
import com.bimb.weather.constant.Constants.SELECTED_CITY
import com.bimb.weather.constant.Constants.SELECTED_COUNTRY
import com.bimb.weather.domain.model.CountriesData
import com.bimb.weather.domain.model.Country
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharePreferenceUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(APP_CACHE, Context.MODE_PRIVATE)
    }

    private fun save(key: String, value: Any) {
        val sharedPreference = getSharedPreference()
        if (key.isEmpty()) return
        val editor = sharedPreference.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Long -> editor.putLong(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
        }
        editor.apply()
    }

    fun saveSelectedCountry(country: Country) {
        save(SELECTED_COUNTRY, Gson().toJson(country))
    }

    fun getSelectedCountry(): Country? {
        return Gson().fromJson(
            getSharedPreference().getString(SELECTED_COUNTRY, ""),
            Country::class.java
        ) ?: null
    }

    fun saveSelectedCity(city: String) {
        save(SELECTED_CITY, city)
    }

    fun getSelectedCity(): String? {
        return getSharedPreference().getString(SELECTED_CITY, "")
    }

    fun saveAllCountries(countriesData: CountriesData) {
        save(ALL_COUNTRIES, Gson().toJson(countriesData))
    }

    fun getAllCountries(): CountriesData? {
        return Gson().fromJson(
            getSharedPreference().getString(ALL_COUNTRIES, ""),
            CountriesData::class.java
        ) ?: null
    }

    fun isUserAlreadyLoggedIn(): Boolean {
        return getSelectedCountry() != null && return getSelectedCity() != null
    }
}
