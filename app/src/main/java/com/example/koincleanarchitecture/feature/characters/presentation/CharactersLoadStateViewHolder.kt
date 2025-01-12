package com.example.koincleanarchitecture.feature.characters.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.R
import com.example.koincleanarchitecture.databinding.LayoutLoadStateViewholderBinding
import com.example.koincleanarchitecture.utils.extension.makeVisible
import com.example.koincleanarchitecture.utils.paging.LoadState

class CharactersLoadStateViewHolder(
    private val binding: LayoutLoadStateViewholderBinding,
    retry:()->Unit
):RecyclerView.ViewHolder(binding.root) {
    init {
        binding.btnRetry.setOnClickListener {
            retry.invoke()
        }
    }
    fun bind(loadState: LoadState){
        if (loadState is LoadState.Error ){
            binding.tvErrorMessage.makeVisible()
            binding.tvErrorMessage.text = loadState.error.localizedMessage
        }
        binding.pgNowStatusLoad.isVisible = loadState is LoadState.Loading
        binding.btnRetry.isVisible = loadState is LoadState.Error
    }
    companion object{
        fun create(parent: ViewGroup, retry: () -> Unit):CharactersLoadStateViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_load_state_viewholder,parent,false)
            val binding = LayoutLoadStateViewholderBinding.bind(view)
            return CharactersLoadStateViewHolder(binding,retry)
        }
    }
}