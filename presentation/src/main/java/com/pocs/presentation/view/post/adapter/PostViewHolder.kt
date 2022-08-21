package com.pocs.presentation.view.post.adapter

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.extension.koreanStringResource
import com.pocs.presentation.model.post.item.PostItemUiState
import io.noties.markwon.*

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onClickItem: (PostItemUiState) -> Unit,
    private val markwon : Markwon
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: PostItemUiState) = with(binding) {
        title.text = uiState.title
        // 마크다운 태크를 제거하기 위해서 사용한다.
        markwon.setMarkdown(content, uiState.content)
        content.movementMethod = null

        var subtitleText = uiState.createdAt
        val middleDot = root.context.getString(R.string.middle_dot)
        if (uiState.isDeleted) {
            val deletedPostPrefix = root.context.getString(R.string.deleted_post_subtitle_prefix)
            subtitleText = deletedPostPrefix + subtitleText
        }
        if (uiState.writer != null) {
            subtitleText += middleDot + uiState.writer
        }
        if (uiState.category != null) {
            subtitleText += middleDot + root.context.getString(uiState.category.koreanStringResource)
        }

        subtitle.text = subtitleText

        root.setOnClickListener { onClickItem(uiState) }
    }
}