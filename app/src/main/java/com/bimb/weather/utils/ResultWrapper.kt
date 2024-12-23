package com.bimb.weather.utils

sealed class ResultWrapper<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> : ResultWrapper<T>()
    class Success<T>(data: T) : ResultWrapper<T>(data)
    class Error<T>(message: String) : ResultWrapper<T>(message = message)
    class NetworkFailed<T>(message: String) : ResultWrapper<T>(message = message)
}
