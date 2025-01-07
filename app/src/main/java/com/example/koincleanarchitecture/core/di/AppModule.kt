package com.example.koincleanarchitecture.core.di

import com.example.koincleanarchitecture.BuildConfig
import com.example.koincleanarchitecture.utils.network.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(KoinCleanApiNetwork::class.java)
    }
}

val networkHelper = module {
    single {
        NetworkHelper(androidContext())
    }
}
