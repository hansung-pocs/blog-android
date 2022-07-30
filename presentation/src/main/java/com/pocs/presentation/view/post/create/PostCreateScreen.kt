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
    onSuccessSave: () -> Unit
) {
    PostEditContent(
        // TODO: 게시글 속성에 따라 "OOO 편집"과 같이 다르게 보이기
        title = stringResource(R.string.write_post),
        uiState = uiState,
        navigateUp = navigateUp,
        onSuccessSave = onSuccessSave
    )
}