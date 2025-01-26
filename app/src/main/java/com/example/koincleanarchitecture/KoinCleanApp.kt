package com.example.koincleanarchitecture

import android.app.Application
import com.example.koincleanarchitecture.core.di.apiService
import com.example.koincleanarchitecture.core.di.ktorModule
import com.example.koincleanarchitecture.core.di.networkHelper
import com.example.koincleanarchitecture.core.di.networkModule
import com.example.koincleanarchitecture.feature.characters.domain.di.characterFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class KoinCleanApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@KoinCleanApp)
            androidLogger(level = Level.DEBUG)
            modules(
                ktorModule,
                apiService,
                networkHelper,
                characterFeatureModule
                )
        }
    }
}