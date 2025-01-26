package com.example.koincleanarchitecture.feature.characters.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String?=null
)