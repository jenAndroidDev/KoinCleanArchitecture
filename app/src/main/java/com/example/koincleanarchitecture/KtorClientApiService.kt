package com.example.koincleanarchitecture

import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorClientApiService(
    private val httpClient: HttpClient
) {
    companion object {
        private const val GET_CHARACTERS = "api/character/?page="
    }
    suspend fun getCharacters(pageNo: Int) = httpClient.get(GET_CHARACTERS.plus(pageNo))

}