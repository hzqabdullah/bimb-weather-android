package com.bimb.weather.presentation.enums

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.bimb.weather.R

enum class Family(val family: FontFamily) {
    MontserratRegular(FontFamily(Font(R.font.montserrat_regular))),
    MontserratMedium(FontFamily(Font(R.font.montserrat_medium))),
    MontserratSemibold(FontFamily(Font(R.font.montserrat_semibold))),
    MontserratBold(FontFamily(Font(R.font.montserrat_bold)));
}
