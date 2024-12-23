package com.bimb.weather.presentation.view.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bimb.weather.R
import com.bimb.weather.presentation.enums.Screens
import com.bimb.weather.presentation.view.component.EmptyStateView
import com.bimb.weather.presentation.view.component.SnackbarComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController
) {
    val viewModel: SplashViewModel = hiltViewModel()
    val splashState = viewModel.splashState
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(splashState.error != null) {
        coroutineScope.launch {
            delay(4000L)
            viewModel.onEvent(SplashEvent.OnDismissedSnackbar)
        }
    }
    LaunchedEffect(key1 = splashState.isUserAlreadyLoggedIn != null) {
        when (splashState.isUserAlreadyLoggedIn) {
            true -> {
                navController.popBackStack()
                navController.navigate(Screens.Weather.name)
            }

            false -> {
                navController.popBackStack()
                navController.navigate(Screens.Country.name)
            }

            else -> Unit
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(150.dp),
                contentDescription = "Logo",
                painter = painterResource(id = R.drawable.ic_logo)
            )
            if (splashState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 24.dp),
                    color = Color(0xFFFAB03C)
                )
            }
        }

        if (splashState.error != null) {
            SplashScreenError(splashState.error)
        }

        if (splashState.networkIssue != null) {
            EmptyStateView(
                onClickRefresh = {
                    viewModel.onEvent(SplashEvent.OnRefresh)
                }
            )
        }
    }
}

@Composable
fun SplashScreenError(message: String) {
    Popup(
        alignment = Alignment.BottomCenter
    ) {
        SnackbarComponent(message = message)
    }
}
