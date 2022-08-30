package com.pocs.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.platform.app.InstrumentationRegistry
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.model.post.PostCreateUiState
import com.pocs.presentation.view.post.create.PostCreateScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostCreateScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var context: Context

    private fun getString(@StringRes resId: Int) = context.getString(resId)

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private val mockUiState = PostCreateUiState(
        title = "",
        content = TextFieldValue(),
        category = PostCategory.NOTICE,
        currentUserType = UserType.ADMIN,
        onlyMember = true,
        onTitleChange = {},
        onContentChange = {},
        onCategoryChange = {},
        onSave = { Result.success(Unit) },
        onOnlyMemberChange = {}
    )

    @Test
    fun shouldShowQuestionCreateTitle_WhenCurrentUserIsAnonymousAndPostCategoryIsQna() {
        composeRule.run {
            val uiState = mockUiState.copy(
                currentUserType = UserType.ANONYMOUS,
                category = PostCategory.QNA
            )
            setContent {
                PostCreateScreen(
                    uiState = uiState,
                    navigateUp = {},
                    onSuccessSave = {}
                )
            }

            onNodeWithText(getString(R.string.write_question)).assertIsDisplayed()
        }
    }

    @Test
    fun shouldShowDefaultPostCreateTitle_WhenCurrentUserIsMember() {
        composeRule.run {
            val uiState = mockUiState.copy(currentUserType = UserType.MEMBER)
            setContent {
                PostCreateScreen(
                    uiState = uiState,
                    navigateUp = {},
                    onSuccessSave = {}
                )
            }

            onNodeWithText(getString(R.string.write_post)).assertIsDisplayed()
        }
    }
}