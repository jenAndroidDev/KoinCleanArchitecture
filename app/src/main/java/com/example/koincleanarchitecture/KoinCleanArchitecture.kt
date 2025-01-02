package com.example.koincleanarchitecture

import android.app.Application
import timber.log.Timber

class KoinCleanArchitecture:Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}