package com.bimb.weather.presentation.view.component

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bimb.weather.R
import com.bimb.weather.constant.Constants.FLAG_ICON_URL
import com.bimb.weather.domain.model.CountriesData
import com.bimb.weather.domain.model.Country
import com.bimb.weather.presentation.enums.Family

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySheet(
    countriesData: CountriesData,
    onClickCountryCity: (Country, String) -> Unit = { _, _ -> },
    onDismissed: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isCityState by remember {
        mutableStateOf(false)
    }
    var selectedCountry by remember {
        mutableStateOf<Country?>(null)
    }
    var searchText by remember {
        mutableStateOf("")
    }
    val countries by remember(isCityState, searchText) {
        derivedStateOf {
            if (!isCityState && searchText.isNotEmpty()) {
                countriesData.countries.filter {
                    it.country.contains(searchText, ignoreCase = true)
                }
            } else {
                countriesData.countries
            }
        }
    }
    val cities by remember(isCityState, searchText, selectedCountry) {
        derivedStateOf {
            if (isCityState && searchText.isNotEmpty() && selectedCountry != null) {
                selectedCountry?.cities.orEmpty().filter {
                    it.contains(searchText, ignoreCase = true)
                }
            } else {
                selectedCountry?.cities.orEmpty()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onDismissed.invoke()
        },
        containerColor = Color(0xFFFFFFFF),
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 24.dp
                )
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isCityState) {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                isCityState = false
                                selectedCountry = null
                                searchText = ""
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            },
                        contentDescription = "Back",
                        painter = painterResource(id = R.drawable.ic_arrow_left)
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (!isCityState) "Select Country" else "Select City in ${selectedCountry?.country.orEmpty()}",
                    style = TextStyle(
                        fontFamily = Family.MontserratBold.family,
                        fontSize = 20.sp,
                    ),
                    color = Color(0xFF000000)
                )
            }
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
                            .padding(16.dp)
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = if (!isCityState) "Search any countries" else "Search for any cities in ${selectedCountry?.country.orEmpty()}",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (!isCityState) {
                    items(countries) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isCityState = true
                                    selectedCountry = it
                                    searchText = ""
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(24.dp),
                                model = String.format(FLAG_ICON_URL, it.iso2),
                                contentDescription = "Country Flag",
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                text = it.country,
                                style = TextStyle(
                                    fontFamily = Family.MontserratRegular.family,
                                    fontSize = 14.sp,
                                ),
                                color = Color(0xFF000000)
                            )
                            Image(
                                modifier = Modifier.size(16.dp),
                                contentDescription = "Select",
                                painter = painterResource(id = R.drawable.ic_chevron_right)
                            )
                        }
                    }
                } else {
                    items(cities) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedCountry != null) {
                                        onClickCountryCity.invoke(selectedCountry!!, it)
                                    }
                                }
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                text = it,
                                style = TextStyle(
                                    fontFamily = Family.MontserratRegular.family,
                                    fontSize = 14.sp,
                                ),
                                color = Color(0xFF000000)
                            )
                            Image(
                                modifier = Modifier.size(16.dp),
                                contentDescription = "Select",
                                painter = painterResource(id = R.drawable.ic_chevron_right)
                            )
                        }
                    }
                }
            }
        }
    }
}
