package com.example.koincleanarchitecture.core.di

import com.example.koincleanarchitecture.BuildConfig
import com.example.koincleanarchitecture.KtorClientApiService
import com.example.koincleanarchitecture.utils.network.KtorHttpLogger
import com.example.koincleanarchitecture.utils.network.NetworkHelper
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.MINUTES)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClientBuilder.build())
            .build()
            .create(KoinCleanApiNetwork::class.java)
    }
}

val networkHelper = module {
    single {
        NetworkHelper(androidContext())
    }
}
val apiService = module {
    single {
        KtorClientApiService(get())
    }
}
/*
* Naming as Ktor Module since Retrofit Existing Module is Present
* will migrate once the implementations are working fine*/
private const val Tag = "KtorTag"
val ktorModule = module {
    single {
        HttpClient(OkHttp) {
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(BuildConfig.BASE_URL)
            }
            install(Logging){
                logger = KtorHttpLogger()
                level = LogLevel.BODY
            }
            install(HttpTimeout){
                requestTimeoutMillis = 5_000
            }
            install(ContentNegotiation){
                json(Json{
                    ignoreUnknownKeys =true
                    prettyPrint = true
                })
            }
        }
    }
}

