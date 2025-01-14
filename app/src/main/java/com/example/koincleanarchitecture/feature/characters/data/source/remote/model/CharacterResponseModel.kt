package com.example.rickymortypagination.feature.rickymorty.data.source.remote.model

import com.example.koincleanarchitecture.feature.characters.data.source.remote.model.Info

data class CharacterResponseModel(
    val info: Info,
    val results: List<Result>
)