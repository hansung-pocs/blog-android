package com.pocs.presentation.view.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.model.PostItemUiState

class PostAdapter : PagingDataAdapter<PostItemUiState, PostViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PostItemUiState>() {
            override fun areItemsTheSame(oldItem: PostItemUiState, newItem: PostItemUiState): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}