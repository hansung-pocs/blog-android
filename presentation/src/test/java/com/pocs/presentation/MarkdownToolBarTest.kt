package com.pocs.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.pocs.presentation.view.component.markdown.MarkdownTag
import com.pocs.presentation.view.component.markdown.addMarkdownTag
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MarkdownToolBarTest {

    @Parameterized.Parameter(value = 0)
    lateinit var tag: MarkdownTag

    @Parameterized.Parameter(value = 1)
    lateinit var textFieldValue: TextFieldValue

    @Parameterized.Parameter(value = 2)
    lateinit var expected: TextFieldValue

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                arrayOf(
                    MarkdownTag.BOLD,
                    TextFieldValue(""),
                    TextFieldValue("****", selection = TextRange(2))
                ),
                arrayOf(
                    MarkdownTag.BOLD,
                    TextFieldValue("hello nice to meet you", selection = TextRange(6, 10)),
                    TextFieldValue("hello **nice** to meet you", selection = TextRange(12))
                ),
                arrayOf(
                    MarkdownTag.ITALIC,
                    TextFieldValue(""),
                    TextFieldValue("__", selection = TextRange(1))
                ),
                arrayOf(
                    MarkdownTag.ITALIC,
                    TextFieldValue("hello nice to meet you", selection = TextRange(6, 10)),
                    TextFieldValue("hello _nice_ to meet you", selection = TextRange(11))
                ),
                arrayOf(
                    MarkdownTag.LINK,
                    TextFieldValue(""),
                    TextFieldValue("[]()", selection = TextRange(4))
                ),
                arrayOf(
                    MarkdownTag.LINK,
                    TextFieldValue("hello\ngoogle link", selection = TextRange(6, 12)),
                    TextFieldValue("hello\n[google]() link", selection = TextRange(16))
                ),
                arrayOf(
                    MarkdownTag.LIST_ITEM,
                    TextFieldValue(""),
                    TextFieldValue("- ", selection = TextRange(2))
                ),
                arrayOf(
                    MarkdownTag.LIST_ITEM,
                    TextFieldValue("welcome\n\nhello nice to meet you.\ngood bye", TextRange(13)),
                    TextFieldValue(
                        "welcome\n\n- hello nice to meet you.\ngood bye",
                        selection = TextRange(15)
                    )
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue(""),
                    TextFieldValue("- [ ] ", selection = TextRange(6))
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue("- [ ] "),
                    TextFieldValue("- [x] ")
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue("- [x] "),
                    TextFieldValue("- [ ] ")
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue("- [X] "),
                    TextFieldValue("- [ ] ")
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue(
                        "hi\n\n\n- [X] content well..\n\nbye",
                        selection = TextRange(15)
                    ),
                    TextFieldValue(
                        "hi\n\n\n- [ ] content well..\n\nbye",
                        selection = TextRange(15)
                    )
                ),
                arrayOf(
                    MarkdownTag.TASK_LIST_ITEM,
                    TextFieldValue(
                        "hi\n\n\n- [x] content well..\n\nbye",
                        selection = TextRange(0)
                    ),
                    TextFieldValue(
                        "- [ ] hi\n\n\n- [x] content well..\n\nbye",
                        selection = TextRange(6)
                    )
                ),
            )
        }
    }

    @Test
    fun addMarkdownTagTest() {
        assertEquals(expected, textFieldValue.addMarkdownTag(tag))
    }
}