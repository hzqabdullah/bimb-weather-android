package com.bimb.weather.presentation.view.screen.splash

sealed class SplashEvent {
    data object OnDismissedSnackbar : SplashEvent()
    data object OnRefresh : SplashEvent()
}
