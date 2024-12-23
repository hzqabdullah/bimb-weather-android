package com.bimb.weather.presentation.view.screen.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.bimb.weather.R
import com.bimb.weather.constant.Constants.WEATHER_ICON_URL
import com.bimb.weather.presentation.enums.Family
import com.bimb.weather.presentation.view.component.CountrySheet
import com.bimb.weather.presentation.view.component.EmptyStateView
import com.bimb.weather.presentation.view.component.LoadingScreen
import com.bimb.weather.presentation.view.component.SnackbarComponent
import com.bimb.weather.utils.DateFormatterUtil.unixToDateString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val weatherState = viewModel.weatherState
    val countriesData = weatherState.countriesData
    val weather = weatherState.weather
    val statusInfo = weatherState.weather?.weatherStatus?.first()
    val weatherStatus = statusInfo?.status.orEmpty()
    val weatherIcon = statusInfo?.icon.orEmpty()
    val searchCity = weatherState.searchCity.orEmpty()
    val searchCountry = weatherState.searchCountry.orEmpty()
    val mainInfo = weather?.weatherMainInfo
    val temperature = mainInfo?.currentTemperature
    val humidity = mainInfo?.humidity
    val seaLevel = mainInfo?.pressureAtSeaLevel
    val windSpeed = weather?.windStatus?.windSpeed
    val systemInfo = weather?.weatherSystemInfo
    val timeZone = weather?.timeZone ?: 0L
    val sunrise = unixToDateString(unixTimestamp = systemInfo?.sunrise ?: 0L, timeZone = timeZone)
    val sunset = unixToDateString(unixTimestamp = systemInfo?.sunset ?: 0L, timeZone = timeZone)
    val coordinate = weather?.coordinate
    val latitude = coordinate?.latitude ?: 0.0
    val longitude = coordinate?.longitude ?: 0.0

    var isShowCountrySheet by remember {
        mutableStateOf(false)
    }
    var isSearchMode by remember {
        mutableStateOf(false)
    }
    var searchText by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(weatherState.error != null) {
        coroutineScope.launch {
            delay(4000L)
            viewModel.onEvent(WeatherEvent.OnDismissedSnackbar)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1F1F1))
    ) {
        if (weather != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(0.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isSearchMode) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                isSearchMode = false
                                                searchText = ""
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            },
                                        contentDescription = "Back",
                                        painter = painterResource(id = R.drawable.ic_arrow_left)
                                    )
                                    BasicTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = searchText,
                                        onValueChange = {
                                            searchText = it
                                        },
                                        textStyle = TextStyle(
                                            fontFamily = Family.MontserratRegular.family,
                                            fontSize = 14.sp
                                        ),
                                        decorationBox = { innerTextField ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        color = Color(0xFFFFFFFF),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = Color(0xFF8D8D8D),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(4.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .weight(1f)
                                                            .padding(horizontal = 8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        if (searchText.isEmpty()) {
                                                            Text(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                text = "Search for any cities",
                                                                style = TextStyle(
                                                                    fontFamily = Family.MontserratRegular.family,
                                                                    fontSize = 14.sp
                                                                ),
                                                                color = Color(0xFF8F8F8F)
                                                            )
                                                        } else {
                                                            innerTextField.invoke()
                                                        }
                                                    }
                                                    Box(
                                                        modifier = Modifier
                                                            .wrapContentHeight()
                                                            .background(
                                                                color = Color(0xFFFAB03C),
                                                                shape = RoundedCornerShape(10.dp)
                                                            )
                                                            .border(
                                                                width = 1.dp,
                                                                color = Color(0xFFFAB03C),
                                                                shape = RoundedCornerShape(10.dp)
                                                            )
                                                            .clickable {
                                                                focusManager.clearFocus()
                                                                keyboardController?.hide()
                                                                viewModel.onEvent(
                                                                    WeatherEvent.OnClickSearchCity(
                                                                        searchText
                                                                    )
                                                                )
                                                            }
                                                            .padding(
                                                                horizontal = 18.dp,
                                                                vertical = 12.dp
                                                            )
                                                    ) {
                                                        Text(
                                                            text = "Search",
                                                            style = TextStyle(
                                                                fontFamily = Family.MontserratMedium.family,
                                                                fontSize = 12.sp
                                                            ),
                                                            color = Color(0xFFFFFFFF)
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions {
                                            focusManager.clearFocus()
                                            keyboardController?.hide()
                                        }
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .padding(end = 16.dp)
                                            .size(24.dp)
                                            .clickable {
                                                isShowCountrySheet = true
                                            },
                                        alignment = Alignment.CenterEnd,
                                        contentDescription = "Set Location",
                                        painter = painterResource(id = R.drawable.ic_set_location)
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                isSearchMode = true
                                                searchText = ""
                                                focusManager.clearFocus()
                                                keyboardController?.hide()
                                            },
                                        alignment = Alignment.CenterEnd,
                                        contentDescription = "Search",
                                        painter = painterResource(id = R.drawable.ic_search)
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 52.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = searchCity,
                                style = TextStyle(
                                    fontFamily = Family.MontserratBold.family,
                                    fontSize = 18.sp,
                                ),
                                textAlign = TextAlign.Center,
                                color = Color(0xFF000000)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = searchCountry,
                                style = TextStyle(
                                    fontFamily = Family.MontserratBold.family,
                                    fontSize = 16.sp,
                                ),
                                textAlign = TextAlign.Center,
                                color = Color(0xFF000000)
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(150.dp),
                                model = String.format(WEATHER_ICON_URL, weatherIcon),
                                contentDescription = "Weather Icon",
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 52.dp),
                                text = weatherStatus,
                                style = TextStyle(
                                    fontFamily = Family.MontserratRegular.family,
                                    fontSize = 16.sp,
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 52.dp),
                            text = if (temperature != null) "${temperature.toInt()}°" else "0°",
                            style = TextStyle(
                                fontFamily = Family.MontserratMedium.family,
                                fontSize = 48.sp,
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            color = Color(0xFF000000)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Humidity",
                                painter = painterResource(id = R.drawable.ic_humidity)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${humidity.toString()}%",
                                style = TextStyle(
                                    fontFamily = Family.MontserratMedium.family,
                                    fontSize = 14.sp,
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Wind",
                                painter = painterResource(id = R.drawable.ic_wind)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = String.format(Locale.getDefault(), "%.2f m/s", windSpeed),
                                style = TextStyle(
                                    fontFamily = Family.MontserratMedium.family,
                                    fontSize = 14.sp,
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(40.dp),
                                contentDescription = "Sea Level",
                                painter = painterResource(id = R.drawable.ic_sea_level)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${seaLevel.toString()} hPa",
                                style = TextStyle(
                                    fontFamily = Family.MontserratMedium.family,
                                    fontSize = 14.sp,
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Localized",
                        style = TextStyle(
                            fontFamily = Family.MontserratSemibold.family,
                            fontSize = 20.sp,
                        ),
                        maxLines = 1,
                        color = Color(0xFF000000)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = "Sunrise",
                                    painter = painterResource(id = R.drawable.ic_sunrise)
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Sunrise at ${sunrise.uppercase()}",
                                    style = TextStyle(
                                        fontFamily = Family.MontserratRegular.family,
                                        fontSize = 16.sp,
                                    ),
                                    maxLines = 1,
                                    color = Color(0xFF000000)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    modifier = Modifier.size(32.dp),
                                    contentDescription = "Sunrise",
                                    painter = painterResource(id = R.drawable.ic_sunset)
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Sunset at ${sunset.uppercase()}",
                                    style = TextStyle(
                                        fontFamily = Family.MontserratRegular.family,
                                        fontSize = 16.sp,
                                    ),
                                    maxLines = 1,
                                    color = Color(0xFF000000)
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Coordinate",
                        style = TextStyle(
                            fontFamily = Family.MontserratSemibold.family,
                            fontSize = 20.sp,
                        ),
                        maxLines = 1,
                        color = Color(0xFF000000)
                    )
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Latitude at $latitude",
                                style = TextStyle(
                                    fontFamily = Family.MontserratRegular.family,
                                    fontSize = 16.sp,
                                ),
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Longitude at $longitude",
                                style = TextStyle(
                                    fontFamily = Family.MontserratRegular.family,
                                    fontSize = 16.sp,
                                ),
                                maxLines = 1,
                                color = Color(0xFF000000)
                            )
                        }
                    }
                }
            }
        }

        if (isShowCountrySheet && countriesData != null) {
            CountrySheet(
                countriesData = countriesData,
                onClickCountryCity = { country, city ->
                    viewModel.onEvent(
                        WeatherEvent.OnSelectedCountryCity(
                            country, city
                        )
                    )
                    isShowCountrySheet = false
                },
                onDismissed = {
                    isShowCountrySheet = false
                }
            )
        }

        if (weatherState.error != null) {
            WeatherScreenError(weatherState.error)
        }

        if (weatherState.isLoading) {
            LoadingScreen(message = "Searching...")
        }

        if (weatherState.networkIssue != null) {
            EmptyStateView(
                onClickRefresh = {
                    viewModel.onEvent(WeatherEvent.OnRefresh)
                }
            )
        }
    }
}

@Composable
fun WeatherScreenError(message: String) {
    Popup(
        alignment = Alignment.BottomCenter
    ) {
        SnackbarComponent(message = message)
    }
}
