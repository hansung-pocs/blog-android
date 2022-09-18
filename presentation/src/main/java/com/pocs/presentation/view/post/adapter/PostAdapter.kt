package com.pocs.presentation.view.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.model.post.item.PostItemUiState

class PostAdapter(
    private val onClickItem: (PostItemUiState) -> Unit
) : PagingDataAdapter<PostItemUiState, PostViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return PostViewHolder(binding, onClickItem)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PostItemUiState>() {
            override fun areItemsTheSame(
                oldItem: PostItemUiState,
                newItem: PostItemUiState
            ): Boolean {
                return oldItem.id == newItem.id
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
