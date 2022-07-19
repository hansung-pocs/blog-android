package com.pocs.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.presentation.databinding.ItemArticleBinding

class ArticleAdapter : PagingDataAdapter<ArticleItemUiState, ArticleViewHolder >(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleBinding.inflate(layoutInflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleItemUiState>() {
            override fun areItemsTheSame(oldItem: ArticleItemUiState, newItem: ArticleItemUiState): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticleItemUiState,
                newItem: ArticleItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}