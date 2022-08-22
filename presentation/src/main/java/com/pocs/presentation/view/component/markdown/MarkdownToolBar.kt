package com.pocs.presentation.view.component.markdown

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.IntegrationInstructions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TODO: IMAGE 추가하기
@VisibleForTesting
enum class MarkdownTag(val imageVector: ImageVector) {
    BOLD(Icons.Default.FormatBold),
    ITALIC(Icons.Default.FormatItalic),
    LINK(Icons.Default.Link),
    LIST_ITEM(Icons.Default.List),
    TASK_LIST_ITEM(Icons.Outlined.CheckBox),
    HEADING(Icons.Default.HMobiledata),
    STRIKETHROUGH(Icons.Default.StrikethroughS),
    QUOTE(Icons.Default.FormatQuote),
    CODE_HIGHLIGHT(Icons.Default.Code),
    CODE_BLOCK(Icons.Outlined.IntegrationInstructions)
}

@Composable
fun MarkdownToolBar(textFieldValue: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .horizontalScroll(rememberScrollState())
    ) {
        val tags = remember { MarkdownTag.values() }
        for (tag in tags) {
            MarkdownToolBarItemButton(tag) {
                onValueChange(textFieldValue.addMarkdownTag(tag))
            }
        }
    }
}

@VisibleForTesting
fun TextFieldValue.addMarkdownTag(tag: MarkdownTag): TextFieldValue {
    val text = this.text
    val selection = this.selection

    return when (tag) {
        MarkdownTag.BOLD -> TextFieldValue(
            text = text.wrapWith("**", selection.start, selection.end),
            selection = TextRange(selection.end + 2)
        )
        MarkdownTag.ITALIC -> TextFieldValue(
            text = text.wrapWith("_", selection.start, selection.end),
            selection = TextRange(selection.end + 1)
        )
        MarkdownTag.LINK -> TextFieldValue(
            text = text.wrapWith(
                prefix = "[",
                suffix = "]()",
                selection.start,
                selection.end
            ),
            selection = TextRange(selection.end + 4)
        )
        MarkdownTag.LIST_ITEM -> TextFieldValue(
            text = text.insertAtFrontOfLine("- ", selection.start),
            selection = TextRange(selection.end + 2)
        )
        MarkdownTag.TASK_LIST_ITEM -> {
            var newText = text.checkIfHasCheckBoxTag(selection.start)
            var newTextRange = TextRange(selection.end)
            // 체크박스가 존재하지 않았던 경우 새로 체크박스 태그를 넣어준다.
            if (newText == text) {
                newText = text.insertAtFrontOfLine("- [ ] ", selection.start)
                newTextRange = TextRange(selection.end + 6)
            }
            TextFieldValue(
                text = newText,
                selection = newTextRange
            )
        }
        MarkdownTag.HEADING -> {
            val hasHeadingTag = text.hasHeadingTagAtLine(selection.start)
            val headingTag = if (hasHeadingTag) "#" else "# "

            TextFieldValue(
                text = text.insertAtFrontOfLine(headingTag, selection.start),
                selection = TextRange(
                    index = selection.end + headingTag.length
                )
            )
        }
        MarkdownTag.STRIKETHROUGH -> TextFieldValue(
            text = text.wrapWith("~~", selection.start, selection.end),
            selection = TextRange(selection.end + 2)
        )
        MarkdownTag.QUOTE -> TextFieldValue(
            text = text.insertAtFrontOfLine("> ", selection.start),
            selection = TextRange(selection.end + 2)
        )
        MarkdownTag.CODE_HIGHLIGHT -> TextFieldValue(
            text = text.wrapWith("`", selection.start, selection.end),
            selection = TextRange(selection.end + 1)
        )
        MarkdownTag.CODE_BLOCK -> TextFieldValue(
            text = text.wrapWith(
                prefix = "```\n",
                suffix = "\n```",
                selection.start,
                selection.end
            ),
            selection = TextRange(selection.start + 3)
        )
    }
}

private fun String.replaceAt(char: Char, index: Int): String {
    return substring(0, index) + char + substring(index + 1, length)
}

private fun String.wrapWith(prefix: String, suffix: String, start: Int, end: Int): String {
    return substring(0, start) + prefix + substring(start, end) + suffix + substring(end, length)
}

private fun String.wrapWith(str: String, start: Int, end: Int): String {
    return wrapWith(str, str, start, end)
}

private fun String.firstIndexOfLine(cursorPosition: Int): Int {
    return substring(0, cursorPosition).lastIndexOf('\n') + 1
}

private fun String.insertAtFrontOfLine(str: String, cursorPosition: Int): String {
    val targetIndex = firstIndexOfLine(cursorPosition)
    return substring(0, targetIndex) + str + substring(targetIndex, length)
}

private fun String.hasHeadingTagAtLine(cursorPosition: Int): Boolean {
    if (isEmpty()) {
        return false
    }
    return this[firstIndexOfLine(cursorPosition)] == '#'
}

private fun String.checkIfHasCheckBoxTag(cursorPosition: Int): String {
    val start = firstIndexOfLine(cursorPosition)
    val end = minOf(start + 6, length)
    val thisCheckBoxString = substring(start, end)
    val hasUncheckedBox = thisCheckBoxString == "- [ ] "
    val hasCheckedBox = thisCheckBoxString == "- [x] " || thisCheckBoxString == "- [X] "
    return if (hasUncheckedBox) {
        replaceAt('x', start + 3)
    } else if (hasCheckedBox) {
        replaceAt(' ', start + 3)
    } else {
        this
    }
}

@Composable
private fun MarkdownToolBarItemButton(tag: MarkdownTag, onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        imageVector = tag.imageVector,
        contentDescription = tag.name
    )
}

@Preview
@Composable
private fun MarkdownToolBarPreview() {
    MarkdownToolBar(textFieldValue = TextFieldValue(), onValueChange = {})
}