package com.pocs.presentation.view.post.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.model.post.PostCreateUiState
import com.pocs.presentation.view.post.edit.PostEditContent

@Composable
fun PostCreateScreen(
    uiState: PostCreateUiState,
    navigateUp: () -> Unit,
    onSuccessSave: () -> Unit
) {
    val title = if (uiState.category == PostCategory.QNA && uiState.isUserAnonymous) {
        stringResource(R.string.write_question)
    } else {
        stringResource(R.string.write_post)
    }
    PostEditContent(
        title = title,
        uiState = uiState,
        navigateUp = navigateUp,
        onSuccessSave = onSuccessSave
    )
}
