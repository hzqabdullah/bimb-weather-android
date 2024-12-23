package com.bimb.weather.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bimb.weather.presentation.enums.Family

@Composable
fun LoadingScreen(message: String) {
    Dialog(
        onDismissRequest = {}
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(52.dp),
                    color = Color(0xFFFAB03C)
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = message,
                    style = TextStyle(
                        fontFamily = Family.MontserratSemibold.family,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(message = "Searching...")
}
