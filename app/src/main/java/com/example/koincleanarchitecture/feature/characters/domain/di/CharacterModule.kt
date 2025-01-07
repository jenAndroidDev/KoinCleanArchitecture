package com.example.koincleanarchitecture.feature.characters.domain.di

import com.example.koincleanarchitecture.feature.characters.data.repository.CharacterRepositoryImpl
import com.example.koincleanarchitecture.feature.characters.data.source.remote.CharacterRemoteDataSourceImpl
import com.example.koincleanarchitecture.feature.characters.domain.model.Character
import com.example.koincleanarchitecture.feature.characters.domain.repository.CharacterRepository
import com.example.koincleanarchitecture.feature.characters.domain.source.remote.CharacterRemoteDataSource
import com.example.koincleanarchitecture.feature.characters.presentation.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val characterFeatureModule = module {
    singleOf(::CharacterRemoteDataSourceImpl).bind<CharacterRemoteDataSource>()
    singleOf(::CharacterRepositoryImpl).bind<CharacterRepository>()
    viewModel { MainActivityViewModel(get()) }
}