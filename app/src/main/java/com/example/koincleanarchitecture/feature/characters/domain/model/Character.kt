package com.example.koincleanarchitecture.feature.characters.domain.model


data class Character(
    val created: String = "",
    val episode: List<String> = arrayListOf(),
    val gender: String = "",
    val id: Int = -1,
    val image: String="",
    val location: Location = Location("",""),
    val name: String="",
    val origin: Origin = Origin("",""),
    val species: String="",
    val status: String="",
    val type: String="",
    val url: String="",
    val readStatus:Boolean=false
)
