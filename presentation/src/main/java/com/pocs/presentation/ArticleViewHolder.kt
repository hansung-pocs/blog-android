package com.pocs.presentation

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.databinding.ItemArticleBinding

class ArticleViewHolder(
    private val binding: ItemArticleBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: ArticleItemUiState) = with(binding) {
        title.text = uiState.title
        subtitle.text = root.context.getString(
            R.string.article_subtitle,
            uiState.date,
            uiState.writer
        )
    }
}