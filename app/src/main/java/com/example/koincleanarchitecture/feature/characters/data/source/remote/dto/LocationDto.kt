package com.example.koincleanarchitecture.feature.characters.data.source.remote.dto

import com.example.koincleanarchitecture.feature.characters.domain.model.Location


data class LocationDto(
    val name: String,
    val url: String
){
    fun toLocation(): Location {
        return Location(
            name = name,
            url = url
        )
    }
}