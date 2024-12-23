package com.bimb.weather.presentation.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bimb.weather.presentation.enums.Family

@Composable
fun SnackbarComponent(
    message: String,
    icon: ImageVector = Icons.Default.Info,
    iconColor: Color = Color(0xFFDD5147)
) {
    Snackbar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        containerColor = Color(0xFF333A49),
        shape = ShapeDefaults.Medium.copy(all = CornerSize(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Top),
                imageVector = icon,
                contentDescription = "Icon",
                colorFilter = ColorFilter.tint(color = iconColor)
            )
            Text(
                text = message,
                style = TextStyle(
                    fontFamily = Family.MontserratSemibold.family,
                    fontSize = 16.sp
                ),
                color = Color(0xFFFFFFFF)
            )
        }
    }
}

@Preview
@Composable
fun SnackbarComponentPreview() {
    SnackbarComponent(message = "Failed to log in")
}
