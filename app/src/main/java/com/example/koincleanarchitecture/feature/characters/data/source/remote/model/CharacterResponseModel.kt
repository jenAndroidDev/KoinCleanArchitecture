package com.example.koincleanarchitecture.feature.characters.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponseModel(
    val info: Info,
    val results: List<Result>
)