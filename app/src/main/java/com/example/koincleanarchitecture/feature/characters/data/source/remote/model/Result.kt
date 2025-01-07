package com.example.rickymortypagination.feature.rickymorty.data.source.remote.model


import com.example.koincleanarchitecture.feature.characters.data.source.remote.dto.LocationDto
import com.example.koincleanarchitecture.feature.characters.data.source.remote.dto.OriginDto
import com.example.koincleanarchitecture.feature.characters.domain.model.Character

data class Result(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: LocationDto,
    val name: String,
    val origin: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
){
    fun toCharacter(): Character {
        return Character(
            created = created,
            episode = episode,
            gender = gender,
            id = id,
            image = image,
            location = location.toLocation(),
            origin = origin.toOrigin(),
            species = species,
            status = status,
            type = type,
            url = url,
            name = name
        )
    }
}