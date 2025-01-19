package com.example.koincleanarchitecture.feature.characters.data.repository

import android.util.Log
import androidx.compose.runtime.currentCompositionErrors
import com.example.koincleanarchitecture.feature.characters.domain.model.Character
import com.example.koincleanarchitecture.feature.characters.domain.repository.CharacterRepository
import com.example.koincleanarchitecture.feature.characters.domain.source.remote.CharacterRemoteDataSource
import com.example.koincleanarchitecture.utils.network.NetworkResult
import com.example.koincleanarchitecture.utils.network.NetworkResultParser
import com.example.koincleanarchitecture.utils.network.Result
import com.example.koincleanarchitecture.utils.paging.PagedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val Tag = "CharacterRepositoryImpl"
 class CharacterRepositoryImpl(private val remoteDataSource: CharacterRemoteDataSource):CharacterRepository,NetworkResultParser {
     override fun getAllCharacters(pageNo: Int): Flow<Result<PagedData<Character>>> {
         return remoteDataSource.getAllCharacters(pageNo = pageNo).map {networkResult->
             when(networkResult){
                 is NetworkResult.Loading->{
                     Result.Loading
                 }
                 is NetworkResult.Success->{
                     val data = networkResult.data!!.results.map { it.toCharacter() }?: emptyList()
                     val totalCount = networkResult.data.info.pages
                     var currentPage = pageNo
                     val previousKey = currentPage.toString()
                     val nextKey = if (pageNo==(networkResult.data.info.count-1)){
                         null
                     }else (++currentPage).toString()

                     Result.Success(
                         PagedData(
                             data = data,
                             prevKey = previousKey,
                             nextKey = nextKey,
                             totalCount = totalCount
                         )
                     )
                 }
                 is NetworkResult.Error->{
                     parseErrorNetworkResult(networkResult)
                 }
                 else->{
                     parseErrorNetworkResult(networkResult)
                 }
             }
         }
     }
 }
