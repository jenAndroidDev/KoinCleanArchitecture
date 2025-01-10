package com.example.koincleanarchitecture.feature.characters.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.databinding.ItemLayoutCharactersBinding
import com.example.koincleanarchitecture.feature.characters.domain.model.Character

class CharactersAdapter:ListAdapter<CharacterUiModel,CharactersAdapter.CharacterViewHolder>(DiffUtil()) {


    class DiffUtil:ItemCallback<CharacterUiModel>(){
        override fun areItemsTheSame(
            oldItem: CharacterUiModel,
            newItem: CharacterUiModel
        ): Boolean {
            return (oldItem is CharacterUiModel.CharacterUiItem
                    && newItem is CharacterUiModel.CharacterUiItem
                    && oldItem.item.id==newItem.item.id)
        }

        override fun areContentsTheSame(
            oldItem: CharacterUiModel,
            newItem: CharacterUiModel
        ): Boolean {
            return oldItem==newItem

        }

    }
    inner class CharacterViewHolder(
        private val binding:ItemLayoutCharactersBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(model:Character) = with(binding){
            tvName.text =model.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = ItemLayoutCharactersBinding.inflate(LayoutInflater.from(
            parent.context),
            parent,
            false
        )
        return CharacterViewHolder((view))

    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        if (getItem(position) is CharacterUiModel){
            with(getItem(position) as CharacterUiModel.CharacterUiItem){
                holder.bind(this.item)
            }
        }
    }

}