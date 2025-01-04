package com.example.koincleanarchitecture

import android.app.Application
import com.example.koincleanarchitecture.core.di.networkHelper
import com.example.koincleanarchitecture.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinCleanApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinCleanApp)
            modules(
                networkHelper,
                networkModule
            )
        }
    }
}