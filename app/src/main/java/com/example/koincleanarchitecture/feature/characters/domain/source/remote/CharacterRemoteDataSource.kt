package com.example.koincleanarchitecture.feature.characters.domain.source.remote

import com.example.koincleanarchitecture.utils.network.NetworkResult
import com.example.rickymortypagination.feature.rickymorty.data.source.remote.model.CharacterResponseModel
import kotlinx.coroutines.flow.Flow


interface CharacterRemoteDataSource {

    fun getAllCharacters(pageNo:Int): Flow<NetworkResult<CharacterResponseModel>>
}