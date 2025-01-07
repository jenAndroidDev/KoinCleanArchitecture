package com.example.koincleanarchitecture.core.di

import com.example.rickymortypagination.feature.rickymorty.data.source.remote.model.CharacterResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KoinCleanApiNetwork {

    @GET("api/character")
    suspend fun getAllCharacters(@Query("page") page:Int):Response<CharacterResponseModel>
}