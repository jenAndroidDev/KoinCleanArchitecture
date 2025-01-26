package com.example.koincleanarchitecture.utils.network

import androidx.annotation.WorkerThread
import coil3.network.HttpException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

open class KtorRemoteDataSource(
    private val networkHelper: NetworkHelper
) {

    @WorkerThread
    protected suspend fun <T> safeApiCall(call:suspend ()->HttpResponse):NetworkResult<T>{
        return when{
            networkHelper.checkForInternet()->{
                try {
                    call.invoke().let { httpResponse ->
                        httpResponse.call.request
                    }
                    NetworkResult.Success(T)
                }catch (e:HttpException){
                    NetworkResult.Error(
                        message = e.stackTraceToString(),
                        uiMessage = e.localizedMessage,
                        code = e.response.code
                    )
                }
            }
            else->NetworkResult.NoInternet("Unable to Connect to Internet")
        }
    }

}