package com.pocs.presentation.view.post.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R
import com.pocs.presentation.model.post.PostCreateUiState
import com.pocs.presentation.view.post.edit.PostEditContent

@Composable
fun PostCreateScreen(
    uiState: PostCreateUiState,
    navigateUp: () -> Unit,
    onSuccessSave: () -> Unit,
) {
    PostEditContent(
        title = stringResource(R.string.write_post),
        uiState = uiState,
        navigateUp = navigateUp,
        onSuccessSave = onSuccessSave
    )
}