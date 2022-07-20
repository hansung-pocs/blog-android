package com.pocs.presentation.view.common

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.model.PostItemUiState

class PostViewHolder(
    private val binding: ItemPostBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: PostItemUiState) = with(binding) {
        title.text = uiState.title
        subtitle.text = root.context.getString(
            R.string.article_subtitle,
            uiState.date,
            uiState.writer
        )
    }
}