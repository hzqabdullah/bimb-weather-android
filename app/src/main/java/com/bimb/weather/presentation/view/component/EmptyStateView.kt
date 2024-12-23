package com.bimb.weather.presentation.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimb.weather.R
import com.bimb.weather.presentation.enums.Family

@Composable
fun EmptyStateView(
    onClickRefresh: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1F1F1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_network_issue),
            contentDescription = "Network Issue"
        )
        Text(
            modifier = Modifier
                .clickable {
                    onClickRefresh.invoke()
                }
                .padding(8.dp),
            text = "Click to refresh",
            style = TextStyle(
                fontFamily = Family.MontserratSemibold.family,
                fontSize = 16.sp
            ),
            color = Color(0xFF000000)
        )
    }
}

@Preview
@Composable
fun EmptyStateViewPreview() {
    EmptyStateView()
}
