package com.example.koincleanarchitecture.feature.characters.domain.source.remote

import com.example.koincleanarchitecture.utils.network.NetworkResult
import com.example.koincleanarchitecture.feature.characters.data.source.remote.model.CharacterResponseModel
import kotlinx.coroutines.flow.Flow


interface CharacterRemoteDataSource {

    fun getAllCharacters(pageNo:Int): Flow<NetworkResult<CharacterResponseModel>>
}