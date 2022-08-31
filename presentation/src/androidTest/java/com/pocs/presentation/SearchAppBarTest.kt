package com.pocs.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.pocs.presentation.view.component.appbar.SearchAppBar
import com.pocs.presentation.view.component.appbar.searchDebounceDelay
import com.pocs.presentation.view.component.appbar.searchTextFieldContentDescription
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class SearchAppBarTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldCallOnSearchEvent_WhenTypedTextIsBlank() {
        composeRule.run {
            var count = 0
            setSearchAppBar(onSearch = { count++ })

            val query = "    "
            findSearchTextField().performTextInput(query)
            onNodeWithText(query).assertIsDisplayed()

            assertEquals(0, count)
        }
    }

    @Test
    fun shouldCallOnSearchEvent_WhenTypingText() {
        composeRule.run {
            var count = 0
            setSearchAppBar(onSearch = { count++ })

            val query = "hello"
            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(searchDebounceDelay)

            assertEquals(1, count)
        }
    }

    @Test
    fun shouldDebounceOnSearchEventCorrectly_WhenTypingText() {
        composeRule.run {
            var count = 0
            setSearchAppBar(onSearch = { count++ })

            val query = "hello"
            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(100)

            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(200)

            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(searchDebounceDelay)

            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(200)

            findSearchTextField().performTextInput(query)
            waitForIdle()
            mainClock.advanceTimeBy(searchDebounceDelay)

            assertEquals(2, count)
        }
    }

    private fun findSearchTextField(): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription(searchTextFieldContentDescription)
    }

    private fun setSearchAppBar(onSearch: (String) -> Unit = {}) {
        composeRule.setContent {
            Scaffold(
                topBar = {
                    SearchAppBar(
                        title = "",
                        enabledSearchMode = true,
                        onSearchModeChange = {},
                        onSearch = onSearch
                    )
                }
            ) {
                Box(Modifier.padding(it))
            }
        }
    }
}