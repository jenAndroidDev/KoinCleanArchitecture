package com.example.koincleanarchitecture.feature.characters.domain.repository

import com.example.koincleanarchitecture.feature.characters.data.source.remote.model.CharacterResponseModel
import com.pepul.shopsseller.utils.paging.PagedData
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(pageNo:Int):Flow<Result<PagedData<CharacterResponseModel>>>
}