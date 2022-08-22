package com.pocs.presentation.view.component.markdown

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.LineHeightSpan
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import coil.ImageLoader
import com.google.android.material.color.MaterialColors
import com.pocs.presentation.extension.toDp
import com.pocs.presentation.extension.syncLineHeight
import io.noties.markwon.*
import io.noties.markwon.core.MarkwonTheme
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
import org.commonmark.node.BulletList

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
        syncLineHeight(32f)
        // 링크를 클릭했을 때 이동 가능하게 하려면 아래의 selectable을 먼저 true로 설정하고나서 movement를 설정해야한다.
        setTextIsSelectable(true)
        movementMethod = LinkMovementMethod.getInstance()

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
        val colorOnBackground = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorOnBackground,
            ""
        )
        val backgroundColor = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.backgroundColor,
            ""
        )

        return Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(CoilImagesPlugin.create(context, imageLoader))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(TaskListPlugin.create(colorOnBackground, colorOnBackground, backgroundColor))
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(
                SyntaxHighlightPlugin.create(
                    Prism4j(GrammarLocatorDef()),
                    Prism4jThemeDefault.create()
                )
            )
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.appendFactory(BulletList::class.java) { _, _ ->
                        // https://github.com/noties/Markwon/issues/413 를 해결하기 전에 사용하는 임시 해결책이다.
                        FirstLineSpacingSpan((-24f).toDp())
                    }
                }
            })
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder.bulletWidth(20).blockMargin(104)
                }
            })
            .build()
    }
}

class FirstLineSpacingSpan(private val spacing: Int) : LineHeightSpan {
    private var startAscent = 0
    private var startTop = 0
    override fun chooseHeight(
        text: CharSequence,
        start: Int,
        end: Int,
        spanstartv: Int,
        v: Int,
        fm: Paint.FontMetricsInt
    ) {
        val spanStart = (text as Spanned).getSpanStart(this)
        if (start == spanStart) {

            // save these values, we will use them to restore fm state (if other lines are present)
            // if we do not, then all the subsequent lines will have space at the top
            startAscent = fm.ascent
            startTop = fm.top

            // obtain previous spans (if none -> we are first, no need to add spacing)
            // `-2` because there is a new-line character that won't have list-item span
            val spans = text.getSpans(
                start - 2, start,
                FirstLineSpacingSpan::class.java
            )
            if (spans != null && spans.isNotEmpty()) {
                fm.ascent -= spacing
                fm.top -= spacing
            }
        } else {
            // reset the values...
            fm.ascent = startAscent
            fm.top = startTop
        }
    }
}