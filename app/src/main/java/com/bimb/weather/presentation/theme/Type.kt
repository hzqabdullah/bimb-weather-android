package com.bimb.weather.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bimb.weather.presentation.enums.Family

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Family.MontserratRegular.family,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Family.MontserratSemibold.family,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Family.MontserratRegular.family,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)
