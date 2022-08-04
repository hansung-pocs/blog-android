package com.pocs.presentation.view.post.adapter

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.model.post.item.PostItemUiState

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onClickItem: (PostItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: PostItemUiState) = with(binding) {
        title.text = uiState.title
        content.text = uiState.content

        var subtitleText = uiState.createdAt
        if (uiState.isDeleted) {
            val deletedPostPrefix = root.context.getString(R.string.deleted_post_subtitle_prefix)
            subtitleText = deletedPostPrefix + subtitleText
        }
        if (uiState.writer != null) {
            val middleDot = root.context.getString(R.string.middle_dot)
            subtitleText = subtitleText + middleDot + uiState.writer
        }
        subtitle.text = subtitleText

        root.setOnClickListener { onClickItem(uiState) }
    }
}