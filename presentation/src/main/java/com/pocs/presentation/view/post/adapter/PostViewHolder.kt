package com.pocs.presentation.view.post.adapter

import androidx.core.view.isVisible
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemPostBinding
import com.pocs.presentation.extension.koreanStringResource
import com.pocs.presentation.model.post.item.PostItemUiState
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val onClickItem: (PostItemUiState) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: PostItemUiState) = with(binding) {
        category.isVisible = uiState.displayCategory
        if (uiState.displayCategory) {
            category.text = root.context.getString(uiState.category.koreanStringResource)
        }

        title.text = uiState.title
        // 마크다운 태그를 제거하기 위해서 사용한다. 게시글 목록에서는 제목을 강조하기 때문에 content는 마크다운 형식 없이
        // 보여야 한다. 그래서 마크다운 태그를 제거하고 있다.
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(uiState.content)
        val html = HtmlGenerator(uiState.content, parsedTree, flavour).generateHtml()
        content.text = html.parseAsHtml().toString().trimEnd()

        var subtitleText = uiState.createdAt
        val middleDot = root.context.getString(R.string.middle_dot)
        if (uiState.isDeleted) {
            val deletedPostPrefix = root.context.getString(R.string.deleted_post_subtitle_prefix)
            subtitleText = deletedPostPrefix + subtitleText
        }
        if (uiState.writer != null) {
            subtitleText += middleDot + uiState.writer
        }
        subtitle.text = subtitleText

        root.setOnClickListener { onClickItem(uiState) }
    }
}
