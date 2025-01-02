package com.example.koincleanarchitecture.utils.network

import com.example.koincleanarchitecture.utils.exception.ApiException
import com.example.koincleanarchitecture.utils.exception.NoInternetException
import timber.log.Timber


interface NetworkResultParser {

    /**
     * Abstracts the boilerplate to parse error results
     */
    fun <T, R> parseErrorNetworkResult(networkResult: NetworkResult<T>): Result<R> {
        return when (networkResult) {
            is NetworkResult.Loading -> {
                Result.Loading
            }
            is NetworkResult.Success -> {
                Timber.w("Not an error result")
                val cause = IllegalStateException("Parsing non error result!")
                Result.Error(ApiException(cause))
            }
            is NetworkResult.Error -> {
                val cause = IllegalStateException(networkResult.message ?: "Something went wrong")
                Result.Error(ApiException(networkResult.message, cause))
            }
            is NetworkResult.NoInternet -> {
                val cause = NoInternetException("Please check internet connection")
                Result.Error(cause)
            }
            is NetworkResult.UnAuthorized -> {
                val cause = IllegalStateException("Something went wrong")
                Result.Error(ApiException(networkResult.message, cause))
            }
        }
    }
}
