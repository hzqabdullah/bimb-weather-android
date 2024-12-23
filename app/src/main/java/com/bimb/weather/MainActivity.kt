package com.bimb.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bimb.weather.presentation.enums.Screens
import com.bimb.weather.presentation.theme.WeatherAppTheme
import com.bimb.weather.presentation.view.screen.country.CountryScreen
import com.bimb.weather.presentation.view.screen.splash.SplashScreen
import com.bimb.weather.presentation.view.screen.weather.WeatherScreen
import com.bimb.weather.utils.SharePreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceUtil: SharePreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppNavigationScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigationScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.name
    ) {
        composable(Screens.Splash.name) {
            SplashScreen(navController = navController)
        }

        composable(Screens.Country.name) {
            CountryScreen(navController = navController)
        }

        composable(Screens.Weather.name) {
            WeatherScreen()
        }
    }
}
