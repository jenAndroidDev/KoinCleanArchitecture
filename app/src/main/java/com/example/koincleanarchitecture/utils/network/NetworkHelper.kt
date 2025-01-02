package com.example.koincleanarchitecture.utils.network

import android.content.Context
import com.example.koincleanarchitecture.utils.extension.isConnected


class NetworkHelper(private val context: Context) {

    fun checkForInternet(): Boolean =
        context.isConnected()
}