package com.example.koincleanarchitecture.feature.characters.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.koincleanarchitecture.databinding.LayoutLoadStateViewholderBinding
import com.example.koincleanarchitecture.utils.paging.LoadState
import com.example.koincleanarchitecture.utils.paging.loadstateadapter.LoadStateAdapter

class CharactersLoadStateAdapter(
    private val retry: () -> Unit
):LoadStateAdapter<CharactersLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CharactersLoadStateViewHolder {
        val binding = LayoutLoadStateViewholderBinding.inflate(
            LayoutInflater.from(
            parent.context
        ),parent,false)
        return CharactersLoadStateViewHolder(binding,retry)
    }

    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState = loadState)
    }

}