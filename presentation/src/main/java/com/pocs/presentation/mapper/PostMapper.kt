package com.pocs.presentation.mapper

import androidx.core.text.parseAsHtml
import com.pocs.domain.model.post.Post
import com.pocs.presentation.extension.toFormattedDateString
import com.pocs.presentation.model.post.item.PostItemUiState
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

private fun String.parseMarkdown(): String {
    val flavour = CommonMarkFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(this)
    val html = HtmlGenerator(this, parsedTree, flavour).generateHtml()
    return html.parseAsHtml().toString().trimEnd()
}

fun Post.toUiState(displayCategory: Boolean = false) = PostItemUiState(
    id = id,
    title = title,
    // 마크다운 태그를 제거하기 위해서 사용한다. 게시글 목록에서는 제목을 강조하기 때문에 content는 마크다운 형식 없이
    // 보여야 한다. 그래서 마크다운 태그를 제거하고 있다.
    content = content.parseMarkdown(),
    category = category,
    displayCategory = displayCategory,
    writer = writer,
    createdAt = createdAt.toFormattedDateString(),
    canceledAt = canceledAt
)
