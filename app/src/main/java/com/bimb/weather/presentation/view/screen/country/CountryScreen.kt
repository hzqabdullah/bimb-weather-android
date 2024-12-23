package com.bimb.weather.presentation.view.screen.country

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bimb.weather.R
import com.bimb.weather.constant.Constants.FLAG_ICON_URL
import com.bimb.weather.presentation.enums.Family
import com.bimb.weather.presentation.enums.Screens
import com.bimb.weather.presentation.view.component.CountrySheet
import com.bimb.weather.presentation.view.component.EmptyStateView
import com.bimb.weather.presentation.view.component.LoadingScreen
import com.bimb.weather.presentation.view.component.SnackbarComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CountryScreen(
    navController: NavController
) {
    val viewModel: CountryViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val countryState = viewModel.countryState
    val countriesData = countryState.countriesData
    val selectedCountry = countryState.selectedCountry?.country
    val selectedCountryIcon = countryState.selectedCountry?.iso2
    val selectedCity = countryState.selectedCity
    val isSelectedCity = countryState.isSelectedCity

    val isEnableButton by remember(selectedCity) {
        derivedStateOf {
            selectedCity != null
        }
    }
    var isShowCountrySheet by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(countryState.error != null) {
        coroutineScope.launch {
            delay(4000L)
            viewModel.onEvent(CountryEvent.OnDismissedSnackbar)
        }
    }
    LaunchedEffect(key1 = isSelectedCity) {
        if (isSelectedCity) {
            navController.popBackStack()
            navController.navigate(Screens.Weather.name)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 140.dp)
                    .size(70.dp),
                contentDescription = "Location",
                painter = painterResource(id = R.drawable.ic_location)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = "Choose Your Location",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = Family.MontserratBold.family,
                    fontSize = 20.sp,
                ),
                color = Color(0xFF000000)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Please select your country and city to help us for \ngive you a better experience",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = Family.MontserratRegular.family,
                    fontSize = 14.sp
                ),
                color = Color(0xFF000000)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 50.dp,
                        start = 24.dp,
                        end = 24.dp
                    )
                    .clickable {
                        isShowCountrySheet = true
                    },
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!selectedCountryIcon.isNullOrEmpty()) {
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = String.format(FLAG_ICON_URL, selectedCountryIcon),
                            contentDescription = "Country Flag",
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = if (selectedCountry.isNullOrEmpty()) "Select Country and City" else "$selectedCity, $selectedCountry",
                        style = TextStyle(
                            fontFamily = Family.MontserratRegular.family,
                            fontSize = 14.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color(0xFF000000)
                    )
                    Image(
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Select",
                        painter = painterResource(id = R.drawable.ic_arrow_right)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color(0xFFFAB03C),
                    disabledContainerColor = Color(0xFFC4C4C4)
                ),
                shape = ShapeDefaults.Small,
                onClick = {
                    if (!isEnableButton) return@Button
                    viewModel.onEvent(CountryEvent.OnClickContinue)
                },
                enabled = isEnableButton
            ) {
                Text(
                    text = "Continue",
                    style = TextStyle(
                        fontFamily = Family.MontserratSemibold.family,
                        fontSize = 16.sp
                    ),
                    color = Color(0xFFFFFFFF)
                )
            }
        }

        if (isShowCountrySheet && countriesData != null) {
            CountrySheet(
                countriesData = countriesData,
                onClickCountryCity = { country, city ->
                    viewModel.onEvent(
                        CountryEvent.OnSelectedCountryCity(
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

        if (countryState.error != null) {
            CountryScreenError(countryState.error)
        }

        if (countryState.isLoading) {
            LoadingScreen(message = "Searching...")
        }

        if (countryState.networkIssue != null) {
            EmptyStateView(
                onClickRefresh = {
                    viewModel.onEvent(CountryEvent.OnRefresh)
                }
            )
        }
    }
}

@Composable
fun CountryScreenError(message: String) {
    Popup(
        alignment = Alignment.BottomCenter
    ) {
        SnackbarComponent(message = message)
    }
}
