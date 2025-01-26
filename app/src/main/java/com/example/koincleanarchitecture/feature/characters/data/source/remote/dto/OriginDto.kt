package com.example.koincleanarchitecture.feature.characters.data.source.remote.dto

import com.example.koincleanarchitecture.feature.characters.domain.model.Origin
import kotlinx.serialization.Serializable

@Serializable
data class OriginDto(
    val name: String,
    val url: String
){
    fun toOrigin(): Origin {
        return Origin(
            name = name,
            url = url
        )
    }
}