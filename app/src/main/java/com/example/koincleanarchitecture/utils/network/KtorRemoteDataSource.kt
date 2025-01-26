package com.example.koincleanarchitecture.utils.network

import androidx.annotation.WorkerThread
import coil3.network.HttpException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

private const val Tag = "KtorRemoteDataSource"
open class KtorRemoteDataSource(
    val networkHelper: NetworkHelper
) {

    @WorkerThread
    protected suspend inline fun <reified T> safeApiCall(call:suspend ()->HttpResponse):NetworkResult<T>{
        return when{
            networkHelper.checkForInternet()->{
                try {
                    call.invoke().let { httpResponse ->
                        when(httpResponse.status){
                            HttpStatusCode.OK->{
                                NetworkResult.Success(
                                    data = httpResponse.body<T>(),
                                    message = httpResponse.status.description,
                                    code = httpResponse.status.value
                                )
                            }
                            else -> {
                                NetworkResult.Error(
                                    message = httpResponse.status.description,
                                    code = httpResponse.status.value
                                )
                            }
                        }
                    }
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