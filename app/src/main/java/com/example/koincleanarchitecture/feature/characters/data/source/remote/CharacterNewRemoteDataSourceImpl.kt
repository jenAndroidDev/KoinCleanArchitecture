package com.example.koincleanarchitecture.feature.characters.data.source.remote


import com.example.koincleanarchitecture.KtorClientApiService
import com.example.koincleanarchitecture.feature.characters.domain.source.remote.CharacterRemoteDataSource
import com.example.koincleanarchitecture.utils.network.KtorRemoteDataSource
import com.example.koincleanarchitecture.utils.network.NetworkHelper
import com.example.koincleanarchitecture.utils.network.NetworkResult
import com.example.koincleanarchitecture.feature.characters.data.source.remote.model.CharacterResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CharacterNewRemoteDataSourceImpl(
    networkHelper: NetworkHelper,
    private val apiService: KtorClientApiService
) :CharacterRemoteDataSource,KtorRemoteDataSource(
    networkHelper = networkHelper
){
    override fun getAllCharacters(pageNo: Int): Flow<NetworkResult<CharacterResponseModel>>  = flow{
        emit(NetworkResult.Loading())
        emit(safeApiCall<CharacterResponseModel> { apiService.getCharacters(pageNo) })
    }.flowOn(Dispatchers.IO)

}
