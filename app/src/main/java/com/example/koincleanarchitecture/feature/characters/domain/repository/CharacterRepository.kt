package com.example.koincleanarchitecture.feature.characters.domain.repository

import com.example.koincleanarchitecture.feature.characters.domain.model.Character
import com.example.koincleanarchitecture.utils.network.Result
import com.example.koincleanarchitecture.utils.paging.PagedData
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(pageNo:Int):Flow<Result<PagedData<Character>>>
}