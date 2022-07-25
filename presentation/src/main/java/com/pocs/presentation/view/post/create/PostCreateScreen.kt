package com.pocs.presentation.view.post.create

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pocs.presentation.R
import com.pocs.presentation.model.PostCreateUiState
import com.pocs.presentation.view.post.edit.PostEditContent

@Composable
fun PostCreateScreen(uiState: PostCreateUiState) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    PostEditContent(
        // TODO: 게시글 속성에 따라 "OOO 편집"과 같이 다르게 보이기
        appBarTitle = stringResource(R.string.write_post),
        title = uiState.title,
        content = uiState.content,
        isInSaving = uiState.isInSaving,
        enableSendIcon = uiState.canSave,
        onBackPressed = { onBackPressedDispatcher?.onBackPressed() },
        onTitleChange = uiState.onTitleChange,
        onContentChange = uiState.onContentChange,
        onSave = uiState.onSave
    )
}