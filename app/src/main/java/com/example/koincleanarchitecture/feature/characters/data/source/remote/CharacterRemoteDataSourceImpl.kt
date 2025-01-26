package com.example.koincleanarchitecture.feature.characters.data.source.remote

import com.example.koincleanarchitecture.core.di.KoinCleanApiNetwork
import com.example.koincleanarchitecture.feature.characters.domain.source.remote.CharacterRemoteDataSource
import com.example.koincleanarchitecture.utils.network.BaseRemoteDataSource
import com.example.koincleanarchitecture.utils.network.NetworkHelper
import com.example.koincleanarchitecture.utils.network.NetworkResult
import com.example.rickymortypagination.feature.rickymorty.data.source.remote.model.CharacterResponseModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharacterRemoteDataSourceImpl(private
val apiNetwork: KoinCleanApiNetwork,
val networkHelper: NetworkHelper,
val ktorClient: HttpClient
    ):CharacterRemoteDataSource, BaseRemoteDataSource(networkHelper) {

    override fun getAllCharacters(pageNo: Int): Flow<NetworkResult<CharacterResponseModel>> = flow{
        emit(NetworkResult.Loading())
        emit(safeApiCall { apiNetwork.getAllCharacters(page = pageNo) })
    }.flowOn(Dispatchers.IO)

}

