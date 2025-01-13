package com.example.koincleanarchitecture.feature.characters.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.koincleanarchitecture.databinding.ItemLayoutCharactersBinding
import com.example.koincleanarchitecture.databinding.LayoutLoadStateViewholderBinding

class DummyAdapter:ListAdapter<Sample,DummyAdapter.DummyViewHolder>(DiffUtil()) {

    inner class DummyViewHolder(
        private val binding: LayoutLoadStateViewholderBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(model:Sample) = with(binding){

        }
    }

    class DiffUtil:ItemCallback<Sample>(){
        override fun areItemsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Sample, newItem: Sample): Boolean {
            return oldItem==newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val view = LayoutLoadStateViewholderBinding.inflate(LayoutInflater.from(
            parent.context,
        ),parent,false)
        return DummyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }
}