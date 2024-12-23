package com.bimb.weather.utils

import com.bimb.weather.domain.model.ErrorResponse
import com.google.gson.Gson
import okio.IOException
import retrofit2.Response

suspend fun <T> networkRequest(response: suspend () -> Response<T>): ResultWrapper<T?> {
    return try {
        val apiResponse = response()
        when (apiResponse.isSuccessful) {
            true -> ResultWrapper.Success(apiResponse.body())
            false -> ResultWrapper.Error(errorResult(apiResponse.errorBody()?.string()))
        }
    } catch (exception: IOException) {
        ResultWrapper.NetworkFailed(exception.localizedMessage.orEmpty())
    } catch (exception: Exception) {
        ResultWrapper.Error(exception.localizedMessage.orEmpty())
    }
}

private fun errorResult(errorResponse: String?): String {
    val errorMessage = "Oops! Something went wrong"
    val error = Gson().fromJson(errorResponse, ErrorResponse::class.java)
    return when (error != null) {
        true -> error.message ?: errorMessage
        false -> errorMessage
    }
}
