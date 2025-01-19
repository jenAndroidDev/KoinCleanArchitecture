package com.example.koincleanarchitecture.feature.characters.presentation

import android.util.Log
import android.view.ViewGroup
import com.example.koincleanarchitecture.utils.paging.LoadState
import com.example.koincleanarchitecture.utils.paging.loadstateadapter.LoadStateAdapter
import timber.log.Timber


class CharactersLoadStateAdapter(
    private val retry: () -> Unit
):LoadStateAdapter<CharactersLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharactersLoadStateViewHolder {
        Timber.tag(Tag).d("onCreateViewHolder() called with: parent = " + parent + ", loadState = " + loadState)
        return CharactersLoadStateViewHolder.create(parent,retry)
    }

    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        Timber.tag(Tag).d("onBindViewHolder() called with: holder = " + holder + ", loadState = " + loadState)
        holder.bind(loadState = loadState)

    }
    companion object{
        private const val Tag = "CharacterLoadStateAdapter"
    }

}