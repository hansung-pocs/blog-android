package com.pocs.presentation.view.component

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.IntegrationInstructions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.ImageLoader
import com.google.android.material.color.MaterialColors
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    @FontRes fontResource: Int? = null,
    style: TextStyle = LocalTextStyle.current,
    @IdRes viewId: Int? = null,
    onClick: (() -> Unit)? = null,
    // this option will disable all clicks on links, inside the markdown text
    // it also enable the parent view to receive the click event
    disableLinkMovementMethod: Boolean = false,
) {
    val defaultColor: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    val context: Context = LocalContext.current
    val markdownRender: Markwon = remember { Markdown.createMarkdownRender(context) }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            createTextView(
                context = ctx,
                color = color,
                defaultColor = defaultColor,
                fontSize = fontSize,
                fontResource = fontResource,
                maxLines = maxLines,
                style = style,
                textAlign = textAlign,
                viewId = viewId,
                onClick = onClick,
            )
        },
        update = { textView ->
            markdownRender.setMarkdown(textView, markdown)
            if (disableLinkMovementMethod) {
                textView.movementMethod = null
            }
        }
    )
}

private fun createTextView(
    context: Context,
    color: Color = Color.Unspecified,
    defaultColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    @FontRes fontResource: Int? = null,
    style: TextStyle,
    @IdRes viewId: Int? = null,
    onClick: (() -> Unit)? = null
): TextView {

    val textColor = color.takeOrElse { style.color.takeOrElse { defaultColor } }
    val mergedStyle = style.merge(
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            textAlign = textAlign,
        )
    )
    return TextView(context).apply {
        onClick?.let { setOnClickListener { onClick() } }
        setTextColor(textColor.toArgb())
        setMaxLines(maxLines)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, mergedStyle.fontSize.value)

        viewId?.let { id = viewId }
        textAlign?.let { align ->
            textAlignment = when (align) {
                TextAlign.Left, TextAlign.Start -> View.TEXT_ALIGNMENT_TEXT_START
                TextAlign.Right, TextAlign.End -> View.TEXT_ALIGNMENT_TEXT_END
                TextAlign.Center -> View.TEXT_ALIGNMENT_CENTER
                else -> View.TEXT_ALIGNMENT_TEXT_START
            }
        }

        fontResource?.let { font ->
            typeface = ResourcesCompat.getFont(context, font)
        }
    }
}

@PrismBundle(includeAll = true)
private object Markdown {
    fun createMarkdownRender(context: Context): Markwon {
        val imageLoader = ImageLoader.Builder(context)
            .apply {
                crossfade(true)
            }.build()

        return Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(CoilImagesPlugin.create(context, imageLoader))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(
                TaskListPlugin.create(
                    MaterialColors.getColor(
                        context,
                        com.google.android.material.R.attr.colorPrimary,
                        ""
                    ),
                    MaterialColors.getColor(
                        context,
                        com.google.android.material.R.attr.colorOnBackground,
                        ""
                    ),
                    MaterialColors.getColor(
                        context,
                        com.google.android.material.R.attr.backgroundColor,
                        ""
                    ),
                )
            )
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(
                SyntaxHighlightPlugin.create(
                    Prism4j(GrammarLocatorDef()),
                    Prism4jThemeDefault.create()
                )
            )
            .build()
    }
}


// TODO: IMAGE 추가하기
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