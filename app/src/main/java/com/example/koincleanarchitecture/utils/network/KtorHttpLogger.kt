package com.example.koincleanarchitecture.utils.network

import io.ktor.client.plugins.logging.Logger
import timber.log.Timber

private const val Tag = "KtorHttpLogger"
class KtorHttpLogger:Logger {
    override fun log(message: String) {
        Timber.tag(Tag).d("log message:$message")
    }
}