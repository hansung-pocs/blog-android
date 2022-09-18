package com.pocs.presentation

import com.pocs.presentation.mapper.toUiState
import com.pocs.test_library.mock.mockPost
import org.junit.Assert.assertEquals
import org.junit.Test

class PostMapperTest {

    @Test
    fun parseMarkdownOfContent() {
        val rawText = "# This is Header This is _italic_"
        val parsedText = "This is Header This is italic"
        val post = mockPost.copy(content = rawText)

        val uiState = post.toUiState()

        assertEquals(parsedText, uiState.content)
    }
}
