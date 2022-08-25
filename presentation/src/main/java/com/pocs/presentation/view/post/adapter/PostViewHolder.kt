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
        // 마크다운 태그를 제거하기 위해서 사용한다. 게시글 목록에서는 제목을 강조하기 때문에 content는 마크다운 형식 없이
        // 보여야 한다. 그래서 마크다운 태그를 제거하고 있다.
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
        if (uiState.displayCategory) {
            subtitleText += middleDot + root.context.getString(uiState.category.koreanStringResource)
        }
        subtitle.text = subtitleText

        root.setOnClickListener { onClickItem(uiState) }
    }
}