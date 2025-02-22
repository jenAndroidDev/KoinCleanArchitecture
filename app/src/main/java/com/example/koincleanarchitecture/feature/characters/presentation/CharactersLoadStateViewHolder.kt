package com.example.koincleanarchitecture.feature.characters.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.R
import com.example.koincleanarchitecture.databinding.LayoutLoadStateViewholderBinding
import com.example.koincleanarchitecture.utils.extension.makeVisible
import com.example.koincleanarchitecture.utils.paging.LoadState
import timber.log.Timber

private const val Tag = "CharactersLoadStateViewHolder"
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
        Timber.tag(Tag).d("bind() called with: loadState = " + loadState)
        if (loadState is LoadState.Error ){
            binding.tvErrorMessage.makeVisible()
            binding.tvErrorMessage.text = loadState.error.localizedMessage
        }
        //Toast.makeText(itemView.context,"LoadStateAdapter",Toast.LENGTH_LONG).show()
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