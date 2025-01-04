package com.example.koincleanarchitecture.core.di

import retrofit2.http.GET
import retrofit2.http.Query

interface KoinCleanApiNetwork {

    @GET("api/character")
    suspend fun getCharacters(@Query("page") page:Int)
}